package com.example.iotautomatescenemanagementprojectht2023;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TimeToDay extends AppCompatActivity {

    Button btn_Home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_to_day);

        btn_Home = findViewById(R.id.btn_Home);
        btn_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(TimeToDay.this, MainActivity.class)});

            }
        });

    }
}