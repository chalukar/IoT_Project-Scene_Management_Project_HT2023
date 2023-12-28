package com.example.iotautomatescenemanagementprojectht2023;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TimeToDay extends AppCompatActivity {

    Button btn_Home;
    private TextView txt_current_date;
    private TextView txt_mode;
    private TextView txt_status;
    private TextView txv_proximity;
    private Handler handler = new Handler();
    private Runnable updateTimeRunnable;

    private static final String BROKER = "tcp://test.mosquitto.org:1883";
    private static final String TOPIC = "iotproject/asmlight";
    private static final String CLIENT_ID = MqttClient.generateClientId();
    private static final String TAG = "TimeToDay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_to_day);

        btn_Home = findViewById(R.id.btn_Home);
        txt_current_date = findViewById(R.id.txt_current_date);
        txt_mode = findViewById(R.id.txt_mode);
        txt_status = findViewById(R.id.txt_status);


        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                updateCurrentTime();
                handler.postDelayed(this,1000); // Update every 1 second
            }
        };
        handler.post(updateTimeRunnable);  // Start the initial runnable task by posting through the handler

        btn_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(TimeToDay.this, MainActivity.class)});

            }
        });
        connectToMQTTBroker();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTimeRunnable);
    }
    // Time
    private void updateCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        txt_current_date.setText(currentTime);
    }
    private void connectToMQTTBroker() {
        try {
            MqttClient mqttClient = new MqttClient(BROKER, CLIENT_ID, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            mqttClient.connect(options);

            // Set the callback for message handling
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    // Handle connection lost
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // Update the TextView with the received message
                    String payload = new String(message.getPayload());
                    System.out.println("Incoming message: " + payload);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Update the UI (e.g., set text on a TextView)
                            updateTextView(payload);
                        }
                    });
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Delivery complete
                }
            });

            // Subscribe to the topic
            mqttClient.subscribe(TOPIC, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateTextView(final String text) {
        txt_mode.setText(text.split("-")[0]);
        txt_status.setText(text.split("-")[1]);

    }

}