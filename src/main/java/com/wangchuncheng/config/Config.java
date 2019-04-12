package com.wangchuncheng.config;

import com.wangchuncheng.service.InfluxdbService;
import com.wangchuncheng.service.MqttService;
import org.influxdb.InfluxDB;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.influx.InfluxDbAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chuncheng.wang@hand-china.com 2019-01-29 16:29:28
 */
@Configuration
@AutoConfigureAfter(InfluxDbAutoConfiguration.class)
@EnableConfigurationProperties({InfluxConfigProperties.class, MqttConfigProperties.class})
public class Config {

    @Bean
    @ConditionalOnProperty("mqtt.brokerURL")
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
    @ConditionalOnClass(InfluxDB.class)
    public InfluxdbService influxdbService(InfluxDB influxDB){
        return new InfluxdbService(influxDB);
    }
}
