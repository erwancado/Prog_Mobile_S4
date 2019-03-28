package com.ecado.taquin;

import android.graphics.Bitmap;

class ImagePiece {
    private Bitmap bmp;
    private int initialPosition;

    ImagePiece(Bitmap bmp, int initialPosition) {
        this.bmp = bmp;
        this.initialPosition = initialPosition;
    }

    Bitmap getBmp() {
        return bmp;
    }

    int getInitialPosition() {
        return initialPosition;
    }
}
