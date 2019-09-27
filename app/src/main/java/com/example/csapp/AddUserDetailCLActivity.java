package com.example.csapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class AddUserDetailCLActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText etFirstName, etLastName, etAge, etDateOfBirth, etPhoneNumber, etDescription;
    ImageView ivProfilePicture;
    Button btnNext;

    String mStoragePath = "Images/Profile Pictures/";
    String mDatabasePath = "Users";
    Uri mFilePathUri;
    DatabaseReference mDatabaseReference;
    ProgressDialog mProgressDialog;
    int IMAGE_REQUEST_CODE = 5;
    Spinner spState;
    private StorageReference mStorageReference;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_detail);

        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etDateOfBirth = findViewById(R.id.et_date_of_birth);
        etAge = findViewById(R.id.et_age);
        ivProfilePicture = findViewById(R.id.iv_profile_picture);
        btnNext = findViewById(R.id.btn_next);
        spState = findViewById(R.id.sp_state);
        etPhoneNumber = findViewById(R.id.et_phone_number);

        etDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddUserDetailCLActivity.this, mDateSetListener, year, month, day);
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

//        Bundle intent = getIntent().getExtras();
//        if (intent != null) {
//            accountType = intent.getString("accountType");
//            firstName = intent.getString("firstName");
//            lastName = intent.getString("lastName");
//            dateOfBirth = intent.getString("dateOfBirth");
//            age = intent.getString("age");
//            phoneNumber = intent.getString("phoneNumber");
//            nationality = intent.getString("nationality");
//            state = intent.getString("state");
//            description = intent.getString("description");
//            image = intent.getString("image");
//
//            etFirstName.setText(firstName);
//            etDescription.setText(description);
//            Picasso.get().load(image).into(ivProfilePicture);
//
//            btnNext.setText("Update");
//        }

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

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnNext.getText().equals("Next")) {
                    uploadDataToFirebase();
                } else {
//                    beginUpdate();
                }
            }
        });

        mStorageReference = getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(mDatabasePath);
        mProgressDialog = new ProgressDialog(AddUserDetailCLActivity.this);
    }

//    private void beginUpdate() {
//        mProgressDialog.setMessage("Updating...");
//        mProgressDialog.show();
//        deletePreviousImage();
//    }

//    private void deletePreviousImage() {
//        StorageReference mPictureRef = getInstance().getReferenceFromUrl(image);
//        mPictureRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(AddUserDetailCLActivity.this, "Previous image deleted.", Toast.LENGTH_SHORT).show();
//                uploadNewImage();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(AddUserDetailCLActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                mProgressDialog.dismiss();
//            }
//        });
//    }

//    private void uploadNewImage() {
//        String imageName = System.currentTimeMillis() + ".png";
//        StorageReference storageReference2 = mStorageReference.child(mStoragePath + imageName);
//        Bitmap bitmap = ((BitmapDrawable) ivProfilePicture.getDrawable()).getBitmap();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        byte[] data = baos.toByteArray();
//        UploadTask uploadTask = storageReference2.putBytes(data);
//        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(AddUserDetailCLActivity.this, "New image uploaded.", Toast.LENGTH_SHORT).show();
//
//                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                while (!uriTask.isSuccessful()) ;
//                Uri downloadUri = uriTask.getResult();
//                updateDatabase(downloadUri.toString());
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(AddUserDetailCLActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                mProgressDialog.dismiss();
//            }
//        });
//    }

//    private void updateDatabase(final String image) {
//        final String accountType = spAccountType.getSelectedItem().toString();
//        final String firstName = etFirstName.getText().toString();
//        final String lastName = etLastName.getText().toString();
//        final String dateOfBirth = etDateOfBirth.getText().toString();
//        final String age = etAge.getText().toString();
//        final String phoneNumber = etPhoneNumber.getText().toString();
//        final String nationality = spNationality.getSelectedItem().toString();
//        final String state = spState.getSelectedItem().toString();
//        final String description = etDescription.getText().toString();
//        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference mRef = mFirebaseDatabase.getReference("Users");
//
//        Query query = mRef.orderByChild("firstName").equalTo(this.firstName);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    ds.getRef().child("accountType").setValue(accountType);
//                    ds.getRef().child("firstName").setValue(firstName);
//                    ds.getRef().child("lastName").setValue(lastName);
//                    ds.getRef().child("dateOfBirth").setValue(dateOfBirth);
//                    ds.getRef().child("age").setValue(age);
//                    ds.getRef().child("phoneNumber").setValue(phoneNumber);
//                    ds.getRef().child("nationality").setValue(nationality);
//                    ds.getRef().child("state").setValue(state);
//                    ds.getRef().child("description").setValue(description);
//                    ds.getRef().child("image").setValue(image);
//                }
//                mProgressDialog.dismiss();
//                Toast.makeText(AddUserDetailCLActivity.this, "Data updated.", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(AddUserDetailCLActivity.this, MainActivity.class));
//                finish();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void uploadDataToFirebase() {
        if (mFilePathUri != null) {
            mProgressDialog.setTitle("Uploading");
            mProgressDialog.show();
            StorageReference storageReference2 = mStorageReference.child(mStoragePath +
                    System.currentTimeMillis() + "." + getFileExtention(mFilePathUri));
            storageReference2.putFile(mFilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri downloadUri = uriTask.getResult();

                            String accountType = "cl";
                            String verified = "f";
                            String firstName = etFirstName.getText().toString().trim();
                            String lastName = etLastName.getText().toString().trim();
                            String dateOfBirth = etDateOfBirth.getText().toString().trim();
                            String age = etAge.getText().toString().trim();
                            String phoneNumber = etPhoneNumber.getText().toString().trim();
                            String state = spState.getSelectedItem().toString().trim();
                            mProgressDialog.dismiss();
                            Toast.makeText(AddUserDetailCLActivity.this, "Uploaded successfully.",
                                    Toast.LENGTH_SHORT).show();

                            UploadInformation uploadInformation = new UploadInformation(accountType,
                                    verified, firstName, lastName, dateOfBirth, age, phoneNumber,
                                    state, downloadUri.toString());

//                            String imageUploadId = mDatabaseReference.push().getKey();
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String imageUploadId = user.getUid();
                            mDatabaseReference.child(imageUploadId).setValue(uploadInformation);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressDialog.dismiss();
                            Toast.makeText(AddUserDetailCLActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressDialog.setTitle("Uploading...");
                        }
                    });
        } else {
            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtention(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String nationality = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
