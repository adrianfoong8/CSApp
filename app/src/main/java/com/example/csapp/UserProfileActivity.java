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

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private DatabaseReference ref, ref2;
    private TextView tvName, tvDateOfBirth, tvAge, tvPhoneNumber, tvState, tvTnC, tvOTnC, tvTnC1, tvOTnC1;
    private ImageView ivProfile;
    private Button btnGetCurrentLocation, btnEditProfile, btnManagePackage, btnChangePassword;
//    private Intent editIntent = new Intent(UserProfileActivity.this, EditProfileActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    ref2 = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                    ref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null && dataSnapshot.getChildren() != null &&
                                    dataSnapshot.getChildren().iterator().hasNext()) {
                                if (dataSnapshot.child("accountType").getValue().toString().equals("clt")
                                        || dataSnapshot.child("accountType").getValue().toString().equals("clf")) {
                                    btnManagePackage.setVisibility(View.VISIBLE);
                                }
                                if (dataSnapshot.child("accountType").getValue().toString().equals("m")) {
                                    btnManagePackage.setVisibility(View.GONE);
                                    tvTnC.setVisibility(View.GONE);
                                    tvOTnC.setVisibility(View.GONE);
                                    tvTnC1.setVisibility(View.GONE);
                                    tvOTnC1.setVisibility(View.GONE);
                                }
                            } else {
//                                startActivity(new Intent(MainActivity.this, AddUserDetailCLActivity.class));
//                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                }
            }
        };

        tvName = findViewById(R.id.tv_name);
        tvDateOfBirth = findViewById(R.id.tv_date_of_birth);
        tvAge = findViewById(R.id.tv_age);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvState = findViewById(R.id.tv_state);
//        tvLocation = findViewById(R.id.tv_location);
        ivProfile = findViewById(R.id.iv_profile);
        tvTnC = findViewById(R.id.tv_tnc);
        tvOTnC = findViewById(R.id.tv_otnc);
        tvTnC1 = findViewById(R.id.tv_tnc1);
        tvOTnC1 = findViewById(R.id.tv_otnc1);

        auth = FirebaseAuth.getInstance();

//        btnGetCurrentLocation = findViewById(R.id.btnGetCurrentLocation);
//        btnGetCurrentLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(UserProfileActivity.this, MapsActivity.class));
//            }
//        });

        btnManagePackage = findViewById(R.id.btn_manage_package);
        btnManagePackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfileActivity.this, PackageManagerActivity.class));
            }
        });


        btnChangePassword = findViewById(R.id.btn_change_password);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfileActivity.this, ChangePasswordActivity.class));
            }
        });

//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String firstName = dataSnapshot.child("firstName").getValue().toString();
                final String lastName = dataSnapshot.child("lastName").getValue().toString();
                final String name = firstName + " " + lastName;
                final String dateOfBirth = dataSnapshot.child("dateOfBirth").getValue().toString();
                final String age = dataSnapshot.child("age").getValue().toString();
                final String phoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
                final String state = dataSnapshot.child("state").getValue().toString();
//                final String latitude;
//                if (dataSnapshot.child("location").child("latitude").getValue() != null) {
//                    latitude = dataSnapshot.child("location").child("latitude").getValue().toString();
//                } else {
//                    latitude = "?";
//                }
//                final String longitude;
//                if (dataSnapshot.child("location").child("longitude").getValue() != null) {
//                    longitude = dataSnapshot.child("location").child("longitude").getValue().toString();
//                } else {
//                    longitude = "?";
//                }
//                final String location = latitude + ", " + longitude;
                final String tnc = dataSnapshot.child("tnc").getValue().toString();
                final String otnc = dataSnapshot.child("otnc").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();

                tvName.setText(name);
                tvDateOfBirth.setText(dateOfBirth);
                tvAge.setText(age);
                tvPhoneNumber.setText(phoneNumber);
                tvState.setText(state);
//                tvLocation.setText(location);
                tvTnC.setText(tnc);
                tvOTnC.setText(otnc);
                Picasso.get().load(image).into(ivProfile);

                btnEditProfile = findViewById(R.id.btn_edit_profile);
                btnEditProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent editIntent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
                        editIntent.putExtra("sLastName", lastName);
                        editIntent.putExtra("sFirstName", firstName);
                        editIntent.putExtra("sDateOfBirth", dateOfBirth);
                        editIntent.putExtra("sAge", age);
                        editIntent.putExtra("sPhoneNumber", phoneNumber);
                        editIntent.putExtra("sState", state);
                        editIntent.putExtra("sTnc", tnc);
                        editIntent.putExtra("sOtnc", otnc);
                        editIntent.putExtra("sImage", image);
                        startActivity(editIntent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
