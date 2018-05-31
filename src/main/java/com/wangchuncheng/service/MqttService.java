package com.wangchuncheng.service;

import com.wangchuncheng.controller.DataSender;
import com.wangchuncheng.controller.TaskExecutePool;
import com.wangchuncheng.entity.HomeData;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;

/**
 * Mqtt Service.
 * This class offers MQTT publish homedata service.
 */
public class MqttService {
    @Autowired
    private TaskExecutePool executePool;
    private int qos;
    private String userName;
    private String broker;
    private String password;
    private String pubTopic;
    private String subTopic;
    private String clientId = "monitor_mqtt_java_" + UUID.randomUUID().toString();

    private MqttClient mqttClient;
    private MqttConnectOptions connOpts;
    private Executor executor;

    /**
     * constructor
     */
    public MqttService() {

    }
    public void init(){
        executor = executePool.getExecutor();
        connOpts = new MqttConnectOptions();
        connOpts.setUserName(userName);
        connOpts.setPassword(password.toCharArray());
        try {
            mqttClient = new MqttClient(broker, clientId);
        } catch (MqttException e) {
            System.out.println("mqttClient = new MqttClient(broker, clientId) ");
            e.printStackTrace();
        }
        mqttClient.setCallback(new MyMqttCallback());
        connect();
    }
    /**
     * connect
     */
    public void connect() {
        try {
            mqttClient.connect(connOpts);
            System.out.println("连接建立成功");
            mqttClient.subscribe(subTopic);
            System.out.println("Subscribed to topic: " + subTopic);
        } catch (MqttException e) {
            e.printStackTrace();
            System.out.println("连接建立失败");
        }
    }

    /**
     * pub msg:String using given topic
     *
     * @param msg
     * @param topic
     */
    private void pub(String msg, String topic) {
        MqttMessage message = new MqttMessage(msg.getBytes());
        message.setQos(qos);
        message.setRetained(false);
        try {
            mqttClient.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
            System.out.println("发布失败！");
        }
    }

    /**
     * publish home data list
     *
     * @param homeDataList
     * @return status code:int
     */
    public int publishHomeData(List<HomeData> homeDataList) {
        if (mqttClient != null) {

        } else {
            connect();
            System.out.println("mqtt client is null 开始重连！");
        }
        for (int i = 0; i < homeDataList.size(); i++) {
            pub(homeDataList.get(i).toString(), pubTopic);
        }
        return 0;
    }

    //getter and setter

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPubTopic() {
        return pubTopic;
    }

    public void setPubTopic(String pubTopic) {
        this.pubTopic = pubTopic;
    }

    public String getSubTopic() {
        return subTopic;
    }

    public void setSubTopic(String subTopic) {
        this.subTopic = subTopic;
    }
}

/**
 * MqttCallBack
 */
class MyMqttCallback implements MqttCallback {
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
            dataSender.setHomeID(queries[1]);
            dataSender.setLimit(Long.parseLong(queries[2]));
            executor.execute(dataSender);
        }
        //else {}//Other request
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
