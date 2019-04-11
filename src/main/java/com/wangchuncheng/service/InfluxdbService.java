package com.wangchuncheng.service;

import com.wangchuncheng.entity.Mapping;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Influxdb Service.
 * this class storage Object to influxdb and query from influxdb.
 */
@SuppressWarnings("unused")
public class InfluxdbService {
    private String url;
    private String user;
    private String password;
    private String retention;
    private int batchNum;

    private InfluxDB influxDB;

    /**
     * 将给定home data写入influxdb的数据表
     *
     * @param mappingsList 映射列表
     * @param measurement  measurement
     */
    public void writeToInfluxdb(List<List<Mapping>> mappingsList, String measurement, String dbName) {

        // Flush every 2000 Points, at least every 1000ms
        //启用批量influxDB.enableBatch(1, 1, TimeUnit.MILLISECONDS);

        BatchPoints batchPoints = BatchPoints
                .database(dbName)
                // .retentionPolicy(retention)
                .consistency(InfluxDB.ConsistencyLevel.ALL)
                .build();
        //get a mapping
        for (List<Mapping> mappings : mappingsList) {
            Point.Builder point = Point.measurement(measurement);
            //set Time
            point.time((Long) mappings.get(0).getValue(), TimeUnit.MILLISECONDS);
            //get key-values then add to point
            for (int i = 1; i < mappings.size(); i++) {
                Mapping mapping = mappings.get(i);
                //add field to point
                String key = mapping.getKey();
                Object value = mapping.getValue();
                if (value instanceof Long || value instanceof Integer) {
                    point.addField(key, (Number) value);
                } else if (value instanceof Float || value instanceof Double) {
                    point.addField(key, (Number) value);
                } else if (value instanceof Number) {
                    point.addField(key, (Number) value);
                } else if (value instanceof String) {
                    point.addField(key, (String) value);
                } else {
                    throw new RuntimeException("TypeError");
                }
            }
            batchPoints.point(point.build());//add point to batch points
        }
        influxDB.write(batchPoints);//write to influx
    }

    public QueryResult query(String sql, String dbName) {
        return influxDB.query(new Query(sql, dbName));
    }

    //    init
    public void connect() {
        influxDB = InfluxDBFactory.connect(url, user, password);
    }//end of initPara

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRetention(String retention) {
        this.retention = retention;
    }

    public void setBatchNum(int batchNum) {
        this.batchNum = batchNum;
    }
}