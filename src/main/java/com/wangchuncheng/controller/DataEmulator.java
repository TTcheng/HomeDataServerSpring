package com.wangchuncheng.controller;

import com.wangchuncheng.dao.HomeDataDao;
import com.wangchuncheng.entity.HomeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Data Emulator.
 * this class generate homedata randomly
 */
@Controller
public class DataEmulator implements Runnable {
    @Autowired
    private HomeDataDao homeDataDao;

    public DataEmulator() {
    }

    /**
     * override method of Runnable interface
     */
    public void run() {
        int maxtime = 10000000;
        while (maxtime > 0) {
            for (int i = 0; i < preHomeIds.length; i++) {
                for (int j = 0; j < sufHomeIds.length; j++) {
                    emulateHomedata(preHomeIds[i] + sufHomeIds[j]);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            maxtime--;
        }
    }

    /**
     * 产生随机homedata
     */
    public void emulateHomedata(String homeId) {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        HomeData homeData = new HomeData();

        double temperature = Math.random() * 120 - 40;  //-40~80℃
        double humidity = Math.random() * 100;        //0~100%RH Relative Humidity

        homeData.setPointtime(new Date().getTime());
        homeData.setHomeId(homeId);
        homeData.setTemperature(Double.parseDouble(decimalFormat.format(temperature)));
        homeData.setHumidity(Double.parseDouble(decimalFormat.format(humidity)));

        System.out.println("emulated : " + homeData);
        /**
         * measurement : homedata
         */
        homeDataDao.writeToDatabase(homeData);
    }

    public final String[] preHomeIds = new String[]{      //10 floors
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
    };
    public final String[] sufHomeIds = new String[]{      //9 rooms
            "01", "02", "03", "04", "05", "06", "07", "08", "09"
    };
}