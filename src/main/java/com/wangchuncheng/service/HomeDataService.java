package com.wangchuncheng.service;

import com.wangchuncheng.entity.HomeData;

import java.util.List;

public interface HomeDataService {
    void writeToDatabase(HomeData[] datas);
    void writeToDatabase(HomeData data);
    List<HomeData> queryForListByTimerange(String homeId, long begin, long end);
    List<HomeData> queryForListByLimit(String homeId, long limit);
}
