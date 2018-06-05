package com.wangchuncheng.controller;

import com.wangchuncheng.dao.HomeDataDao;
import com.wangchuncheng.entity.HomeData;
import com.wangchuncheng.service.InfluxdbService;
import com.wangchuncheng.service.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Query data from influx and Send it to client using MQTT
 */
@Controller
public class DataSender implements Runnable {
    @Autowired
    private HomeDataDao homeDataDao;
    @Autowired
    private MqttService mqttService;
    private long limit;
    private String homeId;

    public DataSender() {

    }

    @Override
    public void run() {
        List<HomeData> homeDataList = homeDataDao.queryForListByLimit(homeId, limit);
        mqttService.publishHomeData(homeDataList);
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }
}
