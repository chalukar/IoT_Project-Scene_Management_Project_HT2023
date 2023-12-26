package com.example.iotautomatescenemanagementprojectht2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class WeatherIntegration extends AppCompatActivity {
    Button btn_Home;
    private TextView txt_temp;
    private TextView txt_humidity;
    private TextView txt_weather_status;
    private TextView txv_proximity;
    private MqttAndroidClient client;
    private static final String SERVER_URI = "tcp://test.mosquitto.org:1883";
    private static final String TAG = "WeatherIntegration";
    private static String[] pub_topics = {"iotproject/asmsensors"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_integration);

        btn_Home = findViewById(R.id.btn_Home);
        txt_temp = findViewById(R.id.txt_temp);
        txt_humidity = findViewById(R.id.txt_humidity);
        txt_weather_status = findViewById(R.id.txt_weather_status);

        connect(this.getApplicationContext());



//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                connect();
//            }
//        }).start();

//        client.setCallback(new MqttCallbackExtended() {
//            @Override
//            public void connectComplete(boolean reconnect, String serverURI) {
//                if (reconnect) {
//                    System.out.println("Reconnected to : " + serverURI);
//                    // Re-subscribe as we lost it due to new session
//                    subscribe("iotproject/asmsensors");
//                } else { System.out.println("Connected to: " + serverURI);
//                    subscribe("iotproject/asmsensors");
//                }
//            }
//            @Override
//            public void connectionLost(Throwable cause) {
//                System.out.println("The Connection was lost.");
//            }
//            @Override
//            public void messageArrived(String topic, MqttMessage message) throws Exception {
//                String newMessage = new String(message.getPayload());
//                System.out.println("Incoming message: " + newMessage);
//                String[] separated = newMessage.split(".");
//                txt_temp.setText(separated[0]);
//                txt_humidity.setText(separated[1]);
//            }
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken token) {
//
//            }
//        });

//        btn_Home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivities(new Intent[]{new Intent(WeatherIntegration.this, MainActivity.class)});
//            }
//        });
    }

    private void connect(Context context) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                String clientId = MqttClient.generateClientId();
                client =
                        new MqttAndroidClient(context, SERVER_URI,
                                clientId);
                try {
                    IMqttToken token = client.connect();
                    token.setActionCallback(new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {

                            // We are connected
                            Log.d(TAG, "onSuccess");
                            System.out.println(TAG + " Success. Connected to " + SERVER_URI);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            // Something went wrong e.g. connection timeout or firewall problems
                            Log.d(TAG, "onFailure");
                            System.out.println(TAG + " Oh no! Failed to connect to " +
                                    SERVER_URI);
                        }
                    });
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                client.setCallback(new MqttCallbackExtended() {
                    @Override
                    public void connectComplete(boolean reconnect, String serverURI) {
                        if (reconnect) {
                            System.out.println("Reconnected to : " + serverURI);
                            // Re-subscribe as we lost it due to new session
                            subscribe(pub_topics);
                        } else {
                            System.out.println("Connected to: " + serverURI);
                            subscribe(pub_topics);
                        }
                    }

                    @Override
                    public void connectionLost(Throwable cause) {
                        System.out.println("The Connection was lost.");
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws
                            Exception {

                        String newMessage = new String(message.getPayload());
                        System.out.println("Incoming message: " + newMessage);
                        String[] separated = newMessage.split(".");
                        txt_temp.setText(separated[0]);
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                    }
                });

            }
        }.execute();
    }

    private void subscribe(String[] topicToSubscribe) {
        final String[] topic = topicToSubscribe;
        int[] qos = {1};
        try {
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("Subscription successful to topic: " + topic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    System.out.println("Failed to subscribe to topic: " + topic);
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using  wildcards
                }
            });
        } catch (MqttException e)
        {
            e.printStackTrace();
        }
    }
}

//    private void connect(){
//        memPer = new MemoryPersistence();
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
//
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
//                    // The subscription could not be performed, maybe the user was not
//                    // authorized to subscribe on the specified topic e.g. using wildcards
//                }
//            });
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }
