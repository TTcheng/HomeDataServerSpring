package com.wangchuncheng.emulator;

import com.wangchuncheng.service.HomeDataService;
import com.wangchuncheng.entity.HomeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Data Emulator.
 * this class generate homedata randomly
 */
@Controller
public class DataEmulator implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataEmulator.class);
    private HomeDataService homeDataService;

    public DataEmulator(HomeDataService homeDataService) {
        this.homeDataService = homeDataService;
    }

    /**
     * override method of Runnable interface
     */
    public void run() {
        int maxtime = 10000000;
        while (maxtime > 0) {
            for (String floor : PRE_HOME_IDS) {
                for (String room : SUF_HOME_IDS) {
                    emulateHomedata(floor + room);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.error("InterruptedException", e);
                Thread.currentThread().interrupt();
            }
            maxtime--;
        }
    }

    /**
     * 产生随机homedata
     */
    private void emulateHomedata(String homeId) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        HomeData homeData = new HomeData();

        double temperature = Math.random() * 120 - 40;  //-40~80℃
        double humidity = Math.random() * 100;        //0~100%RH Relative Humidity

        homeData.setPointtime(new Date().getTime());
        homeData.setHomeId(homeId);
        homeData.setTemperature(Double.parseDouble(decimalFormat.format(temperature)));
        homeData.setHumidity(Double.parseDouble(decimalFormat.format(humidity)));

        LOGGER.debug("emulated : {}", homeData);
        homeDataService.writeToDatabase(homeData);
    }

    private static final String[] PRE_HOME_IDS = new String[]{      //10 floors
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
    };
    private static final String[] SUF_HOME_IDS = new String[]{      //9 rooms
            "01", "02", "03", "04", "05", "06", "07", "08", "09"
    };
}