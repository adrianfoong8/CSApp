package com.example.csapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewPackageDetailActivity extends AppCompatActivity {

    private ImageView ivPackagePhoto;
    private TextView tvPackageName, tvPackagePrice, tvPackageDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_package_detail);

        ivPackagePhoto = findViewById(R.id.iv_package_photo);
        tvPackageName = findViewById(R.id.tv_package_name);
        tvPackagePrice = findViewById(R.id.tv_package_price);
        tvPackageDescription = findViewById(R.id.tv_package_description);

        String packagePhoto = getIntent().getStringExtra("packagePhoto");
        String packageName = getIntent().getStringExtra("packageName");
        String packagePrice = getIntent().getStringExtra("packagePrice");
        String packageDescription = getIntent().getStringExtra("packageDescription");

        Picasso.get().load(packagePhoto).into(ivPackagePhoto);
        tvPackageName.setText(packageName);
        tvPackagePrice.setText(packagePrice);
        tvPackageDescription.setText(packageDescription);
    }
}
