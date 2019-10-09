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
    private ImageView ivPackagePhoto;
    private TextView tvPackageName, tvPackagePrice, tvPackageDescription;
    private Button btnBooking;

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
                startActivity(new Intent(ViewPackageDetailActivity.this, BookingActivity.class));
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
