package com.example.csapp;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class Apply2Activity extends AppCompatActivity {

    ImageView ivIdentityCard;
    Button btnNext;

    String mStoragePath = "Images/Applications/";
    String mDatabasePath = "Users";
    Uri mFilePathUri;
    DatabaseReference mDatabaseReference;
    ProgressDialog mProgressDialog;
    int IMAGE_REQUEST_CODE = 5;
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply2);

        ivIdentityCard = findViewById(R.id.iv_identity_card);
        btnNext = findViewById(R.id.btn_next);

        ivIdentityCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Identity Card"),
                        IMAGE_REQUEST_CODE);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDataToFirebase();
            }
        });

        mStorageReference = getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(mDatabasePath);
        mProgressDialog = new ProgressDialog(Apply2Activity.this);
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
                    "identity_card" + "." + getFileExtention(mFilePathUri));
            storageReference2.putFile(mFilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            startActivity(new Intent(Apply2Activity.this, MainActivity.class));
                            Toast.makeText(Apply2Activity.this, "Application Sent", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressDialog.dismiss();
                            Toast.makeText(Apply2Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                ivIdentityCard.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
