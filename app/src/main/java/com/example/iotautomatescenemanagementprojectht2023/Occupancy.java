package com.example.iotautomatescenemanagementprojectht2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Occupancy extends AppCompatActivity {
    Button btn_Home;
    TextView txv_Proximity;
    TextView txv_Pro_Status;
    private static final String BROKER = "tcp://test.mosquitto.org:1883";
    private static final String TOPIC_PUB = "iotproject/asmoccupancy";
    private static final String CLIENT_ID = MqttClient.generateClientId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occupancy);

        btn_Home = findViewById(R.id.btn_Home);
        txv_Proximity = findViewById(R.id.txv_proximity);
        txv_Pro_Status = findViewById(R.id.txv_proximity_status);
        btn_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(Occupancy.this, MainActivity.class)});

            }
        });
        connectToMQTTBroker();
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
                            updateOccupancyTextView(payload);
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

    private void updateOccupancyTextView(String text) {
        String result = removeBrackets(text);
        String splitval[] = result.split(",");
        String proximity = splitval[0];
        String prox_status = splitval[1];
        String repl_prox_status = prox_status.replace("'", ""); //prox_status After Removing Single quotes
        txv_Proximity.setText(proximity);
        txv_Pro_Status.setText(repl_prox_status);
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