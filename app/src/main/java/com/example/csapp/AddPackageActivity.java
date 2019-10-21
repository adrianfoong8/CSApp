package com.example.csapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class AddPackageActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int RESULT_LOAD_IMAGE = 1;
    private EditText etPackageName, etPackagePrice, etPackageDescription, etStartDate, etEndDate, etOtherService;
    private ImageView ivPackagePhoto, ivUserTestimony;
    private String sPackageId, sPackagePhoto, sPackageName, sPackagePrice, sPackageDescription;
    private Button btnAddPackage;
    private String mDatabasePath = "Users";
    private String mStoragePath = "Images/Package Photos/";
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;
    private ProgressDialog mProgressDialog;
    private int IMAGE_REQUEST_CODE = 5;
    private RecyclerView rvAddMedia;
    private List<String> fileNameList, fileDoneList;
    private Uri mFilePathUri, mFilePathUri1;
    private String previousPackageId;
    private CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8;
    private String services;
    private Spinner spDuration;
    private DatePickerDialog.OnDateSetListener mDateSetListener, mDateSetListener1;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_package);

        etPackageName = findViewById(R.id.et_package_name);
        etPackagePrice = findViewById(R.id.et_package_price);
        etPackageDescription = findViewById(R.id.et_package_description);
        btnAddPackage = findViewById(R.id.btn_add_package);
        spDuration = findViewById(R.id.sp_package_duration);
        etStartDate = findViewById(R.id.et_available_start_date);
        etEndDate = findViewById(R.id.et_available_end_date);
        etOtherService = findViewById(R.id.et_other_service);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(mDatabasePath);
        mProgressDialog = new ProgressDialog(AddPackageActivity.this);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String imageUploadId = user.getUid();
        String newStoragePath = mStoragePath + imageUploadId + "/";
        mStoragePath = newStoragePath;
        list = new ArrayList<>();

        cb1 = findViewById(R.id.cb1);
        cb1.setOnClickListener(this);
        cb2 = findViewById(R.id.cb2);
        cb2.setOnClickListener(this);
        cb3 = findViewById(R.id.cb3);
        cb3.setOnClickListener(this);
        cb4 = findViewById(R.id.cb4);
        cb4.setOnClickListener(this);
        cb5 = findViewById(R.id.cb5);
        cb5.setOnClickListener(this);
        cb6 = findViewById(R.id.cb6);
        cb6.setOnClickListener(this);
        cb7 = findViewById(R.id.cb7);
        cb7.setOnClickListener(this);
        cb8 = findViewById(R.id.cb8);
        cb8.setOnClickListener(this);

        ivPackagePhoto = findViewById(R.id.iv_package_photo);
        ivPackagePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"),
                        IMAGE_REQUEST_CODE);
            }
        });
        ivUserTestimony = findViewById(R.id.iv_user_testimony);
        ivUserTestimony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"),
                        6);
            }
        });
        btnAddPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnAddPackage.getText().equals("Next")) {
                    uploadDataToFirebase();
                } else {
                    beginUpdate();
                }
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.package_duration, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDuration.setAdapter(adapter);
        spDuration.setOnItemSelectedListener(this);

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddPackageActivity.this, mDateSetListener, year, month, day);
                dialog.show();
            }
        });
        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddPackageActivity.this, mDateSetListener1, year, month, day);
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                etStartDate.setText(date);
            }
        };
        mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                etEndDate.setText(date);
            }
        };

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            sPackageId = intent.getString("packageId");
            sPackagePhoto = intent.getString("packagePhoto");
            sPackageName = intent.getString("packageName");
            sPackagePrice = intent.getString("packagePrice");
            sPackageDescription = intent.getString("packageDescription");

            previousPackageId = sPackageId;
            Picasso.get().load(sPackagePhoto).into(ivPackagePhoto);
            etPackageName.setText(sPackageName);
            etPackagePrice.setText(sPackagePrice);
            etPackageDescription.setText(sPackageDescription);

            btnAddPackage.setText("Update");
        }
    }

    private void beginUpdate() {
        mProgressDialog.setTitle("Updating");
        mProgressDialog.show();
        deletePreviousImage();
    }

    private void deletePreviousImage() {
        StorageReference mPictureRef = getInstance().getReferenceFromUrl(sPackagePhoto);
        mPictureRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddPackageActivity.this, "Previous image deleted.", Toast.LENGTH_SHORT).show();
                uploadNewImage();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPackageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
    }

    private void uploadNewImage() {
        String imageName = System.currentTimeMillis() + ".png";
        StorageReference storageReference2 = mStorageReference.child(mStoragePath + imageName);
        Bitmap bitmap = ((BitmapDrawable) ivPackagePhoto.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storageReference2.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddPackageActivity.this, "New image uploaded.", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                Uri downloadUri = uriTask.getResult();
                updateDatabase(downloadUri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPackageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
    }

    private void updateDatabase(final String image) {
        final String packageName = etPackageName.getText().toString();
        final String packagePrice = etPackagePrice.getText().toString();
        final String packageDescription = etPackageDescription.getText().toString();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mRef = mFirebaseDatabase.getReference("Users/" + user.getUid() + "/package");

        Query query = mRef.orderByChild("packageId").equalTo(previousPackageId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ds.getRef().child("packagePhoto").setValue(image);
                    ds.getRef().child("packageName").setValue(packageName);
                    ds.getRef().child("packagePrice").setValue(packagePrice);
                    ds.getRef().child("packageDescription").setValue(packageDescription);
                }
                mProgressDialog.dismiss();
                Toast.makeText(AddPackageActivity.this, "Data updated.", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                            final Uri downloadUri = uriTask.getResult();

                            if (mFilePathUri1 != null) {
                                mProgressDialog.setTitle("Uploading");
                                mProgressDialog.show();
                                StorageReference storageReference2 = mStorageReference.child(mStoragePath +
                                        System.currentTimeMillis() + "." + getFileExtention(mFilePathUri1));
                                storageReference2.putFile(mFilePathUri1)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                                while (!uriTask.isSuccessful()) ;
                                                Uri downloadUri1 = uriTask.getResult();

                                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                String packageId = user.getUid() + System.currentTimeMillis();
                                                String packageName = etPackageName.getText().toString().trim();
                                                String packagePrice = etPackagePrice.getText().toString().trim();
                                                String packageDescription = etPackageDescription.getText().toString().trim();
                                                String packageDuration = spDuration.getSelectedItem().toString().trim();
                                                String packageOtherService = etOtherService.getText().toString().trim();
                                                if (cb1.isChecked()) {
                                                    list.add(cb1.getText().toString());
                                                }
                                                if (cb2.isChecked()) {
                                                    list.add(cb2.getText().toString());
                                                }
                                                if (cb3.isChecked()) {
                                                    list.add(cb3.getText().toString());
                                                }
                                                if (cb4.isChecked()) {
                                                    list.add(cb4.getText().toString());
                                                }
                                                if (cb5.isChecked()) {
                                                    list.add(cb5.getText().toString());
                                                }
                                                if (cb6.isChecked()) {
                                                    list.add(cb6.getText().toString());
                                                }
                                                if (cb7.isChecked()) {
                                                    list.add(cb7.getText().toString());
                                                }
                                                if (cb8.isChecked()) {
                                                    list.add(cb8.getText().toString());
                                                }
                                                if (TextUtils.isEmpty(packageOtherService)) {

                                                } else {
                                                    list.add(packageOtherService);
                                                }
                                                for (String str : list) {
                                                    services = list.toString().trim();
                                                }
                                                String packageService = services.trim();
                                                String packageAvailableStartDate = etStartDate.getText().toString().trim();
                                                String packageAvailableEndDate = etEndDate.getText().toString().trim();
                                                UploadPackage uploadPackage = new UploadPackage(packageId, downloadUri.toString(), downloadUri1.toString(), packageName, packagePrice, packageDescription,
                                                        packageDuration, packageService, packageAvailableStartDate, packageAvailableEndDate);
                                                mDatabaseReference.child(user.getUid()).child("package").child(packageId).setValue(uploadPackage);
                                                mProgressDialog.dismiss();
                                                Toast.makeText(AddPackageActivity.this, "Uploaded successfully.",
                                                        Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                mProgressDialog.dismiss();
                                                Toast.makeText(AddPackageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                mProgressDialog.setTitle("Uploading...");
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressDialog.dismiss();
                            Toast.makeText(AddPackageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                ivPackagePhoto.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 6 && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {
            mFilePathUri1 = data.getData();
            try {
                Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(),
                        mFilePathUri1);
                ivUserTestimony.setImageBitmap(bitmap1);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
