package com.ecado.taquin;

class ImageItem {
    private String title;
    private Integer imageURL;

    ImageItem(String title, int imageURL) {
        this.title = title;
        this.imageURL = imageURL;
    }

    String getTitle() {
        return title;
    }

    Integer getImageURL() {
        return imageURL;
    }
}