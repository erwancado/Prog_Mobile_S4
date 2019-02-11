package com.ecado.implicitintent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button send=findViewById(R.id.Send);
        final Intent intention = new Intent(this,Receiver.class);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message= String.valueOf(((EditText) findViewById(R.id.Message)).getText());
                intention.putExtra("com.ecado.message",message);
                startActivity(intention);

            }
        });
    }
}
