package com.ecado.isbn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
        Button scan = findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                integrator.initiateScan();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TextView authorText = findViewById(R.id.author);
        TextView editorText = findViewById(R.id.editor);
        TextView yearText = findViewById(R.id.year);
        TextView titleText = findViewById(R.id.titre);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:"+result.getContents();
                GetData getText=new GetData();
                String jsonData= null;
                try {
                    jsonData = getText.execute(url).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONObject items = jsonObject.getJSONArray("items").getJSONObject(0);
                    JSONObject volumeInfo=items.getJSONObject("volumeInfo");
                    String title = volumeInfo.getString("title");
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    StringBuilder author = new StringBuilder();
                    for (int i=0;i<authors.length();i++)
                        author.append(authors.getString(i)).append(" ");
                    String date = volumeInfo.getString("publishedDate");
                    String editor = volumeInfo.getString("publisher");
                    authorText.setText(author);
                    titleText.setText(title);
                    editorText.setText(editor);
                    yearText.setText(date);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
