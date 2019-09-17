package com.example.androidmqtt;

import android.content.Context;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MqttHelper extends MqttAndroidClient {

    public MqttHelper(Context context, String serverURI, String clientId, String host, String password,
                      String port, String subTopic, String pubTopic){
        super(context, serverURI, clientId);
    }
    public void connectToMQTTBroker(final Context context, String hostname, String username, String password, MqttAndroidClient client)
    {
        try {
            MqttConnectOptions conOption = new MqttConnectOptions();
            conOption.setPassword(password.toCharArray());
            conOption.setUserName(username);
            conOption.setConnectionTimeout(100);
            IMqttToken token = client.connect(conOption);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems

                    Toast.makeText(context, "Not Connected", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribeToProvidedTopic(final Context context, final String subscribeTopic, MqttAndroidClient client)
    {
        int qos = 1;
        try {
            IMqttToken subToken = client.subscribe(subscribeTopic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The message was published
                    Toast.makeText(context, new StringBuilder().append("Subscribed to topic\n").append(subscribeTopic).toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards
                    Toast.makeText(context, "Subscribe Failed", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void publishMessage(String messagePayload, MqttAndroidClient client, String pubTopic)
    {
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = messagePayload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(pubTopic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }
}
