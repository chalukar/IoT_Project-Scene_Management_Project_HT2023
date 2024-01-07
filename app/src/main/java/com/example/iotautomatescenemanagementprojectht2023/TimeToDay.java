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
    private TextView txt_out_status;
    private Handler handler = new Handler();
    private Runnable updateTimeRunnable;

    private static final String BROKER = "tcp://test.mosquitto.org:1883";
    private static final String TOPIC_PUB = "iotproject/asmnaturallightlux";
    private static final String CLIENT_ID = MqttClient.generateClientId();
    private static final String TAG = "TimeToDay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_to_day);

        btn_Home = findViewById(R.id.btn_Home);
        txt_current_date = findViewById(R.id.txv_current_date);
        txt_mode = findViewById(R.id.txv_mode);
        txt_status = findViewById(R.id.txv_status);
        txt_out_status = findViewById(R.id.txv_out_status);


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
                public void messageArrived(String pub_topic, MqttMessage message) throws Exception {
                    // Update the TextView with the received message
                    String newMessage = new String(message.getPayload());
                    System.out.println("Incoming message: " + newMessage);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Update the UI (e.g., set text on a TextView)
                            updateTimeOfDayTextView(newMessage);
                        }
                    });
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Delivery complete
                }
            });

            // Subscribe to the topic
            mqttClient.subscribe(TOPIC_PUB, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateTimeOfDayTextView(final String newMessage) {
        String result = removeBrackets(newMessage);
        String sep_val[] = result.split(",");
        String light_status = sep_val[0];
        String timeofday_status = sep_val[1];
        String outsite_natural_status = sep_val[2];
        String repl_light_status = light_status.replace("'", "");
        String repl_timeofday_status = timeofday_status.replace("'", "");
        String repl_outsite_natural_status = outsite_natural_status.replace("'", "");
        txt_mode.setText(repl_light_status);
        txt_status.setText(repl_timeofday_status);
        txt_out_status.setText(repl_outsite_natural_status);

    }
    private static String removeBrackets(String input) {
        // Check if the string has brackets
        if (input.startsWith("[") && input.endsWith("]")) {
            // Remove the first and last characters (brackets)
            return input.substring(1, input.length() - 1);
        } else {
            // If the input doesn't have brackets, return as is
            return input;
        }
    }

}