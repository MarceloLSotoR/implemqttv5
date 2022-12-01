package com.msotor.implemqttv5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ImpleMQTTV5";
    private String topic = "Topic01";
    private String content = "Mensaje ejemplo publicación";
    private int qos = 0;
    //private String broker = "tcp://10.110.64.102:1883";
    private String broker = "tcp://10.110.64.67:1883";
    private String clientId = "AndroidAPPMSR_ID_v5";
    private MemoryPersistence persistence = new MemoryPersistence();
    MqttConnectionOptions mqttConnectionOptions = new MqttConnectionOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mqttConnectionOptions.setUserName("marcelo");
        mqttConnectionOptions.setPassword("1235".getBytes());
        mqttConnectionOptions.setCleanStart(false);
        mqttConnectionOptions.setAutomaticReconnect(true);

        try {
            MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void disconnected(MqttDisconnectResponse disconnectResponse) {

                }

                @Override
                public void mqttErrorOccurred(MqttException exception) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.i(TAG, "Mensaje recibido:" + message.toString());
                }

                @Override
                public void deliveryComplete(IMqttToken token) {

                }

                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    Log.i(TAG, "Conectado a: "+ mqttClient.getCurrentServerURI());
                    try {
                        mqttClient.subscribe(topic, qos);
                        Log.i(TAG, "Suscrito a: "+topic);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void authPacketArrived(int reasonCode, MqttProperties properties) {

                }
            });

            Log.i(TAG, "Conectando al broker: " + broker);
            mqttClient.connect(mqttConnectionOptions);

            // funciona ok, pero recibe los mensajes lento...

        } catch (MqttException me) {
            me.printStackTrace();
        }
    }
}