package com.ecado.taquin;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

class Tools {
    private static int calculateInSampleSize(BitmapFactory.Options paramOptions, int paramInt1, int paramInt2)
    {
        int k = paramOptions.outHeight;
        int m = paramOptions.outWidth;
        int j = 1;
        int i = 1;
        if ((k > paramInt2) || (m > paramInt1))
        {
            k /= 2;
            m /= 2;
            for (;;)
            {
                j = i;
                if (k / i < paramInt2) {
                    break;
                }
                j = i;
                if (m / i < paramInt1) {
                    break;
                }
                i *= 2;
            }
        }
        return j;
    }

    static Bitmap decodeSampledBitmap(Resources paramResources, int paramInt1, int paramInt2, int paramInt3)
    {
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(paramResources, paramInt1, localOptions);
        localOptions.inSampleSize = calculateInSampleSize(localOptions, paramInt2, paramInt3);
        localOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(paramResources, paramInt1, localOptions);
    }

    static Bitmap[] splitBitmap(Bitmap picture, int gridSize, int width, int height)
    {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(picture, width, height, true);
        Bitmap[] imgs = new Bitmap[gridSize*gridSize];
        for (int i=0;i<gridSize;i++){
            for(int j=0;j<gridSize;j++){
                imgs[i*gridSize+j] = Bitmap.createBitmap(scaledBitmap,j*width/gridSize,i*height/gridSize,width/gridSize,height/gridSize);
            }
        }
        imgs[gridSize*gridSize-1]=null;
        return imgs;
    }
}
