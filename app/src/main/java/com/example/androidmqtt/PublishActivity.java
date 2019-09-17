package com.example.androidmqtt;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class PublishActivity extends AppCompatActivity {

    String username;
    String password;
    String port;
    String subTopic;
    String pubTopic;
    String host;
    String toggleOn = "ON";
    String toggleOff = "OFF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        Button subBtn = (Button) findViewById(R.id.subBtn);
        Button pushBtn = (Button) findViewById(R.id.pushBtn);
        final ToggleButton toggleBtn = (ToggleButton) findViewById(R.id.toggleBtn);
        final EditText payload = (EditText) findViewById(R.id.payloadEditText);
        final TextView dataReceivedTextView = (TextView) findViewById(R.id.dataReceivedTextView);
        Boolean connectionDone = false;

        if (getIntent().hasExtra("username")) {
            username = (String) getIntent().getExtras().get("username");
        }
        if (getIntent().hasExtra("password")) {
            password = (String) getIntent().getExtras().get("password");
        }
        if (getIntent().hasExtra("port")) {
            port = (String) getIntent().getExtras().get("port");
        }
        if (getIntent().hasExtra("pubTopic")) {
            pubTopic = (String) getIntent().getExtras().get("pubTopic");
        }
        if (getIntent().hasExtra("host")) {
            host = (String) getIntent().getExtras().get("host");
        }
        if (getIntent().hasExtra("subTopic")) {
            subTopic = (String) getIntent().getExtras().get("subTopic");
        }
        String clientId = MqttClient.generateClientId();
        host = host + ":" + port;
        final MqttAndroidClient client = new MqttAndroidClient(PublishActivity.this, host, clientId);
        final MqttHelper helperMqtt = new MqttHelper(PublishActivity.this, clientId, host, username, password, port, subTopic, pubTopic);
        try {
            helperMqtt.connectToMQTTBroker(PublishActivity.this, host, username, password, client);
            connectionDone = true;
        }
        catch(Exception e)
        {
        }
        if(connectionDone)
        {
            helperMqtt.subscribeToProvidedTopic(PublishActivity.this, subTopic, client);
        }
        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helperMqtt.subscribeToProvidedTopic(PublishActivity.this, subTopic, client);
                helperMqtt.publishMessage("STAT", client, pubTopic);
            }
        });
        pushBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helperMqtt.publishMessage(payload.getText().toString(), client, pubTopic);
                payload.setText("");
            }
        });
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(PublishActivity.this, "Connection lost", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                dataReceivedTextView.setText(new String(message.getPayload()));
                if (dataReceivedTextView.getText().equals("ON")) {
                    toggleBtn.post(new Runnable() {
                        @Override
                        public void run() {
                            toggleBtn.setChecked(true);
                        }
                    });
                }
                if (dataReceivedTextView.getText().equals("OFF")) {
                    toggleBtn.post(new Runnable() {
                        @Override
                        public void run() {
                            toggleBtn.setChecked(false);
                        }
                    });
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        toggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    helperMqtt.publishMessage(toggleOn, client, pubTopic);
                } else {
                    helperMqtt.publishMessage(toggleOff, client, pubTopic);
                }
            }
        });
        toggleBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(PublishActivity.this);
                 final View mView = getLayoutInflater().inflate(R.layout.set_toggle_values, null);
                final EditText etToggleOn = (EditText) mView.findViewById(R.id.editTextToggleOn);
                final EditText etToggleOff = (EditText) mView.findViewById(R.id.editTextToggleOff);
                Button setValuesBtn = (Button)  mView.findViewById(R.id.setToggleValuesBtn);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                setValuesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toggleOn = etToggleOn.getText().toString();
                        toggleOff = etToggleOff.getText().toString();
                        dialog.dismiss();
                    }
                });
                return false;
            }
        });
    }
}