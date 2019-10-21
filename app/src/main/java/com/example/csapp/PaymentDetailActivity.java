package com.example.csapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetailActivity extends AppCompatActivity {

    TextView txtId, txtAmount, txtStatus, tvPackageName, tvPackagePrice, tvPackageDuration, tvPackageStartDate;
    String sPackageName, sPackagePrice, sPackageDuration, sPackageStartDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);

        txtId = findViewById(R.id.txtId);
        txtAmount = findViewById(R.id.txtAmount);
        txtStatus = findViewById(R.id.txtStatus);
        tvPackageName = findViewById(R.id.tv_package_name);
        tvPackagePrice = findViewById(R.id.tv_package_price);
        tvPackageDuration = findViewById(R.id.tv_package_duration);
        tvPackageStartDate = findViewById(R.id.tv_package_start_date);

        Intent intent = getIntent();

        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetail"));
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String packageName = getIntent().getStringExtra("packageName");
        String packagePrice = getIntent().getStringExtra("packagePrice");
        String packageDuration = getIntent().getStringExtra("packageDuration");
        String packageStartDate = getIntent().getStringExtra("packageStartDate");

        sPackageName = packageName;
        sPackagePrice = packagePrice;
        sPackageDuration = packageDuration;
        sPackageStartDate = packageStartDate;
        tvPackageName.setText(sPackageName);
        tvPackagePrice.setText(sPackagePrice);
        tvPackageDuration.setText(sPackageDuration);
        tvPackageStartDate.setText(sPackageStartDate);
    }

    private void showDetails(JSONObject response, String paymentAmount) {
        try {
            txtId.setText(response.getString("id"));
            txtAmount.setText("RM" + paymentAmount);
            txtStatus.setText(response.getString("state"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
