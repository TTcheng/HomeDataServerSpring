package com.wangchuncheng.service;

import com.wangchuncheng.entity.HomeData;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * Mqtt Service.
 * This class offers MQTT publish homedata service.
 */
public class MqttService implements MqttCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqttService.class);
    private Integer qos;
    private String userName;
    private String broker;
    private String password;
    private String pubTopic;
    private String subTopic;
    private final String clientId = "monitor_mqtt_java_" + UUID.randomUUID().toString();

    private MqttClient mqttClient;
    private MqttConnectOptions connOpts;
    @Autowired
    private HomeDataService homeDataService;

    public void init() {
        connOpts = new MqttConnectOptions();
        connOpts.setUserName(userName);
        connOpts.setPassword(password.toCharArray());
        try {
            mqttClient = new MqttClient(broker, clientId);
        } catch (MqttException e) {
            LOGGER.error("mqttClient = new MqttClient(broker, clientId) ", e);
        }
        mqttClient.setCallback(this);
        connect();
    }

    private void connect() {
        try {
            mqttClient.connect(connOpts);
            LOGGER.debug("连接建立成功");
            mqttClient.subscribe(subTopic);
            LOGGER.debug("Subscribed to topic: {}", subTopic);
        } catch (MqttException e) {
            LOGGER.error("连接建立失败", e);
        }
    }

    /**
     * pub msg:String using given topic
     *
     * @param msg   消息
     * @param topic 主题
     */
    private void pub(String msg, String topic) {
        MqttMessage message = new MqttMessage(msg.getBytes());
        message.setQos(qos);
        message.setRetained(false);
        try {
            mqttClient.publish(topic, message);
        } catch (MqttException e) {
            LOGGER.error("发布失败", e);
        }
    }

    /**
     * publish home data list
     *
     * @param homeDataList 数据列表
     */
    public void publishHomeData(List<HomeData> homeDataList) {
        if (mqttClient == null) {
            connect();
            LOGGER.debug("mqtt client is null 开始重连！");
        }
        for (HomeData homeData : homeDataList) {
            pub(homeData.toString(), pubTopic);
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        LOGGER.error("Connection lost!!!!!!!!!");
    }

    /**
     * 根据请求返回对应数据
     */
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {
        String msg = new String(mqttMessage.getPayload());
        LOGGER.debug("MQTT message received: {}", msg);

        String[] queries = msg.split("_");  //request_homeid_limit
        if (queries[0].equals("request")) { //do request
            List<HomeData> homeDataList = homeDataService.queryForListByLimit(queries[1], Long.parseLong(queries[2]));
            this.publishHomeData(homeDataList);
        } else {
            LOGGER.error("Unknown request");
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        LOGGER.debug("deliveryComplete");
    }

    // setters

    public void setQos(Integer qos) {
        this.qos = qos;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPubTopic(String pubTopic) {
        this.pubTopic = pubTopic;
    }

    public void setSubTopic(String subTopic) {
        this.subTopic = subTopic;
    }
}

