package com.wangchuncheng.service.impl;

import com.wangchuncheng.entity.HomeData;
import com.wangchuncheng.service.HomeDataService;
import com.wangchuncheng.service.InfluxdbService;
import org.influxdb.dto.Point;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class HomeDataServiceImpl implements HomeDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeDataServiceImpl.class);
    private final InfluxdbService influxdbService;

    private String dbName;
    private String measurement;

    public HomeDataServiceImpl(InfluxdbService influxdbService) {
        this.influxdbService = influxdbService;
    }

    @Override
    public void writeToDatabase(HomeData[] datas) {
        List<Point> points = new ArrayList<>(datas.length);
        for (HomeData data : datas) {
            points.add(data.toPoint(measurement));
        }
        influxdbService.writeToInfluxdb(points, dbName);
    }

    @Override
    public void writeToDatabase(HomeData data) {
        influxdbService.writeToInfluxdb(data.toPoint(measurement), dbName);
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
        if (queryResult.getResults().isEmpty()) {
            LOGGER.warn("查询结果为空");
            return Collections.emptyList();
        }
        List<HomeData> homeDataList = new LinkedList<>();
        List<QueryResult.Result> results = queryResult.getResults();
        results.forEach(result -> {
            List<QueryResult.Series> series = result.getSeries();
            series.forEach(aSeries -> {
                List<String> columns = aSeries.getColumns();
                List<List<Object>> aSeriesValues = aSeries.getValues();
                homeDataList.addAll(getQueryData(columns, aSeriesValues));
            });
        });
        return homeDataList;
    }

    @Value("${datastore.dbName}")
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    @Value("${datastore.measurement}")
    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    /***整理列名、行数据***/
    private List<HomeData> getQueryData(List<String> columns, List<List<Object>> values) {
        List<HomeData> lists = new ArrayList<>();
        for (List<Object> list : values) {
            HomeData homeData = new HomeData();
            BeanWrapperImpl bean = new BeanWrapperImpl(homeData);
            for (int i = 0; i < list.size(); i++) {
                if (HomeData.FIELD_TIME.equals(columns.get(i))){
                    String timeStr = (String) list.get(i);
                    bean.setPropertyValue(HomeData.FIELD_TIME, Instant.parse(timeStr));
                    continue;
                }
                String propertyName = columns.get(i);//字段名
                Object value = list.get(i);//相应字段值
                bean.setPropertyValue(propertyName, value);
            }
            lists.add(homeData);
        }
        return lists;
    }
}
