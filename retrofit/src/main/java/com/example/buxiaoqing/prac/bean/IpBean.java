package com.example.buxiaoqing.prac.bean;
/*Retrofit2.0*/

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IpBean {
    //国家
    public String country;
    //国家代码
    @SerializedName("country_id")
    public String countryId;
    //地区
    public String area;
    //地区代码
    @SerializedName("area_id")
    public String areaId;
    //省份
    public String region;
    //省份代码
    @SerializedName("region_id")
    public String regionId;
    //城市
    public String city;
    //城市代码
    @SerializedName("city_id")
    public String cityId;
    //区
    public String county;
    //区号
    @SerializedName("county_id")
    public String countyId;
    //运营商
    public String isp;
    //运营商代码
    @SerializedName("isp_id")
    public String ispId;
    //IP地址
    public String ip;
}