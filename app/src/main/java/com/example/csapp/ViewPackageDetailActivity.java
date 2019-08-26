package com.example.csapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewPackageDetailActivity extends AppCompatActivity {

    TextView tvPackageName, tvPackagePrice, tvPackageDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_package_detail);

        tvPackageName=findViewById(R.id.tv_package_name);
        tvPackagePrice=findViewById(R.id.tv_package_price);
        tvPackageDescription=findViewById(R.id.tv_package_description);

        String packageName=getIntent().getStringExtra("packageName");
        String packagePrice=getIntent().getStringExtra("packagePrice");
        String packageDescription=getIntent().getStringExtra("packageDescription");

        tvPackageName.setText(packageName);
        tvPackagePrice.setText(packagePrice);
        tvPackageDescription.setText(packageDescription);
    }
}
