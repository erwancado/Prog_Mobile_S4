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
        Button modifyId=findViewById(R.id.modifyID);
        final Intent messageIntent = new Intent(this,Receiver.class);
        final Intent IdModifierIntent = new Intent(this,IdModifier.class);
        IdModifierIntent.putExtra("com.ecado.name",((TextView) findViewById(R.id.name)).getText().toString());
        IdModifierIntent.putExtra("com.ecado.surname",((TextView) findViewById(R.id.surname)).getText().toString());
        IdModifierIntent.putExtra("com.ecado.number",((TextView) findViewById(R.id.number)).getText().toString());
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message= String.valueOf(((EditText) findViewById(R.id.Message)).getText());
                messageIntent.putExtra("com.ecado.message",message);
                startActivity(messageIntent);

            }
        });
        modifyId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(IdModifierIntent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent modificationIntent = getIntent();
        Button modifyId=findViewById(R.id.modifyID);
        final Intent IdModifierIntent = new Intent(this,IdModifier.class);
        if(modificationIntent.hasExtra("com.ecado.inputName")) {
            String name = modificationIntent.getStringExtra("com.ecado.inputName");
            String surname = modificationIntent.getStringExtra("com.ecado.inputSurname");
            String number = modificationIntent.getStringExtra("com.ecado.inputNumber");
            ((TextView) findViewById(R.id.name)).setText(name);
            ((TextView) findViewById(R.id.surname)).setText(surname);
            ((TextView) findViewById(R.id.number)).setText(number);
            IdModifierIntent.putExtra("com.ecado.name",name);
            IdModifierIntent.putExtra("com.ecado.surname",surname);
            IdModifierIntent.putExtra("com.ecado.number",number);
        }
        modifyId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(IdModifierIntent);
            }
        });
    }
}
