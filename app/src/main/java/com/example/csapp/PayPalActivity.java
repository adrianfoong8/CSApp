package com.example.csapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.csapp.Config.Config;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class PayPalActivity extends AppCompatActivity {

    public static final int PAYPAL_REQUEST_CODE = 7171;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    Button btnPayNow;
    EditText edtAmount;
    String amount = "";
    String sPackageName, sPackagePrice, sPackageDuration, sPackageStartDate;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        btnPayNow = findViewById(R.id.btnPayNow);
        edtAmount = findViewById(R.id.edtAmount);

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });

        String packageName = getIntent().getStringExtra("packageName");
        String packagePrice = getIntent().getStringExtra("packagePrice");
        String packageDuration = getIntent().getStringExtra("packageDuration");
        String packageStartDate = getIntent().getStringExtra("packageStartDate");

        sPackageName = packageName;
        sPackagePrice = packagePrice;
        sPackageDuration = packageDuration;
        sPackageStartDate = packageStartDate;

        edtAmount.setCursorVisible(false);
        edtAmount.setFocusable(false);
        edtAmount.setFocusableInTouchMode(false);
        edtAmount.setText(sPackagePrice);
    }

    private void processPayment() {
        amount = edtAmount.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount), "MYR",
                "Pay now", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetail = confirmation.toJSONObject().toString(4);
                        Toast.makeText(this, "Payment successfully.", Toast.LENGTH_SHORT).show();

                        sendMessage();

                        startActivity(new Intent(this, PaymentDetailActivity.class)
                                .putExtra("PaymentDetail", paymentDetail)
                                .putExtra("PaymentAmount", amount)
                                .putExtra("packageName", sPackageName)
                                .putExtra("packagePrice", sPackagePrice)
                                .putExtra("packageDuration", sPackageDuration)
                                .putExtra("packageStartDate", sPackageStartDate));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }

    private void sendMessage() {
//        final ProgressDialog dialog = new ProgressDialog(PayPalActivity.this);
//        dialog.setTitle("Sending Notify Email");
//        dialog.setMessage("Please wait");
//        dialog.show();
//        Thread sender = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    GMailSender sender = new GMailSender("sf_adrianfoongkeanhwa@qiup.edu.my", "9tOesPKPW!64");
//                    sender.sendMail("Donation to CnD App",
//                            "Donation of MYR " + amount + " has been made. Thank you for your support.",
//                            "sf_adrianfoongkeanhwa@qiup.edu.my",
//                            uEmail);
//                    dialog.dismiss();
//                } catch (Exception e) {
//                    Log.e("SendMail", e.getMessage(), e);
//                }
//            }
//        });
//        sender.start();
    }
}
