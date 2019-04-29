package com.traffic.pd.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/17.
 */
public class Carousel implements Serializable {
    private String id;
    private String title;
    private String pic;
    private String type;
    private String url;
    private String singerName;
    private String content = "";
    private String cover_url;
    private String total_song;
    private String slide_url;
    private String image;

    private String browser_head_url;//浏览器浏览链接
    private String browser_url;//浏览器下载链接
    private String is_browser;

    public int getPicurl() {
        return picurl;
    }

    public void setPicurl(int picurl) {
        this.picurl = picurl;
    }

    private int picurl;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getTotal_song() {
        return total_song;
    }

    public void setTotal_song(String total_song) {
        this.total_song = total_song;
    }

    public String getSlide_url() {
        return slide_url;
    }

    public void setSlide_url(String slide_url) {
        this.slide_url = slide_url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBrowser_url() {
        return browser_url;
    }

    public void setBrowser_url(String browser_url) {
        this.browser_url = browser_url;
    }

    public String getBrowser_head_url() {
        return browser_head_url;
    }

    public void setBrowser_head_url(String browser_head_url) {
        this.browser_head_url = browser_head_url;
    }

    public String getIs_browser() {
        return is_browser;
    }

    public void setIs_browser(String is_browser) {
        this.is_browser = is_browser;
    }

}
