package com.ecado.taquin;

import android.graphics.Bitmap;

public class ImagePiece {
    private Bitmap bmp;
    private int initialPosition;

    public ImagePiece(Bitmap bmp, int initialPosition) {
        this.bmp = bmp;
        this.initialPosition = initialPosition;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public int getInitialPosition() {
        return initialPosition;
    }
}
