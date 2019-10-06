package com.example.csapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

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

        recyclerView = findViewById(R.id.rv_package);
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
        ref = firebaseDatabase.getReference("Users").child(user.getUid()).child("package");
        FirebaseRecyclerAdapter<ModelPackage, ViewPackageHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelPackage, ViewPackageHolder>(
                ModelPackage.class, R.layout.view_package_holder, ViewPackageHolder.class, ref
        ) {
            @Override
            protected void populateViewHolder(ViewPackageHolder viewHolder, ModelPackage model, int position) {
                viewHolder.setDetails(getApplicationContext(), model.getPackagePhoto(), model.getPackageName(), model.getPackagePrice());
            }

            @Override
            public ViewPackageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewPackageHolder viewPackageHolder = super.onCreateViewHolder(parent, viewType);
                viewPackageHolder.setOnClickListener(new ViewPackageHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String packagePhoto = getItem(position).getPackagePhoto();
                        String packageName = getItem(position).getPackageName();
                        String packagePrice = getItem(position).getPackagePrice();
                        String packageDescription = getItem(position).getPackageDescription();

                        Intent intent = new Intent(view.getContext(), ViewPackageDetailActivity.class);
                        intent.putExtra("packagePhoto", packagePhoto);
                        intent.putExtra("packageName", packageName);
                        intent.putExtra("packagePrice", packagePrice);
                        intent.putExtra("packageDescription", packageDescription);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        final String packageId = getItem(position).getPackageId();
                        final String packagePhoto = getItem(position).getPackagePhoto();
                        final String packageName = getItem(position).getPackageName();
                        final String packagePrice = getItem(position).getPackagePrice();
                        final String packageDescription = getItem(position).getPackageDescription();

                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewPackageActivity.this);
                        String[] options = {"Edit", "Delete"};
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent intent = new Intent(ViewPackageActivity.this, AddPackageActivity.class);
                                    intent.putExtra("packageId", packageId);
                                    intent.putExtra("packagePhoto", packagePhoto);
                                    intent.putExtra("packageName", packageName);
                                    intent.putExtra("packagePrice", packagePrice);
                                    intent.putExtra("packageDescription", packageDescription);
                                    startActivity(intent);
                                }
                                if (which == 1) {
                                    showDeleteDialog(packageName, packagePhoto);
                                }
                            }
                        });
                        builder.create().show();
                    }
                });
                return viewPackageHolder;
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void showDeleteDialog(final String packageName, final String packagePhoto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewPackageActivity.this);
        builder.setTitle("Delete?");
        builder.setMessage("Are you sure to delete " + packageName + "?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Query mQuery = ref.orderByChild("packageName").equalTo(packageName);
                mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(ViewPackageActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ViewPackageActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                StorageReference mPictureRef = getInstance().getReferenceFromUrl(packagePhoto);
                mPictureRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ViewPackageActivity.this, "Image deleted successfully", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewPackageActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
