package com.wangchuncheng.entity;

import org.influxdb.dto.Point;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Data entity class.
 */
@SuppressWarnings("unused")
public class HomeData implements Pointed, Serializable {
    public static final String FIELD_TIME = "time";
    public static final String FIELD_HOME_ID = "homeId";
    public static final String FIELD_TEMPERATURE = "temperature";
    public static final String FIELD_HUMIDITY = "humidity";
    //    private boolean hasHuman;   //人      true false
//    private boolean smoke;      //烟雾    true false
    @NonNull
    private String homeId;      //房间号    1-01 ~ 20-12
    private Double temperature; //温度    -40~80℃
    private Double humidity;    //湿度    相对湿度0~100％RH
    //    private double brightness;  //光照度   0.000001~200000lux
    @NonNull
    private Instant time;

    public HomeData() {
    }

    /**
     * constructor with all param
     */
    public HomeData(String homeId, double temperature, double humidity, Instant time) {
        this.homeId = homeId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.time = time;
    }

    //getter and setter


    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    @Override
    public int hashCode() {
        return homeId.hashCode() & time.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HomeData) {
            HomeData target = (HomeData) obj;
            return Objects.equals(homeId, target.getHomeId())
                    && Objects.equals(temperature, target.getTemperature())
                    && Objects.equals(humidity, target.getHumidity())
                    && Objects.equals(time, target.getTime());
        }
        return false;
    }

    @Override
    public String toString() {
        return "HomeData{" +
                "homeId='" + homeId + '\'' +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", time=" + time +
                '}';
    }

    @Override
    public Point toPoint(final String measurement) {
        Point.Builder pointBuilder = Point.measurement(measurement);
        pointBuilder.time(time.toEpochMilli(), TimeUnit.MILLISECONDS);
        pointBuilder.addField(FIELD_HOME_ID, homeId);
        pointBuilder.addField(FIELD_HUMIDITY, humidity);
        pointBuilder.addField(FIELD_TEMPERATURE, temperature);
        return pointBuilder.build();
    }
}
