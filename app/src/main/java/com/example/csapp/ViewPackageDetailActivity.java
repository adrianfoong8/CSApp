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

public class ViewPackageDetailActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private DatabaseReference ref, ref2;
    private ImageView ivPackagePhoto, ivUserTestimony;
    private TextView tvPackageName, tvPackagePrice, tvPackageDescription, tvPackageDuration, tvPackageService, tvPackageStartDate, tvPackageEndDate;
    private Button btnBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_package_detail);

        ivPackagePhoto = findViewById(R.id.iv_package_photo);
        ivUserTestimony = findViewById(R.id.iv_user_testimony);
        tvPackageName = findViewById(R.id.tv_package_name);
        tvPackagePrice = findViewById(R.id.tv_package_price);
        tvPackageDescription = findViewById(R.id.tv_package_description);
        tvPackageDuration = findViewById(R.id.tv_package_duration);
        tvPackageService = findViewById(R.id.tv_package_service);
        tvPackageStartDate = findViewById(R.id.tv_package_start_date);
        tvPackageEndDate = findViewById(R.id.tv_package_end_date);

        String packagePhoto = getIntent().getStringExtra("packagePhoto");
        String packageUserTestimony = getIntent().getStringExtra("packageUserTestimony");
        final String packageName = getIntent().getStringExtra("packageName");
        final String packagePrice = getIntent().getStringExtra("packagePrice");
        String packageDescription = getIntent().getStringExtra("packageDescription");
        final String packageDuration = getIntent().getStringExtra("packageDuration");
        String packageService = getIntent().getStringExtra("packageService");
        String packageAvailableStartDate = getIntent().getStringExtra("packageAvailableStartDate");
        String packageAvailableEndDate = getIntent().getStringExtra("packageAvailableEndDate");

        Picasso.get().load(packagePhoto).into(ivPackagePhoto);
        Picasso.get().load(packageUserTestimony).into(ivUserTestimony);
        tvPackageName.setText(packageName);
        tvPackagePrice.setText(packagePrice);
        tvPackageDescription.setText(packageDescription);
        tvPackageDuration.setText(packageDuration);
        tvPackageService.setText(packageService);
        tvPackageStartDate.setText(packageAvailableStartDate);
        tvPackageEndDate.setText(packageAvailableEndDate);

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
                                    btnBooking.setVisibility(View.GONE);
                                }
                                if (dataSnapshot.child("accountType").getValue().toString().equals("m")) {
                                    btnBooking.setVisibility(View.VISIBLE);
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

        btnBooking = findViewById(R.id.btn_booking);
        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), BookingActivity.class);
                intent.putExtra("packageName", packageName);
                intent.putExtra("packagePrice", packagePrice);
                intent.putExtra("packageDuration",packageDuration);
                startActivity(intent);
                finish();
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
