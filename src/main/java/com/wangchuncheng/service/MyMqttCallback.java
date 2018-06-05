package com.wangchuncheng.service;

import com.wangchuncheng.controller.DataSender;
import com.wangchuncheng.controller.TaskExecutePool;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * MqttCallBack
 */
@Component
public class MyMqttCallback implements MqttCallback {
    @Autowired
    TaskExecutePool executePool;
    @Autowired
    DataSender dataSender;

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection lost!!!!!!!!!");
    }

    /**
     * Override method. when message arrived.
     * do request
     *
     * @param s
     * @param mqttMessage
     * @throws Exception
     */
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        Executor executor = executePool.getExecutor();

        String msg = new String(mqttMessage.getPayload());
        System.out.println("MQTT message received: " + msg);

        String[] queries = msg.split("_");  //request_homeid_limit
        if (queries[0].equals("request")) { //do request
            dataSender.setHomeId(queries[1]);
            dataSender.setLimit(Long.parseLong(queries[2]));
            executor.execute(dataSender);
        }
        //else {}//Other request
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
