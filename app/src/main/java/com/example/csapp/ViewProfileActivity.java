package com.example.csapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewProfileActivity extends AppCompatActivity {

    TextView tvName, tvDescription;
    ImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        tvName = findViewById(R.id.tv_name);
        tvDescription = findViewById(R.id.tv_description);
        ivProfile = findViewById(R.id.iv_profile);

        String name = getIntent().getStringExtra("firstName");
        String description = getIntent().getStringExtra("description");
        String image = getIntent().getStringExtra("image");

        tvName.setText(name);
        tvDescription.setText(description);
        Picasso.get().load(image).into(ivProfile);
    }
}
