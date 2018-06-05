import com.wangchuncheng.entity.HomeData;
import com.wangchuncheng.entity.Mapping;
import com.wangchuncheng.service.InfluxdbService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tests {
    private InfluxdbService influxdbService;
    private ApplicationContext ctx;
    {
        ctx = new ClassPathXmlApplicationContext("spring-config.xml");
        influxdbService = (InfluxdbService) ctx.getBean("influxdbService");
    }
    @Test
    public void testInfluxdbSerivce(){
        List<List<Mapping>> mappingsList = new ArrayList<>();
        HomeData homeData = new HomeData("101",25.6,56,new Date().getTime());
        mappingsList.add(homeData.toHomeDataMapping());
        influxdbService.writeToInfluxdb(mappingsList,"homeData");
    }
}
