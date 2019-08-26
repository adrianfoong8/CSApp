package com.example.csapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class AddPackageActivity extends AppCompatActivity {

    EditText etPackageName, etPackagePrice, etPackageDescription;
    Button btnAddPackage;

    String mDatabasePath = "Users";
    DatabaseReference mDatabaseReference;
    StorageReference mStorageReference;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_package);

        etPackageName = findViewById(R.id.et_package_name);
        etPackagePrice = findViewById(R.id.et_package_price);
        etPackageDescription = findViewById(R.id.et_package_description);
        btnAddPackage = findViewById(R.id.btn_add_package);

        mStorageReference = getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(mDatabasePath);
        mProgressDialog = new ProgressDialog(AddPackageActivity.this);

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
}
