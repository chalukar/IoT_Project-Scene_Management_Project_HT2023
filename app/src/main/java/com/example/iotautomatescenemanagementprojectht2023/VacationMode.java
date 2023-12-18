package com.example.iotautomatescenemanagementprojectht2023;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class VacationMode extends AppCompatActivity {
    private DatePickerDialog fromdatePickerDialog,todatePickerDialog;
    private Button fromdateBtn,todateBtn,homeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_mode);
        fromDatePicker();
        toDatePicker();

        fromdateBtn = findViewById(R.id.vacation_fromdatePickerbtn);
        todateBtn = findViewById(R.id.vacation_todatePickerbtn);
        homeBtn = findViewById(R.id.btn_Home);

        fromdateBtn.setText(getTodayDate());
        todateBtn.setText(getTodayDate());
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(VacationMode.this, MainActivity.class)});

            }
        });

    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);
    }

    private void fromDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = makeDateString(dayOfMonth,month,year);
                fromdateBtn.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        fromdatePickerDialog = new DatePickerDialog(this,style,dateSetListener,year,month,day);
    }
    private void toDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = makeDateString(dayOfMonth,month,year);
                todateBtn.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        todatePickerDialog = new DatePickerDialog(this,style,dateSetListener,year,month,day);
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return getMonthFormat(month)+"-"+ dayOfMonth +"-"+ year;

    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return  "JAN";
        if(month == 2)
            return  "FEB";
        if(month == 3)
            return  "MAR";
        if(month == 4)
            return  "APR";
        if(month == 5)
            return  "MAY";
        if(month == 6)
            return  "JUN";
        if(month == 7)
            return  "JUL";
        if(month == 8)
            return  "AUG";
        if(month == 9)
            return  "SEP";
        if(month == 10)
            return  "OCT";
        if(month == 11)
            return  "NOV";
        if(month == 12)
            return  "DEC";
        return "JAN";
    }

    public void openFromDatePicker(View view) {
        fromdatePickerDialog.show();

    }

    public void openToDatePicker(View view) {
        todatePickerDialog.show();
    }
}