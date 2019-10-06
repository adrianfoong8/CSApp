package com.example.csapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class EditProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText etFirstName, etLastName, etAge, etDateOfBirth, etPhoneNumber;
    private String sFirstName, sLastName, sImage, sAge, sDateOfBirth, sPhoneNumber, sState;
    private ImageView ivProfilePicture;
    private Button btnSave;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private DatabaseReference ref, ref2;
    private String mStoragePath = "Images/Profile Pictures/";
    private String mDatabasePath = "Users";
    private Uri mFilePathUri;
    private DatabaseReference mDatabaseReference;
    private ProgressDialog mProgressDialog;
    private int IMAGE_REQUEST_CODE = 5;
    private Spinner spState;
    private StorageReference mStorageReference;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etDateOfBirth = findViewById(R.id.et_date_of_birth);
        etAge = findViewById(R.id.et_age);
        ivProfilePicture = findViewById(R.id.iv_profile_picture);
        btnSave = findViewById(R.id.btn_save);
        spState = findViewById(R.id.sp_state);
        etPhoneNumber = findViewById(R.id.et_phone_number);

        etDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(EditProfileActivity.this, mDateSetListener, year, month, day);
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                etDateOfBirth.setText(date);

                Calendar calendar2 = Calendar.getInstance();
                int age = calendar2.get(Calendar.YEAR) - year;
                Integer iAge = new Integer(age);
                String sAge = iAge.toString();
                etAge.setText(sAge);
            }
        };

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.state, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spState.setAdapter(adapter);
        spState.setOnItemSelectedListener(this);

        ivProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"),
                        IMAGE_REQUEST_CODE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginUpdate();
            }
        });

        mStorageReference = getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(mDatabasePath);
        mProgressDialog = new ProgressDialog(EditProfileActivity.this);

        Bundle editIntent = getIntent().getExtras();
        if (editIntent != null) {
            sFirstName = editIntent.getString("sFirstName");
            sLastName = editIntent.getString("sLastName");
            sImage = editIntent.getString("sImage");
            sDateOfBirth = editIntent.getString("sDateOfBirth");
            sAge = editIntent.getString("sAge");
            sPhoneNumber = editIntent.getString("sPhoneNumber");
            sState = editIntent.getString("sState");

            etFirstName.setText(sFirstName);
            etLastName.setText(sLastName);
            Picasso.get().load(sImage).into(ivProfilePicture);
            etDateOfBirth.setText(sDateOfBirth);
            etAge.setText(sAge);
            etPhoneNumber.setText(sPhoneNumber);
            spState.setSelection(getIndex(spState, sState));
        }
    }

    private int getIndex(Spinner spinner, String string) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(string)) {
                return i;
            }
        }
        return 0;
    }

    private void beginUpdate() {
        mProgressDialog.setTitle("Updating");
        mProgressDialog.show();
        deletePreviousImage();
    }

    private void deletePreviousImage() {
        StorageReference mPictureRef = getInstance().getReferenceFromUrl(sImage);
        mPictureRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditProfileActivity.this, "Previous image deleted.", Toast.LENGTH_SHORT).show();
                uploadNewImage();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
    }

    private void uploadNewImage() {
        String imageName = System.currentTimeMillis() + ".png";
        StorageReference storageReference2 = mStorageReference.child(mStoragePath + imageName);
        Bitmap bitmap = ((BitmapDrawable) ivProfilePicture.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storageReference2.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(EditProfileActivity.this, "New image uploaded.", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                Uri downloadUri = uriTask.getResult();
                updateDatabase(downloadUri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
    }

    private void updateDatabase(final String image) {
        final String firstName = etFirstName.getText().toString();
        final String lastName = etLastName.getText().toString();
        final String dateOfBirth = etDateOfBirth.getText().toString();
        final String age = etAge.getText().toString();
        final String phoneNumber = etPhoneNumber.getText().toString();
        final String state = spState.getSelectedItem().toString();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mFirebaseDatabase.getReference("Users");

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = mRef.orderByChild("uid").equalTo(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ds.getRef().child("firstName").setValue(firstName);
                    ds.getRef().child("lastName").setValue(lastName);
                    ds.getRef().child("dateOfBirth").setValue(dateOfBirth);
                    ds.getRef().child("age").setValue(age);
                    ds.getRef().child("phoneNumber").setValue(phoneNumber);
                    ds.getRef().child("state").setValue(state);
                    ds.getRef().child("image").setValue(image);
                }
                mProgressDialog.dismiss();
                Toast.makeText(EditProfileActivity.this, "Data updated.", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String nationality = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {
            mFilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),
                        mFilePathUri);
                ivProfilePicture.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
