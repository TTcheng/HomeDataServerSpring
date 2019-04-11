package com.wangchuncheng.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chuncheng.wang@hand-china.com 2019-01-29 16:29:28
 */
@ConfigurationProperties(prefix = "mqtt")
public class MqttConfigProperties {

    private String userName;
    private String password;
    private String brokerURL;
    private String pubTopic;
    private String subTopic;
    private Integer qos;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBrokerURL() {
        return brokerURL;
    }

    public void setBrokerURL(String brokerURL) {
        this.brokerURL = brokerURL;
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

    public Integer getQos() {
        return qos;
    }

    public void setQos(Integer qos) {
        this.qos = qos;
    }
}
