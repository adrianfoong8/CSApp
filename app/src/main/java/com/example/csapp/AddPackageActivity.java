package com.example.csapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class AddPackageActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private EditText etPackageName, etPackagePrice, etPackageDescription;
    private Button btnAddPackage, btnAddMedia;
    private String mDatabasePath = "Users";
    private String mStoragePath ="Images/Package Media/";
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;
    private ProgressDialog mProgressDialog;
    private RecyclerView rvAddMedia;
    private List<String> fileNameList, fileDoneList;
    private UploadPackageMedia uploadPackageMedia;
    private Uri mFilePathUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_package);

        etPackageName = findViewById(R.id.et_package_name);
        etPackagePrice = findViewById(R.id.et_package_price);
        etPackageDescription = findViewById(R.id.et_package_description);
        btnAddPackage = findViewById(R.id.btn_add_package);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(mDatabasePath);
        mProgressDialog = new ProgressDialog(AddPackageActivity.this);

        btnAddMedia = findViewById(R.id.btn_add_media);
        rvAddMedia = findViewById(R.id.rv_add_media);
        btnAddMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Media"), RESULT_LOAD_IMAGE);
            }
        });

        fileNameList = new ArrayList<>();
        uploadPackageMedia = new UploadPackageMedia(fileNameList, fileDoneList);
        rvAddMedia.setLayoutManager(new LinearLayoutManager(this));
        rvAddMedia.setHasFixedSize(true);
        rvAddMedia.setAdapter(uploadPackageMedia);

        btnAddPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog.setTitle("Uploading");
                mProgressDialog.show();

                String packageName = etPackageName.getText().toString().trim();
                String packagePrice = etPackagePrice.getText().toString().trim();
                String packageDescription = etPackageDescription.getText().toString().trim();

                UploadPackage uploadPackage = new UploadPackage(packageName, packagePrice, packageDescription);
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                mDatabaseReference.child(user.getUid()).child("package").child(packageName).setValue(uploadPackage);
                mProgressDialog.dismiss();
                Toast.makeText(AddPackageActivity.this, "Uploaded successfully.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int totalItemsSelected = data.getClipData().getItemCount();
                for (int i = 0; i < totalItemsSelected; i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    String fileName = getFileName(fileUri);
                    fileNameList.add(fileName);
//                    fileDoneList.add("uploading");
                    uploadPackageMedia.notifyDataSetChanged();
//                    StorageReference fileToUpload = mStorageReference.child("Image").child(fileName);
//                    final int finalInt = i;
//                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            fileDoneList.remove(finalInt);
//                            fileDoneList.add(finalInt, "done");
//                            uploadPackageMedia.notifyDataSetChanged();
//                        }
//                    });
                }
            } else if (data.getData() != null) {
                Toast.makeText(AddPackageActivity.this, "Selected Single File", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private String getFileExtention(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
