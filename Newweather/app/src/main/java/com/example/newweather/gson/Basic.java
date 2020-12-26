package com.example.newweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 服务器返回的天气信息对应的实体类
 * Created by he on 2017/1/18.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;
    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }

}
