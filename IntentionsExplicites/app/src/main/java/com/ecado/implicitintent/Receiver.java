package com.ecado.implicitintent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Receiver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);
        Intent messageIntent = getIntent();
        String message=messageIntent.getStringExtra("com.ecado.message");
        ((TextView) findViewById(R.id.displayMessage)).setText(message);
        final Intent backIntent = new Intent(this,MainActivity.class);
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(backIntent);
            }
        });
    }
}
