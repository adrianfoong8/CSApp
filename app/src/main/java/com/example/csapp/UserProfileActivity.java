package com.example.csapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    DatabaseReference ref;
    TextView tvName, tvDateOfBirth, tvAge, tvPhoneNumber, tvState, tvLocation;
    ImageView ivProfile;
    Button btnGetCurrentLocation, btnEditProfile, btnChangePassword;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        tvName = findViewById(R.id.tv_name);
        tvDateOfBirth = findViewById(R.id.tv_date_of_birth);
        tvAge = findViewById(R.id.tv_age);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvState = findViewById(R.id.tv_state);
        tvLocation = findViewById(R.id.tv_location);
        ivProfile = findViewById(R.id.iv_profile);

        auth = FirebaseAuth.getInstance();

        btnGetCurrentLocation = findViewById(R.id.btnGetCurrentLocation);
        btnGetCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this, MapsActivity.class));
            }
        });

        btnChangePassword = findViewById(R.id.btn_change_password);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfileActivity.this, ChangePasswordActivity.class));
            }
        });

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstName = dataSnapshot.child("firstName").getValue().toString();
                String lastName = dataSnapshot.child("lastName").getValue().toString();
                String name = firstName + " " + lastName;
                String dateOfBirth = dataSnapshot.child("dateOfBirth").getValue().toString();
                String age = dataSnapshot.child("age").getValue().toString();
                String phoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
                String state = dataSnapshot.child("state").getValue().toString();
                String latitude;
                if (dataSnapshot.child("location").child("latitude").getValue() != null) {
                    latitude = dataSnapshot.child("location").child("latitude").getValue().toString();
                } else {
                    latitude = "?";
                }
                String longitude;
                if (dataSnapshot.child("location").child("longitude").getValue() != null) {
                    longitude = dataSnapshot.child("location").child("longitude").getValue().toString();
                } else {
                    longitude = "?";
                }
                String location = latitude + ", " + longitude;
                String image = dataSnapshot.child("image").getValue().toString();

                tvName.setText(name);
                tvDateOfBirth.setText(dateOfBirth);
                tvAge.setText(age);
                tvPhoneNumber.setText(phoneNumber);
                tvState.setText(state);
                tvLocation.setText(location);
                Picasso.get().load(image).into(ivProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
