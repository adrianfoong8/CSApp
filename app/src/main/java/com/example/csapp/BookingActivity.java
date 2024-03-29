package com.example.csapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class BookingActivity extends AppCompatActivity {

    private EditText etStartDate, etEndDate;
    private Button btnNext;
    private String startDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener, mDateSetListener1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        etStartDate = findViewById(R.id.et_start_date);
//        etEndDate = findViewById(R.id.et_end_date);
        btnNext = findViewById(R.id.btn_next);

        final String packageName = getIntent().getStringExtra("packageName");
        final String packagePrice = getIntent().getStringExtra("packagePrice");
        final String packageDuration = getIntent().getStringExtra("packageDuration");

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(BookingActivity.this, mDateSetListener, year, month, day);
                dialog.show();
            }
        });
//        etEndDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Calendar calendar = Calendar.getInstance();
//                SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/YYYY");
//                String cStartDate= sdf.format(startDate);
//                String dStartDate = sdf.parse(cStartDate);
//            }
//        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                etStartDate.setText(date);
                startDate = date;
            }
        };
//        mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                month = month + 1;
//                String date = day + "/" + month + "/" + year;
//                etEndDate.setText(date);
//            }
//        };
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PayPalActivity.class);
                intent.putExtra("packageName", packageName);
                intent.putExtra("packagePrice", packagePrice);
                intent.putExtra("packageDuration", packageDuration);
                intent.putExtra("packageStartDate", startDate);
                startActivity(intent);
                finish();
            }
        });
    }
}
