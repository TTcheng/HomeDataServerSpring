package com.wangchuncheng.service.impl;

import com.wangchuncheng.entity.HomeData;
import com.wangchuncheng.entity.Mapping;
import com.wangchuncheng.service.HomeDataService;
import com.wangchuncheng.service.InfluxdbService;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class HomeDataServiceImpl implements HomeDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeDataServiceImpl.class);
    private final InfluxdbService influxdbService;

    @Value("${datastore.dbName}")
    private String dbName;
    @Value("${datastore.measurement}")
    private String measurement;

    public HomeDataServiceImpl(InfluxdbService influxdbService) {
        this.influxdbService = influxdbService;
    }

    @Override
    public void writeToDatabase(HomeData[] datas) {
        List<List<Mapping>> mappingsList = new ArrayList<>();
        for (HomeData homedata : datas) {
            List<Mapping> mappings = homedata.toHomeDataMapping();
            mappingsList.add(mappings);
        }
        influxdbService.writeToInfluxdb(mappingsList, measurement, dbName);
    }

    @Override
    public void writeToDatabase(HomeData data) {
        List<List<Mapping>> mappingsList = new ArrayList<>();
        List<Mapping> mappings = data.toHomeDataMapping();
        mappingsList.add(mappings);
        influxdbService.writeToInfluxdb(mappingsList, measurement, dbName);
    }

    @Override
    public List<HomeData> queryForListByTimerange(String homeId, long begin, long end) {
        String sql = "select * from MEASUREMENT" + measurement.trim() + "  where homeId='" + homeId + "' and time>" + begin + " and time<" + end;
        return processQueryResults(influxdbService.query(sql, dbName));
    }

    @Override
    public List<HomeData> queryForListByLimit(String homeId, long limit) {
        String sql = "SELECT * FROM " + measurement.trim() + "  WHERE homeId='" + homeId + "' LIMIT " + limit;
        return processQueryResults(influxdbService.query(sql, dbName));
    }

    /**
     * 处理查询结果,将结果转化成HomeData的列表
     *
     * @param queryResult 查询结果
     * @return home data list.
     */
    private List<HomeData> processQueryResults(QueryResult queryResult) {
        List<HomeData> homeDataList = new LinkedList<>();
        if (queryResult.getResults().isEmpty()) {
            LOGGER.warn("查询结果为空");
            return Collections.emptyList();
        } else {
            List<QueryResult.Result> results = queryResult.getResults();
            List<List<Object>> objList = results.get(0).getSeries().get(0).getValues();

            for (List<Object> objects : objList) {
                HomeData data = new HomeData();

                String timeStr = (String) objects.get(0);
                //timeStr 2018-04-25T07:02:18.119Z
                timeStr = timeStr.replace('T', ' ');
                timeStr = timeStr.substring(0, timeStr.length() - 1);
                LOGGER.debug("Time timeStr is {}", timeStr);
                Timestamp timestamp = Timestamp.valueOf(timeStr);
                data.setPointtime(timestamp.getTime());
                String homeId = (String) objects.get(3);
                double humidity = (double) objects.get(4);
                double temperature = (double) objects.get(6);
                data.setPointtime(timestamp.getTime());
                data.setHomeId(homeId);
                data.setTemperature(temperature);
                data.setHumidity(humidity);
                LOGGER.debug("查询结果:{}", data);
                homeDataList.add(data);
            }
        }
        return homeDataList;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }
}
