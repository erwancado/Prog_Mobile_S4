package com.ecado.taquin;

public class ImageItem {
    private String title;
    private Integer imageURL;

    ImageItem(String title, int imageURL) {
        this.title = title;
        this.imageURL = imageURL;
    }

    String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getImageURL() {
        return imageURL;
    }

    public void setImageURL(int imageURL) {
        this.imageURL = imageURL;
    }
}