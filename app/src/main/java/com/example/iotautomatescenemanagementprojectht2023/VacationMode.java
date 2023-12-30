package com.example.iotautomatescenemanagementprojectht2023;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class VacationMode extends AppCompatActivity {
    private DatePickerDialog fromdatePickerDialog,todatePickerDialog;
    private Button fromdateBtn,todateBtn,homeBtn,addVacation,fromtimeBtn,totimeBtn;
    private Button colorToggleButton;
    private ToggleButton toggleButton;
    private boolean isColorOn = false;
    private String tempFromDateString = "";
    private String tempToDateString = "";
    private String tempFromTimeString = "";
    private String tempToTimeString = "";
    private TextView returnresult;
    int hour, minute;
    private static final String TOPIC_SUB = "iotproject/asmvacationmode_subscribe"; //subscribe
    private static final String TOPIC_PUB = "iotproject/asmvacationmode_publish"; // publisher
    private static final String BROKER = "tcp://test.mosquitto.org:1883";
    private static final String CLIENT_ID = MqttClient.generateClientId();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_mode);

        fromdateBtn = findViewById(R.id.vacation_fromdatePickerbtn);
        todateBtn = findViewById(R.id.vacation_todatePickerbtn);
        fromtimeBtn = findViewById(R.id.vacation_fromtimePickerbtn);
        totimeBtn = findViewById(R.id.vacation_totimePickerbtn);
        homeBtn = findViewById(R.id.btn_Home);
        addVacation = findViewById(R.id.btn_add_vacation_mode);
        colorToggleButton = findViewById(R.id.btntoggle_vs);
        toggleButton = findViewById(R.id.btntoggle_vs);
        returnresult = findViewById(R.id.txv_vacationStatus);

        fromDatePicker();
        toDatePicker();
        connectToMQTTBroker();
        updateButtonColor();// Set initial color

        fromdateBtn.setText(getTodayDate());
        todateBtn.setText(getTodayDate());
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(VacationMode.this, MainActivity.class)});
            }
        });
        colorToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the color state
                isColorOn = !isColorOn;

                // Update the button color
                updateButtonColor();
            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // isChecked will be true if the button is in the "on" state
                if (isChecked) {
                    // Execute the condition when the button is ON
                    performActionWhenButtonOn();
                } else {
                    // Execute the condition when the button is OFF
                    performActionWhenButtonOff();
                }
            }
        });


        //Send Data to Python via MQTT
        addVacation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String tempFromDate =tempFromDateString;
                    String tempToDate =tempToDateString;
                    String tempFromTime =tempFromTimeString;
                    String tempToTime =tempToTimeString;

                    sendToPython(tempFromDate,tempToDate,tempFromTime,tempToTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                    String payload = new String(message.getPayload());
                    System.out.println("Incoming message: " + payload);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Update the UI (e.g., set text on a TextView)
                            updateVacationTextView(payload);
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

    private void sendToPython(String tempFromDate,String tempToDate,String tempFromTime,String tempToTime) {
        try {
            MqttClient mqttClient = new MqttClient(BROKER, CLIENT_ID, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            mqttClient.connect(options);

            // Publish the data to the topic
            String [] tempval = {tempFromDate,tempToDate,tempFromTime,tempToTime};
            String message = String.join(",", tempval);
            mqttClient.publish(TOPIC_SUB, message.getBytes(), 0, false);

            // Disconnect after sending the data
//            mqttClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateVacationTextView(String payload) {
        returnresult.setText(payload);
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
                String btnFromDateText = fromdateBtn.getText().toString();
                tempFromDateString = btnFromDateText;
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
                String btnToDateText = todateBtn.getText().toString();
                tempToDateString = btnToDateText;
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        todatePickerDialog = new DatePickerDialog(this,style,dateSetListener,year,month,day);
    }

    public void openFromTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                fromtimeBtn.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
                String btnFromTimeText = fromtimeBtn.getText().toString();
                tempFromTimeString = btnFromTimeText;
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void openToTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                totimeBtn.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
                String btnToTimeText = totimeBtn.getText().toString();
                tempToTimeString = btnToTimeText;
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return getMonthFormat(month)+"-"+ dayOfMonth +"-"+ year;

    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return  "01";
        if(month == 2)
            return  "02";
        if(month == 3)
            return  "03";
        if(month == 4)
            return  "04";
        if(month == 5)
            return  "05";
        if(month == 6)
            return  "06";
        if(month == 7)
            return  "07";
        if(month == 8)
            return  "08";
        if(month == 9)
            return  "09";
        if(month == 10)
            return  "10";
        if(month == 11)
            return  "11";
        if(month == 12)
            return  "12";
        return "01";
    }

    public void openFromDatePicker(View view) {
        fromdatePickerDialog.show();

    }

    public void openToDatePicker(View view) {
        todatePickerDialog.show();
    }
    private void updateButtonColor() {
        // Set the button color based on the state
        int color = isColorOn ? Color.rgb(7, 176, 41) :Color.rgb(63, 81, 181) ;
        colorToggleButton.setBackgroundColor(color);
    }
    private void performActionWhenButtonOff() {
        showToast("Button is OFF");
    }

    private void performActionWhenButtonOn() {
        showToast("Button is ON");
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}