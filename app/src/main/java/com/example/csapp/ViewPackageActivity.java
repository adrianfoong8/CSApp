package com.example.csapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewPackageActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_package);

        recyclerView=findViewById(R.id.rv_package);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseDatabase=FirebaseDatabase.getInstance();

        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ref = firebaseDatabase.getReference("Users").child(user.getUid()).child("package");
        FirebaseRecyclerAdapter<ModelPackage, ViewPackageHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelPackage, ViewPackageHolder>(
                ModelPackage.class, R.layout.view_package_holder, ViewPackageHolder.class, ref
        ) {
            @Override
            protected void populateViewHolder(ViewPackageHolder viewHolder, ModelPackage model, int position) {
                viewHolder.setDetails(getApplicationContext(), model.getPackageName(), model.getPackagePrice(), model.getPackageDescription());
            }

            @Override
            public ViewPackageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewPackageHolder viewPackageHolder = super.onCreateViewHolder(parent, viewType);
                viewPackageHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String packageName = getItem(position).getPackageName();
                        String packagePrice = getItem(position).getPackagePrice();
                        String packageDescription = getItem(position).getPackageDescription();

                        Intent intent=new Intent(view.getContext(), ViewPackageDetailActivity.class);
                        intent.putExtra("packageName", packageName);
                        intent.putExtra("packagePrice", packagePrice);
                        intent.putExtra("packageDescription", packageDescription);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                return viewPackageHolder;
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
