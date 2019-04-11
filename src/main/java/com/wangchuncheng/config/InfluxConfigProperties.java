package com.wangchuncheng.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chuncheng.wang@hand-china.com 2019-01-29 16:29:28
 */
@ConfigurationProperties(prefix = "datastore")
public class InfluxConfigProperties {
    private String dbName;
    private String measurement;
    private String retention;
    private Integer batchNum;
    private String username;
    private String password;
    private String url;
    private String topicName;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getRetention() {
        return retention;
    }

    public void setRetention(String retention) {
        this.retention = retention;
    }

    public Integer getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(Integer batchNum) {
        this.batchNum = batchNum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    //    datastore.dbName=homedata
//    datastore.measurement=homedatas
//    datastore.retention=default
//    datastore.batchNum=2000
//    datastore.userName=admin
//    datastore.password=admin
//    datastore.url=http:127.0.0.1:8086/
//    datastore.topicName=datahome
}
