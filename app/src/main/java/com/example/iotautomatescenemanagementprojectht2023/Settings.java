package com.example.iotautomatescenemanagementprojectht2023;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Settings extends AppCompatActivity {
    private TextView txt_value;
    private MqttClient mqttClient;
    private String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        txt_value = findViewById(R.id.txt_value);

        // Connect to MQTT broker
//        connectToMqttBroker();


    }

//    private void connectToMqttBroker() {
//        try {
//            mqttClient = new MqttClient("tcp://test.mosquitto.org:1883", MqttClient.generateClientId(), null);
//            MqttConnectOptions options = new MqttConnectOptions();
//            options.setCleanSession(true);
//
//            mqttClient.connect();
//
//            mqttClient.setCallback(new MqttCallback() {
//                @Override
//                public void connectionLost(Throwable cause) {
//                    Log.e("MQTT", "Connection lost: " + cause.getMessage());
//                }
//
//                @Override
//                public void messageArrived(String topic, MqttMessage message) throws Exception {
//                    final String payload = new String(message.getPayload());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            txt_value.setText(payload);
//                        }
//                    });
//                }
//
//                @Override
//                public void deliveryComplete(IMqttDeliveryToken token) {
//                    // Not needed for subscribing
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void subscribeToTopic(String s) {
//        try {
//            mqttClient.subscribe(topic, 0);
//        } catch (Exception e) {
//            Log.e("MQTT", "Failed to subscribe to topic: " + e.getMessage());
//        }
//    }
}