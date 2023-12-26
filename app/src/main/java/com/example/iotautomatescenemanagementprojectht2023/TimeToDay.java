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

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

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
    private MqttAndroidClient client;
    private static final String SERVER_URI = "tcp://test.mosquitto.org:1883";
    private static final String TAG = "TimeToDay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_to_day);

//        try {
//            connect();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("connection", "Exception during connect(): " + e.getMessage());
//        }

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

//        try {
//            client.setCallback(new MqttCallbackExtended() {
//                @Override
//                public void connectComplete(boolean reconnect, String serverURI) {
//                    if (reconnect) {
//                        System.out.println("Reconnected to : " + serverURI);
//                        // Re-subscribe as we lost it due to new session
//                        subscribe("iotproject/asmlight");
//                    } else { System.out.println("Connected to: " + serverURI);
//                        subscribe("iotproject/asmlight");
//                    }
//                }
//                @Override
//                public void connectionLost(Throwable cause) {
//                    System.out.println("The Connection was lost.");
//                }
//                @Override
//                public void messageArrived(String topic, MqttMessage message) throws Exception {
//                    String newMessage = new String(message.getPayload());
//                    Log.d("light", "newMessage");
//                    System.out.println("Incoming message: " + newMessage);
//                    String[] separated = newMessage.split("-");
//
//                    txt_mode.setText(separated[0]);
//
//                }
//                @Override
//                public void deliveryComplete(IMqttDeliveryToken token) {
//
//                }
//            });
//        } catch (Exception e) {
//            Log.e("MqttExample", "An error occurred: " + e.getMessage(), e);
//        }

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

//    private void connect(){
//        String clientId = MqttClient.generateClientId();
//        client = new MqttAndroidClient(this.getApplicationContext(), SERVER_URI, clientId);
//        try {
//            IMqttToken token = client.connect();
//            token.setActionCallback(new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                    // We are connected
//                    Log.d(TAG, "onSuccess");
//                    System.out.println(TAG + " Success. Connected to " + SERVER_URI);
//                }
//                @Override
//                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                    // Something went wrong e.g. connection timeout or firewall problems
//                    Log.d(TAG, "onFailure");
//                    System.out.println(TAG + " Oh no! Failed to connect to " + SERVER_URI);
//                }
//            });
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }
//    private void subscribe(String topicToSubscribe) {
//        final String topic = topicToSubscribe;
//        int qos = 1;
//        try {
//            IMqttToken subToken = client.subscribe(topic, qos);
//            subToken.setActionCallback(new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                    System.out.println("Subscription successful to topic: " + topic);
//                } @Override
//                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                    System.out.println("Failed to subscribe to topic: " + topic);
//                }
//            });
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }
}