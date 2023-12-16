package com.example.iotautomatescenemanagementprojectht2023;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView timeToDay = findViewById(R.id.cardTimeOfDay);
        CardView occupancy = findViewById(R.id.cardOccupancy);
        CardView weather = findViewById(R.id.cardWeather);
        CardView vacation = findViewById(R.id.cardVacation);
        CardView report = findViewById(R.id.cardReports);
        CardView settings = findViewById(R.id.cardSettings);

        timeToDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(MainActivity.this, TimeToDay.class)});

            }
        });

        occupancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(MainActivity.this, Occupancy.class)});

            }
        });

        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(MainActivity.this, WeatherIntegration.class)});

            }
        });

        vacation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(MainActivity.this, VacationMode.class)});

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(MainActivity.this, Settings.class)});

            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(MainActivity.this, Report.class)});

            }
        });


    }
}