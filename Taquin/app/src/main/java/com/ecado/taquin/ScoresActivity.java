package com.ecado.taquin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ScoresActivity extends AppCompatActivity {
    /**
     * Transform the string containing the scores into a list to display
     * @param scores string of scores
     * @return a list of scores
     */
    private List<String> scoresToList(String scores){
        ArrayList<String> scoresList = new ArrayList<>();
        ArrayList<Long> scoreValues = new ArrayList<>();
        int i=0;
        while (i<scores.length()){
            String rest = scores.substring(i);
            int end = rest.indexOf(';')+i;
            String score = scores.substring(i,end);
            scoreValues.add(Long.valueOf(score));
            i=end+1;
        }
        Collections.sort(scoreValues);
        Collections.reverse(scoreValues);
        for(long scoreValue : scoreValues)
            scoresList.add((scoreValues.indexOf(scoreValue)+1)+".  --  "+String.valueOf(scoreValue));
        return scoresList;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ListView scoresList = findViewById(R.id.scoresListView);
        scoresList.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,scoresToList(Objects.requireNonNull(sharedPreferences.getString("scores", null)))));
    }

    public void onMenuButton(View view) {
        Intent menuIntent = new Intent(this,MainActivity.class);
        startActivity(menuIntent);
    }
}
