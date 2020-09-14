package com.example.couplesns.DataClass;


public class Viewpage_Img {
    String img;
    int appPage;
    int currentPage;

    public Viewpage_Img(String img, int appPage, int currentPage) {
        this.img = img;
        this.appPage = appPage;
        this.currentPage = currentPage;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getAppPage() {
        return appPage;
    }

    public void setAppPage(int appPage) {
        this.appPage = appPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
