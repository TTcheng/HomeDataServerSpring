package com.wangchuncheng.service;

import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.util.List;

/**
 * Influxdb Service.
 * this class storage Object to influxdb and query from influxdb.
 */
public class InfluxdbService {
    private InfluxDB influxDB;

    public InfluxdbService(InfluxDB influxDB) {
        this.influxDB = influxDB;
    }

    /**
     * 批量写入influxdb的数据表
     *
     * @param point InfluxdbPoint
     */
    public void writeToInfluxdb(Point point, String dbName) {
        influxDB.setDatabase(dbName);
        influxDB.write(point);
    }

    /**
     * 批量写入influxdb的数据表
     *
     * @param points InfluxdbPoint
     */
    public void writeToInfluxdb(List<Point> points, String dbName) {

        // Flush every 2000 Points, at least every 1000ms
        //启用批量influxDB.enableBatch(1, 1, TimeUnit.MILLISECONDS);
        BatchPoints batchPoints = BatchPoints
                .database(dbName)
                // .retentionPolicy(retention)
                .consistency(InfluxDB.ConsistencyLevel.ALL)
                .build();
        //get a mapping
        points.forEach(batchPoints::point);
        influxDB.write(batchPoints);//write to influx
    }

    public QueryResult query(String sql, String dbName) {
        return influxDB.query(new Query(sql, dbName));
    }
}