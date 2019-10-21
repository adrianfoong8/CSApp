package com.example.csapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ViewCLActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cl);

        recyclerView = findViewById(R.id.rv_cl);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseDatabase = FirebaseDatabase.getInstance();

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
        ref = firebaseDatabase.getReference("Users");
        Query query = ref.orderByChild("accountType").equalTo("clt");
        FirebaseRecyclerAdapter<ModelCL, ViewCLHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelCL, ViewCLHolder>(
                ModelCL.class, R.layout.view_cl_holder, ViewCLHolder.class, query
        ) {
            @Override
            protected void populateViewHolder(ViewCLHolder viewHolder, ModelCL model, int position) {
                viewHolder.setDetails(getApplicationContext(), model.getFirstName(), model.getLastName(), model.getImage(), model.getState());
            }

            @Override
            public ViewCLHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewCLHolder viewCLHolder = super.onCreateViewHolder(parent, viewType);
                viewCLHolder.setOnClickListener(new ViewCLHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String firstName = getItem(position).getFirstName();
                        String lastName = getItem(position).getLastName();
                        String image = getItem(position).getImage();
                        String uid = getItem(position).getUid();
                        String age = getItem(position).getAge();
                        String phoneNumber = getItem(position).getPhoneNumber();
                        String state = getItem(position).getState();
                        String tnc = getItem(position).getTnc();
                        String otnc = getItem(position).getOtnc();

                        Intent intent = new Intent(view.getContext(), ViewCLProfileActivity.class);
                        intent.putExtra("firstName", firstName);
                        intent.putExtra("lastName", lastName);
                        intent.putExtra("image", image);
                        intent.putExtra("uid", uid);
                        intent.putExtra("age", age);
                        intent.putExtra("phoneNumber", phoneNumber);
                        intent.putExtra("state", state);
                        intent.putExtra("tnc", tnc);
                        intent.putExtra("otnc", otnc);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                return viewCLHolder;
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
