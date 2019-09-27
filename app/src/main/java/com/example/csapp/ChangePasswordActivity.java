package com.example.csapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private Button btnChangePassword;
    private EditText etNewPassword;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        auth= FirebaseAuth.getInstance();
        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        btnChangePassword=findViewById(R.id.btn_change_password);
        etNewPassword=findViewById(R.id.et_new_password);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null && !etNewPassword.getText().toString().trim().equals("")) {
                    if (etNewPassword.getText().toString().trim().length() < 6) {
                        etNewPassword.setError("Password too short, enter minimum 6 characters");
                    } else {
                        user.updatePassword(etNewPassword.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ChangePasswordActivity.this, "Password is updated, sign in with new oldPassword!", Toast.LENGTH_SHORT).show();
                                            logout();
                                        } else {
                                            Toast.makeText(ChangePasswordActivity.this, "Failed to update old Password!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } else if (etNewPassword.getText().toString().trim().equals("")) {
                    etNewPassword.setError("Enter new password");
                }
            }
        });
    }

    private void logout(){
        auth.signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
