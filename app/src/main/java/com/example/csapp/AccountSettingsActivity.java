package com.example.csapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class AccountSettingsActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private DatabaseReference ref, ref2;
    private Button btnMyProfile, btnPackageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

//        //get firebase auth instance
//        auth = FirebaseAuth.getInstance();
//
//        //get current user
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        authListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // user auth state is changed - user is null
//                    // launch login activity
//                    ref2 = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
//                    ref2.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (dataSnapshot != null && dataSnapshot.getChildren() != null &&
//                                    dataSnapshot.getChildren().iterator().hasNext()) {
//                                if (dataSnapshot.child("accountType").getValue().toString().equals("clt")
//                                        || dataSnapshot.child("accountType").getValue().toString().equals("clf")) {
//                                    btnPackageManager.setVisibility(View.VISIBLE);
//                                }
//                                if (dataSnapshot.child("accountType").getValue().toString().equals("m")) {
//                                    btnPackageManager.setVisibility(View.GONE);
//                                }
//                            } else {
////                                startActivity(new Intent(MainActivity.this, AddUserDetailCLActivity.class));
////                                finish();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                } else {
//                }
//            }
//        };

        btnMyProfile = findViewById(R.id.btn_my_profile);
        btnMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSettingsActivity.this, UserProfileActivity.class));
            }
        });
//        btnPackageManager = findViewById(R.id.btn_package_manager);
//        btnPackageManager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(AccountSettingsActivity.this, PackageManagerActivity.class));
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
//        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (authListener != null) {
//            auth.removeAuthStateListener(authListener);
//        }
    }
}
