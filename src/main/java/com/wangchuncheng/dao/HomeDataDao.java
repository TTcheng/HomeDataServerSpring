package com.wangchuncheng.dao;

import com.wangchuncheng.entity.HomeData;

import java.util.List;

public interface HomeDataDao {
    public void writeToDatabase(HomeData[] datas);
    public void writeToDatabase(HomeData data);
    public List<HomeData> queryForListByTimerange(String homeId, long begin, long end);
    public List<HomeData> queryForListByLimit(String homeId, long limit);
}
