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

public class WeatherIntegration extends AppCompatActivity {
    Button btn_Home;
    private TextView temp_value;
    private TextView humidity_value;
    private TextView weather_status;
    private TextView location;
    private TextView weatherdes;
    private static final String BROKER = "tcp://test.mosquitto.org:1883";
    private static final String TOPIC = "iotproject/asmweather";
    private static final String CLIENT_ID = MqttClient.generateClientId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_integration);

        btn_Home = findViewById(R.id.btn_Home);
        temp_value = (TextView) findViewById(R.id.txv_tempval);
        humidity_value = (TextView) findViewById(R.id.txv_humidityVal);
        weather_status = (TextView) findViewById(R.id.txv_weather_status);
        location = (TextView) findViewById(R.id.txv_city);
        weatherdes = (TextView) findViewById(R.id.txv_description);

        // MQTT connection setup
        connectToMQTTBroker();

        btn_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(WeatherIntegration.this, MainActivity.class)});

            }
        });
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
                    String NewMessage = new String(message.getPayload());
                    System.out.println("Incoming message: " + NewMessage);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Update the UI (e.g., set text on a TextView)
                            updateWeatherTextView(NewMessage);
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

    private void updateWeatherTextView(final String NewMessage) {
        String result = removeBrackets(NewMessage);
        String splitedVal[] = result.split(",");
        String temp = splitedVal[0];
        String humidity = splitedVal[1];
        temp_value.setText(temp +" °C");
        humidity_value.setText(humidity + " %");
        weather_status.setText(splitedVal[2].replace("'", ""));
        location.setText(splitedVal[3].replace("'", ""));
        weatherdes.setText(splitedVal[4].replace("'", ""));
    }

    // Method to remove brackets from an array
    private static String removeBrackets(String result) {
        // Check if the string has brackets
        if (result.startsWith("(") && result.endsWith(")")) {
            // Remove the first and last characters (brackets)
            return result.substring(1, result.length() - 1);
        } else {
            // If the input doesn't have brackets, return as is
            return result;
        }
    }

}

