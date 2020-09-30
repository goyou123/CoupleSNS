package com.example.couplesns.DataClass;

public class GalleryData {
    String idx;
    String images;

    public GalleryData(String idx, String images) {
        this.idx = idx;
        this.images = images;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
