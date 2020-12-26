package com.example.newweather.db;

import org.litepal.crud.DataSupport;

/**
 * 使用LitePal创建表需要用到类，对应County表
 * Created by he on 2017/1/15.
 */

public class County  extends DataSupport {
    private int id;
    private String countyName;
    private String weather;
    private int cityId;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
