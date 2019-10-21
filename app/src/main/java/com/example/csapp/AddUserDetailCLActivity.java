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

    private EditText etFirstName, etLastName, etAge, etDateOfBirth, etPhoneNumber, etTnC, etOTnC;
    private ImageView ivProfilePicture;
    private Button btnNext;
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
        setContentView(R.layout.activity_add_user_detail);

        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etDateOfBirth = findViewById(R.id.et_date_of_birth);
        etAge = findViewById(R.id.et_age);
        ivProfilePicture = findViewById(R.id.iv_profile_picture);
        btnNext = findViewById(R.id.btn_next);
        spState = findViewById(R.id.sp_state);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        etTnC = findViewById(R.id.et_terms_conditions);
        etOTnC = findViewById(R.id.et_other_terms_conditions);

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
                uploadDataToFirebase();
            }
        });

        mStorageReference = getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(mDatabasePath);
        mProgressDialog = new ProgressDialog(AddUserDetailCLActivity.this);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String imageUploadId = user.getUid();
        String newStoragePath = mStoragePath + imageUploadId + "/";
        mStoragePath = newStoragePath;
    }

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

                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = user.getUid();
                            String accountType = "clf";
                            String firstName = etFirstName.getText().toString().trim();
                            String lastName = etLastName.getText().toString().trim();
                            String dateOfBirth = etDateOfBirth.getText().toString().trim();
                            String age = etAge.getText().toString().trim();
                            String phoneNumber = etPhoneNumber.getText().toString().trim();
                            String state = spState.getSelectedItem().toString().trim();
                            String tnc = etTnC.getText().toString().trim();
                            String otnc = etOTnC.getText().toString().trim();
//                            if (otnc == null) {
//                                otnc = " ";
//                            }
                            mProgressDialog.dismiss();
                            Toast.makeText(AddUserDetailCLActivity.this, "Uploaded successfully.",
                                    Toast.LENGTH_SHORT).show();

                            UploadInformation uploadInformation = new UploadInformation(uid,
                                    accountType, firstName, lastName, dateOfBirth, age,
                                    phoneNumber, state, tnc, otnc, downloadUri.toString());

                            mDatabaseReference.child(user.getUid()).setValue(uploadInformation);
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

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
