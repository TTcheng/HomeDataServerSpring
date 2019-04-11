package com.wangchuncheng.config;

import com.wangchuncheng.service.InfluxdbService;
import com.wangchuncheng.service.MqttService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chuncheng.wang@hand-china.com 2019-01-29 16:29:28
 */
@Configuration
@EnableConfigurationProperties({InfluxConfigProperties.class, MqttConfigProperties.class})
public class Config {

    @Bean
    public MqttService mqttService(MqttConfigProperties configProperties) {

        MqttService mqttService = new MqttService();
        mqttService.setBroker(configProperties.getBrokerURL());
        mqttService.setPubTopic(configProperties.getPubTopic());
        mqttService.setSubTopic(configProperties.getSubTopic());
        mqttService.setUserName(configProperties.getUserName());
        mqttService.setPassword(configProperties.getPassword());
        mqttService.setQos(configProperties.getQos());
        mqttService.init();
        return mqttService;
    }

    @Bean
    public InfluxdbService influxdbService(InfluxConfigProperties configProperties){
        InfluxdbService influxdbService = new InfluxdbService();
        influxdbService.setUrl(configProperties.getUrl());
        influxdbService.setBatchNum(configProperties.getBatchNum());
        influxdbService.setRetention(configProperties.getRetention());
        influxdbService.setUser(configProperties.getUsername());
        influxdbService.setPassword(configProperties.getPassword());
        influxdbService.connect();
        return influxdbService;
    }
}
