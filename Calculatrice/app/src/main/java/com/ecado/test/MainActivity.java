package com.ecado.test;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.function.DoubleBinaryOperator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button plus = findViewById(R.id.Plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) findViewById(R.id.result)).setText(String.valueOf(calculate('+')));
            }
        });
        Button minus = findViewById(R.id.Minus);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) findViewById(R.id.result)).setText(String.valueOf(calculate('-')));
            }
        });
        Button multiply = findViewById(R.id.Multiply);
        multiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) findViewById(R.id.result)).setText(String.valueOf(calculate('*')));
            }
        });
        Button divide = findViewById(R.id.Divide);
        divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) findViewById(R.id.result)).setText(String.valueOf(calculate('/')));
            }
        });
    }
    private double calculate(char operator){
        String fN = ((EditText) findViewById(R.id.firstNumber)).getText().toString();
        String sN = ((EditText) findViewById(R.id.secondNumber)).getText().toString();
        double a = 0.0;
        double b = 0.0;
        if(!fN.equals(""))
            a = Double.parseDouble(fN);
        if(!sN.equals(""))
            b=Double.parseDouble(sN);
        switch (operator){
            case('+'):
                return a+b;
            case('-'):
                return a-b;
            case ('*'):
                return a*b;
            case ('/'):
                if(b==0)
                    return 0;
                else
                    return a/b;

                default:
                    return 0;
        }
    }
}
