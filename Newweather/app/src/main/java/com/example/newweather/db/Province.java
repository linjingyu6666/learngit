package com.example.newweather.db;

import org.litepal.crud.DataSupport;

/**
 * 使用LitePal创建表需要用到类，对应Province表
 * Created by he on 2017/1/15.
 */

public class Province  extends DataSupport {
    private int id;
    private String provinceName;
    private int provinceCode;

    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getId() {
        return id;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }
}
