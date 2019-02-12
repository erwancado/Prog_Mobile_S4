package com.ecado.implicitintents;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sms = findViewById(R.id.smsButton);
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent=new Intent(Intent.ACTION_SENDTO);
                String number=((EditText)findViewById(R.id.phoneNumber)).getText().toString();
                if(!number.equals("")) {
                    Uri smsData = Uri.parse("sms:"+number);
                    smsIntent = smsIntent.setData(smsData);
                    if (smsIntent.resolveActivity(getPackageManager()) != null)
                        startActivity(smsIntent);
                }
            }
        });
        Button mms=findViewById(R.id.mmsButton);
        mms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mmsIntent=new Intent(Intent.ACTION_SENDTO);
                String number=((EditText)findViewById(R.id.phoneNumber)).getText().toString();
                if(!number.equals("")) {
                    Uri mmsData = Uri.parse("mms:"+number);
                    mmsIntent = mmsIntent.setData(mmsData);
                    if (mmsIntent.resolveActivity(getPackageManager()) != null)
                        startActivity(mmsIntent);
                }
            }
        });
        Button call=findViewById(R.id.callButton);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent=new Intent(Intent.ACTION_DIAL);
                String number=((EditText)findViewById(R.id.phoneNumber)).getText().toString();
                if(!number.equals("")) {
                    Uri callData= Uri.parse("tel:"+number.trim());
                    callIntent = callIntent.setData(callData);
                    if (callIntent.resolveActivity(getPackageManager()) != null)
                        startActivity(callIntent);
                }
            }
        });
        Button web=findViewById(R.id.webButton);
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent=new Intent(Intent.ACTION_VIEW);
                String url=((EditText)findViewById(R.id.url)).getText().toString();
                if(!url.equals("")) {
                    webIntent.setData(Uri.parse(url));
                    if (webIntent.resolveActivity(getPackageManager()) != null)
                        startActivity(webIntent);
                }
            }
        });
        Button map=findViewById(R.id.mapButton);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent=new Intent(Intent.ACTION_VIEW);
                String lat=((EditText)findViewById(R.id.latitude)).getText().toString();
                String longi=((EditText)findViewById(R.id.longitude)).getText().toString();
                if(!lat.equals("")&&!longi.equals("")) {
                    mapIntent.setData(Uri.parse("geo:"+lat+","+longi));
                    if (mapIntent.resolveActivity(getPackageManager()) != null)
                        startActivity(mapIntent);
                }
            }
        });
    }
}
