package com.ecado.image;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMG = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loadImage=findViewById(R.id.load);
        Button horizontalMirror=findViewById(R.id.horizontalMirror);
        Button verticalMirror=findViewById(R.id.verticalMirror);
        horizontalMirror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matrix matrix=new Matrix();
                matrix.preScale(-1.0f, 1.0f);
                ImageView image=findViewById(R.id.imgView);
                Bitmap src = ((BitmapDrawable) image.getDrawable()).getBitmap();
                src=Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
                image.setImageBitmap(src);
            }
        });
        verticalMirror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matrix matrix=new Matrix();
                matrix.preScale(1.0f, -1.0f);
                ImageView image=findViewById(R.id.imgView);
                Bitmap src = ((BitmapDrawable) image.getDrawable()).getBitmap();
                src=Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
                image.setImageBitmap(src);
            }
        });
        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageIntent=new Intent(Intent.ACTION_PICK);
                imageIntent.setType("image/*");
                if(imageIntent.resolveActivity(getPackageManager())!=null)
                    startActivityForResult(imageIntent,RESULT_LOAD_IMG);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_LOAD_IMG && resultCode==RESULT_OK && data!=null){
            Uri selectedImage=data.getData();
            ((ImageView)findViewById(R.id.imgView)).setImageURI(selectedImage);
        }
    }
}
