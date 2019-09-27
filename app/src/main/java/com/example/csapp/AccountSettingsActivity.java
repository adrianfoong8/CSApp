package com.example.csapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AccountSettingsActivity extends AppCompatActivity {

    private Button btnMyProfile, btnPackageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        btnMyProfile = findViewById(R.id.btn_my_profile);
        btnMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSettingsActivity.this, UserProfileActivity.class));
            }
        });
        btnPackageManager = findViewById(R.id.btn_package_manager);
        btnPackageManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSettingsActivity.this, PackageManagerActivity.class));
            }
        });
    }
}
