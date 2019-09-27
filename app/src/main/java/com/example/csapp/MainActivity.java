package com.example.csapp;

import android.content.Intent;
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
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //    private ImageButton btnProfile, btnAboutUs, btnAddDetail, btnDonate;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore firebaseFirestore;
    private DatabaseReference ref, ref2;
    private List<ColorSpace.Model> list;
    private RecyclerView recyclerView;
    private TextView tvName, tvApproval;
    private Button btnApply;
    private ImageView ivMainProfile;
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvName = findViewById(R.id.tv_name);
        tvApproval = findViewById(R.id.tv_approval);
//        recyclerView = findViewById(R.id.rv_main);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Users");

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
                                Picasso.get().load(image).into(ivMainProfile);
                                ivMainProfile.setVisibility(View.VISIBLE);
                                tvName.setVisibility(View.VISIBLE);
                                if (dataSnapshot.child("accountType").getValue().toString().equals("cl")
                                        && dataSnapshot.child("verified").getValue().toString().equals("f")) {
                                    tvApproval.setVisibility(View.VISIBLE);
                                    btnApply.setVisibility(View.VISIBLE);
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

        ivMainProfile = findViewById(R.id.iv_main_profile);
        ivMainProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
            }
        });

        tvName = findViewById(R.id.tv_name);
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
            }
        });

        tvApproval = findViewById(R.id.tv_approval);
        tvApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
            }
        });

        btnApply = findViewById(R.id.btn_apply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ApplyActivity.class));
            }
        });

//        btnUserProfile = findViewById(R.id.btn_login);
//        btnUserProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
//            }
//        });
//
//        btnLogout = findViewById(R.id.btn_register);
//        btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivity(new Intent(MainActivity.this, RegisterCLActivity.class));
//                logout();
//            }
//        });
//
//        btnResetPassword = findViewById(R.id.btn_reset_password);
//        btnResetPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivity(new Intent(MainActivity.this, ResetPasswordActivity.class));
//                String ue = user.getEmail();
//                Toast.makeText(MainActivity.this, ue, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        btnLocation = findViewById(R.id.btn_location);
//        btnLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, MapsActivity.class));
//            }
//        });
//
//        btnAboutUs = findViewById(R.id.btn_about_us);
//        btnAboutUs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
//            }
//        });
//
//        btnAddPackage = findViewById(R.id.btn_next);
//        btnAddPackage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, AddUserDetailCLActivity.class));
//            }
//        });
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
                    // user auth state is changed - user is null
                    // launch login activity
                    isLogin = false;
                    ivMainProfile.setVisibility(View.GONE);
                    tvName.setVisibility(View.GONE);
                    tvApproval.setVisibility(View.GONE);
                    btnApply.setVisibility(View.GONE);
                }
            }
        };
    }

//    private void showDeleteDialog(final String currentName, final String currentImage) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle("Delete?");
//        builder.setMessage("Are you sure to delete " + currentName + "?");
//
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Query mQuery = ref.orderByChild("firstName").equalTo(currentName);
//                mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                            ds.getRef().removeValue();
//                        }
//                        Toast.makeText(MainActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//                StorageReference mPictureRef = getInstance().getReferenceFromUrl(currentImage);
//                mPictureRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(MainActivity.this, "Image deleted successfully", Toast.LENGTH_LONG).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//        });
//
//        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.create().show();
//    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);

//        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(
//                Model.class, R.layout.view_holder, ViewHolder.class, ref) {
//            @Override
//            protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
//                viewHolder.setDetails(getApplicationContext(), model.getLastName(), model.getDescription(), model.getDistance(), model.getImage());
//            }
//
//            @Override
//            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
//                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        String lastName = getItem(position).getLastName();
//                        String description = getItem(position).getDescription();
//                        String image = getItem(position).getImage();
//
//                        Intent intent = new Intent(view.getContext(), ViewProfileActivity.class);
//                        intent.putExtra("lastName", lastName);
//                        intent.putExtra("description", description);
//                        intent.putExtra("image", image);
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void onItemLongClick(View view, int position) {
////                        final String name = getItem(position).getLastName();
////                        final String description = getItem(position).getDescription();
////                        final String image = getItem(position).getImage();
////
////                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
////                        String[] options = {"Update", "Delete"};
////                        builder.setItems(options, new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////                                if (which == 0) {
////                                    Intent intent = new Intent(MainActivity.this, AddProfileActivity.class);
////                                    intent.putExtra("firstName", name);
////                                    intent.putExtra("description", description);
////                                    intent.putExtra("image", image);
////                                    startActivity(intent);
////                                }
////                                if (which == 1) {
////                                    showDeleteDialog(name, image);
////                                }
////                            }
////                        });
////                        builder.create().show();
//                    }
//                });
//                return viewHolder;
//            }
//        };
//        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
