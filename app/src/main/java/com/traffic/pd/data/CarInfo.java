package com.traffic.pd.data;

import java.io.Serializable;
import java.util.List;

public class CarInfo implements Serializable {
    String id;
    String uid;
    String identity;
    String country;
    String province;
    String city;
    String district;
    String introduce;

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    String driver_id;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    boolean isSelect;

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCar_num() {
        return car_num;
    }

    public void setCar_num(String car_num) {
        this.car_num = car_num;
    }

    public String getCar_num_pic() {
        return car_num_pic;
    }

    public void setCar_num_pic(String car_num_pic) {
        this.car_num_pic = car_num_pic;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCiti_list() {
        return citi_list;
    }

    public void setCiti_list(String citi_list) {
        this.citi_list = citi_list;
    }

    public String getLicense_num() {
        return license_num;
    }

    public void setLicense_num(String license_num) {
        this.license_num = license_num;
    }

    public String getDirver_pic() {
        return dirver_pic;
    }

    public void setDirver_pic(String dirver_pic) {
        this.dirver_pic = dirver_pic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getExamine_time() {
        return examine_time;
    }

    public void setExamine_time(String examine_time) {
        this.examine_time = examine_time;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getLat_n() {
        return lat_n;
    }

    public void setLat_n(String lat_n) {
        this.lat_n = lat_n;
    }

    public String getLong_n() {
        return long_n;
    }

    public void setLong_n(String long_n) {
        this.long_n = long_n;
    }

    String address;
    String car_num;
    String car_num_pic;
    String driver;
    String mobile;
    String citi_list;
    String license_num;
    String dirver_pic;

    public List<String> getCar_pic() {
        return car_pic;
    }

    public void setCar_pic(List<String> car_pic) {
        this.car_pic = car_pic;
    }

    List<String> car_pic;
    String type;
    String weight;
    String score;
    String company_id;
    String status;
    String reson;
    String add_time;
    String examine_time;
    String lat;
    String longi;
    String lat_n;
    String long_n;
}
