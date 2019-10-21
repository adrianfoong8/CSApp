package com.example.csapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ViewCLProfileActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private RecyclerView recyclerView;
    private ImageView ivImage;
    private TextView tvName, tvState, tvAge, tvPhoneNumber, tvTnc, tvOtnc;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_clprofile);

        ivImage = findViewById(R.id.iv_profile);
        tvName = findViewById(R.id.tv_name);
        tvState = findViewById(R.id.tv_state);
        tvAge = findViewById(R.id.tv_age);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvTnc = findViewById(R.id.tv_tnc);
        tvOtnc = findViewById(R.id.tv_otnc);

        String image = getIntent().getStringExtra("image");
        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        String name = firstName + " " + lastName;
        uid = getIntent().getStringExtra("uid");
        String age = getIntent().getStringExtra("age");
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        String state = getIntent().getStringExtra("state");
        String tnc = getIntent().getStringExtra("tnc");
        String otnc = getIntent().getStringExtra("otnc");

        Picasso.get().load(image).into(ivImage);
        tvName.setText(name);
        tvAge.setText(age);
        tvPhoneNumber.setText(phoneNumber);
        tvState.setText(state);
        tvTnc.setText(tnc);
        tvOtnc.setText(otnc);

        recyclerView = findViewById(R.id.rv_cl_package);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseDatabase = FirebaseDatabase.getInstance();

        ref = firebaseDatabase.getReference("Users").child(uid).child("package");
        FirebaseRecyclerAdapter<ModelCLPackage, ViewCLPackageHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelCLPackage, ViewCLPackageHolder>(
                ModelCLPackage.class, R.layout.view_clpackage_holder, ViewCLPackageHolder.class, ref) {
            @Override
            protected void populateViewHolder(ViewCLPackageHolder viewHolder, ModelCLPackage model, int position) {
                viewHolder.setDetails(getApplicationContext(), model.getPackagePhoto(), model.getPackageName(), model.getPackagePrice());
            }

            @Override
            public ViewCLPackageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewCLPackageHolder viewClPackageHolder = super.onCreateViewHolder(parent, viewType);
                viewClPackageHolder.setOnClickListener(new ViewCLPackageHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String packagePhoto = getItem(position).getPackagePhoto();
                        String packageName = getItem(position).getPackageName();
                        String packagePrice = getItem(position).getPackagePrice();
                        String packageDescription = getItem(position).getPackageDescription();
                        String packageId = getItem(position).getPackageId();
                        String packageUserTestimony = getItem(position).getPackageUserTestimony();
                        String packageDuration = getItem(position).getPackageDuration();
                        String packageService = getItem(position).getPackageService();
                        String packageAvailableStartDate = getItem(position).getPackageAvailableStartDate();
                        String packageAvailableEndDate = getItem(position).getPackageAvailableEndDate();

                        Intent intent = new Intent(view.getContext(), ViewPackageDetailActivity.class);
                        intent.putExtra("packagePhoto", packagePhoto);
                        intent.putExtra("packageName", packageName);
                        intent.putExtra("packagePrice", packagePrice);
                        intent.putExtra("packageDescription", packageDescription);
                        intent.putExtra("packageId", packageId);
                        intent.putExtra("packageUserTestimony", packageUserTestimony);
                        intent.putExtra("packageDuration", packageDuration);
                        intent.putExtra("packageService", packageService);
                        intent.putExtra("packageAvailableStartDate", packageAvailableStartDate);
                        intent.putExtra("packageAvailableEndDate", packageAvailableEndDate);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                return viewClPackageHolder;
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
