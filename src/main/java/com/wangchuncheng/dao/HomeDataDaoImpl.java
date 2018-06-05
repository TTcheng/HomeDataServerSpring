package com.wangchuncheng.dao;

import com.wangchuncheng.entity.HomeData;
import com.wangchuncheng.entity.Mapping;
import com.wangchuncheng.service.InfluxdbService;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HomeDataDaoImpl implements HomeDataDao {
    @Autowired
    private InfluxdbService influxdbService;
    private String dbName;//spring config
    private String measurement;//spring config

    @Override
    public void writeToDatabase(HomeData[] datas) {
        List<List<Mapping>> mappingsList = new ArrayList<>();
        for (HomeData homedata : datas) {
            List<Mapping> mappings = homedata.toHomeDataMapping();
            mappingsList.add(mappings);
        }
        influxdbService.writeToInfluxdb(mappingsList,measurement,dbName);
    }

    @Override
    public void writeToDatabase(HomeData data) {
        List<List<Mapping>> mappingsList = new ArrayList<>();
        List<Mapping> mappings =  data.toHomeDataMapping();
        mappingsList.add(mappings);
        influxdbService.writeToInfluxdb(mappingsList,measurement,dbName);
    }

    @Override
    public List<HomeData> queryForListByTimerange(String homeId, long begin, long end) {
        String sql = "select * from MEASUREMENT" + measurement.trim() + "  where homeId='" + homeId + "' and time>" + begin + " and time<" + end;
        return processQueryResults(influxdbService.query(sql,dbName));
    }
    @Override
    public List<HomeData> queryForListByLimit(String homeId, long limit) {
        String sql = "SELECT * FROM " + measurement.trim() + "  WHERE homeId='" + homeId + "' LIMIT " + limit;
        return processQueryResults(influxdbService.query(sql,dbName));
    }
    /**
     * 处理查询结果,将结果转化成HomeData的列表
     *
     * @param queryResult
     * @return home data list.
     */
    public List<HomeData> processQueryResults(QueryResult queryResult) {
        List<HomeData> homeDataList = new LinkedList<>();
        if (queryResult.getResults().isEmpty()) {
            System.out.println("查询结果为空");
            return null;
        } else {
            List<QueryResult.Result> results = queryResult.getResults();
            List<List<Object>> objList = results.get(0).getSeries().get(0).getValues();
            int size = objList.size();

            for (int i = 0; i < size; i++) {
                HomeData data = new HomeData();

                List<Object> fieldList = objList.get(i);
                String timeStr = (String) fieldList.get(0);
                //timeStr 2018-04-25T07:02:18.119Z
                timeStr = timeStr.replace('T', ' ');
                timeStr = timeStr.substring(0, timeStr.length() - 1);
                System.out.println("Time timeStr is :" + timeStr);
                Timestamp timestamp = Timestamp.valueOf(timeStr);
                data.setPointtime(timestamp.getTime());
                String homeId = (String) fieldList.get(3);
                double humidity = (double) fieldList.get(4);
                double temperature = (double) fieldList.get(6);
                data.setPointtime(timestamp.getTime());
                data.setHomeId(homeId);
                data.setTemperature(temperature);
                data.setHumidity(humidity);
                System.out.println(data);
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
