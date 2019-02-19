package com.ecado.asynchrone;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loadData=findViewById(R.id.load);
        updateSpinner();
        loadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city=((EditText)findViewById(R.id.city)).getText().toString();
                if(!isCitySaved(city)) {
                    if (!sharedPreferences.contains("nbCities"))
                        sharedPreferences.edit().putInt("nbCities", 0).apply();
                    int nbCities = sharedPreferences.getInt("nbCities", 0) + 1;
                    sharedPreferences.edit().putInt("nbCities", nbCities).apply();
                    sharedPreferences.edit().putString("CITY" + nbCities, city).apply();
                    updateSpinner();
                }
                String s= "https://api.worldweatheronline.com/premium/v1/weather.ashx?key=3364a8a6233c452bb5c141851191902&q="+city+"&format=json&num_of_days=1";
                GetData getText=new GetData();
                try {
                    String jsonData=getText.execute(s).get();
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject currentCondition = data.getJSONArray("current_condition").getJSONObject(0);
                    String weatherDesc = currentCondition.getJSONArray("weatherDesc").getJSONObject(0).getString("value");
                    String temp = currentCondition.getString("temp_C");
                    ((TextView)findViewById(R.id.displayData)).setText(String.valueOf(weatherDesc +" --- "+ temp+ " °C"));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                GetData getImage=new GetData();
                try {
                    String jD=getImage.execute(s).get();
                    JSONObject jO=new JSONObject(jD);
                    final String iconURL=jO.getJSONObject("data").getJSONArray("current_condition").
                            getJSONObject(0).getJSONArray("weatherIconUrl").
                            getJSONObject(0).getString("value");
                    loadWeatherImage(iconURL);

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void loadWeatherImage(String url){
        ImageView imageView=findViewById(R.id.weatherImage);
        GetImage getImage=new GetImage(url,imageView);
        getImage.execute();
        imageView=getImage.imageView;
    }

    private void updateSpinner(){
        Spinner citiesSpinner = findViewById(R.id.spinner);
        sharedPreferences = getBaseContext().getSharedPreferences("user_prefs",Context.MODE_PRIVATE);
        ArrayList<String> cityList = new ArrayList<>();
        cityList.add("Talence");
        cityList.add("Paris");
        cityList.add("Londres");
        for (int i =1;i<=sharedPreferences.getInt("nbCities",0);i++)
            cityList.add(sharedPreferences.getString("CITY"+i,""));
        ArrayAdapter<String> adp = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, cityList);
        adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        citiesSpinner.setAdapter(adp);
    }
    private boolean isCitySaved(String city){
        if(city==null)
            return false;
        for (int i =1;i<=sharedPreferences.getInt("nbCities",0);i++){
            if(sharedPreferences.getString("CITY"+i,"").equals(city))
                return true;
        }
        return false;
    }
}
