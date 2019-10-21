package com.example.csapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore firebaseFirestore;
    private DatabaseReference ref, ref2;
    private List<ColorSpace.Model> list;
    private RecyclerView recyclerView;
    private TextView tvLabel, tvName, tvApproval;
    private ImageView ivCl1, ivCl2, ivM1, ivM2, ivM3;
    private TextView tvCl1, tvCl2, tvCl3, tvCl4, tvCl5, tvCl6, tvM1, tvM2, tvM3, tvM4, tvM5, tvM6, tv1, tv2;
    private Button btnApply, btnViewCL, btnRegister, btnHireCL;
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLabel = findViewById(R.id.tv_label);
        tvName = findViewById(R.id.tv_name);
        tvApproval = findViewById(R.id.tv_approval);
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Users");
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);

        final SliderView sliderView = findViewById(R.id.imageSlider);
        SliderAdapter adapter = new SliderAdapter(this);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setIndicatorMargin(4);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.DKGRAY);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(5);
        sliderView.startAutoCycle();

        ivCl1 = findViewById(R.id.iv_cl_1);
        ivCl2 = findViewById(R.id.iv_cl_2);
        ivM1 = findViewById(R.id.iv_m_1);
        ivM2 = findViewById(R.id.iv_m_2);
        ivM3 = findViewById(R.id.iv_m_3);
        tvCl1 = findViewById(R.id.tv_cl_1);
        tvCl2 = findViewById(R.id.tv_cl_2);
        tvCl3 = findViewById(R.id.tv_cl_3);
        tvCl4 = findViewById(R.id.tv_cl_4);
        tvCl5 = findViewById(R.id.tv_cl_5);
        tvCl6 = findViewById(R.id.tv_cl_6);
        tvM1 = findViewById(R.id.tv_m_1);
        tvM2 = findViewById(R.id.tv_m_2);
        tvM3 = findViewById(R.id.tv_m_3);
        tvM4 = findViewById(R.id.tv_m_4);
        tvM5 = findViewById(R.id.tv_m_5);
        tvM6 = findViewById(R.id.tv_m_6);

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
                                isLogin = true;
                                String name = dataSnapshot.child("firstName").getValue().toString()
                                        + " " + dataSnapshot.child("lastName").getValue().toString();
                                tvName.setText(name);
                                String image = dataSnapshot.child("image").getValue().toString();
                                tvLabel.setVisibility(View.VISIBLE);
                                tvName.setVisibility(View.VISIBLE);
                                if (dataSnapshot.child("accountType").getValue().toString().equals("clf")) {
                                    tvApproval.setVisibility(View.VISIBLE);
                                    btnApply.setVisibility(View.VISIBLE);
                                }
                                if (dataSnapshot.child("accountType").getValue().toString().equals("clt")) {
                                    tvApproval.setVisibility(View.GONE);
                                    btnApply.setVisibility(View.GONE);
                                }
                                if (dataSnapshot.child("accountType").getValue().toString().equals("clt")
                                        || dataSnapshot.child("accountType").getValue().toString().equals("clf")) {
                                    ivCl1.setVisibility(View.VISIBLE);
                                    ivCl2.setVisibility(View.VISIBLE);
                                    tvCl1.setVisibility(View.VISIBLE);
                                    tvCl2.setVisibility(View.VISIBLE);
                                    tvCl3.setVisibility(View.VISIBLE);
                                    tvCl4.setVisibility(View.VISIBLE);
                                    tvCl5.setVisibility(View.VISIBLE);
                                    tvCl6.setVisibility(View.VISIBLE);

                                    ivM1.setVisibility(View.GONE);
                                    ivM2.setVisibility(View.GONE);
                                    ivM3.setVisibility(View.GONE);
                                    tvM1.setVisibility(View.GONE);
                                    tvM2.setVisibility(View.GONE);
                                    tvM3.setVisibility(View.GONE);
                                    tvM4.setVisibility(View.GONE);
                                    tvM5.setVisibility(View.GONE);
                                    tvM6.setVisibility(View.GONE);
                                    btnViewCL.setVisibility(View.GONE);

                                    sliderView.setVisibility(View.GONE);
                                    btnRegister.setVisibility(View.GONE);
                                    btnHireCL.setVisibility(View.GONE);
                                    tv1.setVisibility(View.GONE);
                                    tv2.setVisibility(View.GONE);
                                }
                                if (dataSnapshot.child("accountType").getValue().toString().equals("m")) {
                                    ivCl1.setVisibility(View.GONE);
                                    ivCl2.setVisibility(View.GONE);
                                    tvCl1.setVisibility(View.GONE);
                                    tvCl2.setVisibility(View.GONE);
                                    tvCl3.setVisibility(View.GONE);
                                    tvCl4.setVisibility(View.GONE);
                                    tvCl5.setVisibility(View.GONE);
                                    tvCl6.setVisibility(View.GONE);

                                    ivM1.setVisibility(View.VISIBLE);
                                    ivM2.setVisibility(View.VISIBLE);
                                    ivM3.setVisibility(View.VISIBLE);
                                    tvM1.setVisibility(View.VISIBLE);
                                    tvM2.setVisibility(View.VISIBLE);
                                    tvM3.setVisibility(View.VISIBLE);
                                    tvM4.setVisibility(View.VISIBLE);
                                    tvM5.setVisibility(View.VISIBLE);
                                    tvM6.setVisibility(View.VISIBLE);
                                    btnViewCL.setVisibility(View.VISIBLE);
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

        btnApply = findViewById(R.id.btn_apply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ApplyActivity.class));
            }
        });

        btnViewCL = findViewById(R.id.btn_view_cl);
        btnViewCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ViewCLActivity.class));
            }
        });

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterMenuActivity.class));
            }
        });

        btnHireCL = findViewById(R.id.btn_hire_cl);
        btnHireCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ViewCLActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_action_bar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        if (isLogin) {
            menu.findItem(R.id.menu_account_settings).setVisible(true);
            menu.findItem(R.id.menu_logout).setVisible(true);
            menu.findItem(R.id.menu_login).setVisible(false);
        } else {
            menu.findItem(R.id.menu_account_settings).setVisible(false);
            menu.findItem(R.id.menu_logout).setVisible(false);
            menu.findItem(R.id.menu_login).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                logout();
                return true;
            case R.id.menu_account_settings:
                startActivity(new Intent(MainActivity.this, AccountSettingsActivity.class));
                return true;
            case R.id.menu_login:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout() {
        Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
        auth.signOut();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    isLogin = false;
                    tvLabel.setVisibility(View.GONE);
                    tvName.setVisibility(View.GONE);
                    tvApproval.setVisibility(View.GONE);
                    btnApply.setVisibility(View.GONE);

                    ivCl1.setVisibility(View.GONE);
                    ivCl2.setVisibility(View.GONE);
                    tvCl1.setVisibility(View.GONE);
                    tvCl2.setVisibility(View.GONE);
                    tvCl3.setVisibility(View.GONE);
                    tvCl4.setVisibility(View.GONE);
                    tvCl5.setVisibility(View.GONE);
                    tvCl6.setVisibility(View.GONE);

                    ivM1.setVisibility(View.GONE);
                    ivM2.setVisibility(View.GONE);
                    ivM3.setVisibility(View.GONE);
                    tvM1.setVisibility(View.GONE);
                    tvM2.setVisibility(View.GONE);
                    tvM3.setVisibility(View.GONE);
                    tvM4.setVisibility(View.GONE);
                    tvM5.setVisibility(View.GONE);
                    tvM6.setVisibility(View.GONE);
                    btnViewCL.setVisibility(View.GONE);
                }
            }
        };
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
