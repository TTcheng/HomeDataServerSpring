package generic;

import com.wangchuncheng.entity.HomeData;
import com.wangchuncheng.entity.Mapping;
import com.wangchuncheng.service.InfluxdbService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tests {
    @Autowired
    private InfluxdbService influxdbService;
    @Value("${datastore.measurement}")
    private String measurement;
    @Value("${datastore.dbName}")
    private String dbName;

    @Test
    public void testInfluxdbSerivce() {
        List<List<Mapping>> mappingsList = new ArrayList<>();
        HomeData homeData = new HomeData("101", 25.6, 56, new Date().getTime());
        mappingsList.add(homeData.toHomeDataMapping());
        influxdbService.writeToInfluxdb(mappingsList, measurement, dbName);
    }
}
