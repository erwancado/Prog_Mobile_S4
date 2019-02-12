package com.ecado.implicitintent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class IdModifier extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_modifier);

        final Intent validateIntent=new Intent(this,MainActivity.class);
        Intent receivedIntent=getIntent();
        String name=receivedIntent.getStringExtra("com.ecado.name");
        String surname=receivedIntent.getStringExtra("com.ecado.surname");
        String number=receivedIntent.getStringExtra("com.ecado.number");
        ((EditText)findViewById(R.id.name)).setText(name);
        ((EditText)findViewById(R.id.inputSurname)).setText(surname);
        ((EditText)findViewById(R.id.inputNumber)).setText(number);
        validateIntent.putExtra("com.ecado.inputName",name);
        validateIntent.putExtra("com.ecado.inputSurname",surname);
        validateIntent.putExtra("com.ecado.inputNumber",number);

        Button validate=findViewById(R.id.validateInput);
        Button cancel=findViewById(R.id.cancel);

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateIntent.putExtra("com.ecado.inputName",((EditText)findViewById(R.id.name)).getText().toString());
                validateIntent.putExtra("com.ecado.inputSurname",((EditText)findViewById(R.id.inputSurname)).getText().toString());
                validateIntent.putExtra("com.ecado.inputNumber",(String.valueOf(((EditText)findViewById(R.id.inputNumber)).getText())));
                startActivity(validateIntent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(validateIntent);
            }
        });
    }
}
