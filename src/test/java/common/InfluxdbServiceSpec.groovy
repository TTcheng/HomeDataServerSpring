package common

import com.wangchuncheng.Application
import com.wangchuncheng.entity.HomeData
import com.wangchuncheng.service.HomeDataService
import com.wangchuncheng.service.InfluxdbService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootContextLoader
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.Instant


/**
 *
 * @author chuncheng.wang@hand-china.com 2019-01-29 16:29:28
 */
@SpringBootTest(classes = Application.class)
@ContextConfiguration(loader = SpringBootContextLoader.class)
class InfluxdbServiceSpec extends Specification {
    @Autowired
    HomeDataService homeDataService

    void "test WriteToInfluxdb"() {
        HomeData homeData = new HomeData("101", 25.6, 56, Instant.now())
        homeDataService.writeToDatabase(homeData)

        expect:
        def res = homeDataService.queryForListByLimit("101", 1)
        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        println(res)
        println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
        homeData == res
    }
}