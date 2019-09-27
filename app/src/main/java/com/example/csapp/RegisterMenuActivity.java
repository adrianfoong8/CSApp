package com.example.csapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class RegisterMenuActivity extends AppCompatActivity {

    private Button btnRegisterCL, btnRegisterM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_menu);

        btnRegisterCL = findViewById(R.id.btn_register_cl);
        btnRegisterCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterMenuActivity.this, RegisterCLActivity.class));
            }
        });
        btnRegisterM = findViewById(R.id.btn_register_m);
        btnRegisterM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterMenuActivity.this, RegisterMActivity.class));
            }
        });
    }
}
