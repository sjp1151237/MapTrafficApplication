package com.traffic.pd.data;

import java.io.Serializable;

public class LocationBean implements Serializable {
    String CountryName; //国家

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String locality) {
        Locality = locality;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getAdminArea() {
        return AdminArea;
    }

    public void setAdminArea(String adminArea) {
        AdminArea = adminArea;
    }

    public String getSubLocality() {
        return SubLocality;
    }

    public void setSubLocality(String subLocality) {
        SubLocality = subLocality;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    String Locality; //市
    String CountryCode;// 国家编号
    String AdminArea;//省份
    String SubLocality; //区

    String Latitude; //经度
    String Longitude;//维度
}
