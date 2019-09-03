package com.example.csapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PackageManagerActivity extends AppCompatActivity {

    private Button btnAddPackage, btnViewPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_manager);

        btnAddPackage = findViewById(R.id.btn_add_package);
        btnAddPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PackageManagerActivity.this, AddPackageActivity.class));
            }
        });
        btnViewPackage = findViewById(R.id.btn_view_package);
        btnViewPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PackageManagerActivity.this, ViewPackageActivity.class));
            }
        });
    }
}
