package com.ecado.taquin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Main activity of the game with the menu
 */
public class MainActivity extends AppCompatActivity {

    private int[] images = {R.drawable.jaguar,R.drawable.ocelot,R.drawable.tigre,R.drawable.lynx};
    private String[] texts = {"Jaguar","Ocelot","Tigre","Lynx"};
    private SharedPreferences preferences;
    private int selectedImage = images[0];
    private TextView selectedImageLabel;
    private Spinner gridChoice;
    List<ImageItem> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView selectInfoText = findViewById(R.id.selectionInfo);
        gridChoice = findViewById(R.id.gridSpinner);
        ArrayList<String> grids = new ArrayList<>();
        grids.add("3x3");
        grids.add("4x4");
        grids.add("5x5");
        grids.add("6x6");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,grids);
        gridChoice.setAdapter(arrayAdapter);
        selectedImageLabel=findViewById(R.id.selectedImageName);
        RecyclerView imagesRecyclerView = (RecyclerView) findViewById(R.id.images);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, 0, false);
        imagesRecyclerView.setHasFixedSize(true);
        imagesRecyclerView.setLayoutManager(layoutManager);
        items = new ArrayList<>();
        for (int i=0;i<images.length;i++)
            items.add(new ImageItem(texts[i],images[i]));
        ImageAdapter adapter = new ImageAdapter(items);
        imagesRecyclerView.setAdapter(adapter);
        imagesRecyclerView.setClickable(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(preferences.getString("scores",null)==null)
            preferences.edit().putString("scores","4100;").apply();
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        this.preferences.edit().putInt("selected_image",selectedImage).apply();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selectedImage=preferences.getInt("selected_image",0);
        selectedImageLabel.setText(" "+ getLabel(selectedImage)+".");
    }

    /**
     * Start a game when the play button is pressed
     * @param view
     */
    public void onPlayButton(View view){
        int i = Integer.parseInt(String.valueOf(this.gridChoice.getSelectedItem().toString().charAt(0)));
        preferences.edit().putInt("game_image",selectedImage)
                .putInt("grid_size",i)
                .apply();
        Intent startGame = new Intent(this,GameActivity.class);
        startActivity(startGame);
    }

    /**
     * Get an image title from its id
     * @param imageId id
     * @return title
     */
    private String getLabel(int imageId)
    {
        Iterator localIterator = this.items.iterator();
        while (localIterator.hasNext())
        {
            ImageItem img = (ImageItem) localIterator.next();
            if (img.getImageURL() == imageId) {
                return img.getTitle();
            }
        }
        return ((ImageItem)this.items.get(0)).getTitle();
    }

    /**
     * Display the leaderbord when the button is pressed
     * @param view
     */
    public void onLeaderboardButton(View view) {
        Intent scoresIntent = new Intent(this,ScoresActivity.class);
        startActivity(scoresIntent);
    }

    /**
     * Adapter for the recycler view containing the images
     */
    public class ImageAdapter
            extends RecyclerView.Adapter<ImageAdapter.MyViewHolder>
    {
        List<ImageItem> imageDataList = Collections.emptyList();

        ImageAdapter(List<ImageItem> items)
        {
            this.imageDataList = items;
        }

        public int getItemCount()
        {
            return this.imageDataList.size();
        }

        public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, int paramInt)
        {
            viewHolder.currentImage.setImageBitmap(Tools.decodeSampledBitmap(MainActivity.this.getResources(),
                    ((ImageItem) this.imageDataList.get(paramInt)).getImageURL(), 150, 150));
            viewHolder.currentLabel.setText(((ImageItem)this.imageDataList.get(paramInt)).getTitle());
            viewHolder.currentImage.setOnClickListener(new View.OnClickListener()
            {
                /**
                 * Get the image selected by a click and display its title
                 * @param view the clicked imageView
                 */
                public void onClick(View view)
                {
                    selectedImage=((ImageItem)MainActivity.ImageAdapter.this.imageDataList.get(viewHolder.getAdapterPosition())).getImageURL();
                    selectedImageLabel.setText(" " + ((ImageItem)MainActivity.ImageAdapter.this.imageDataList.get(viewHolder.getAdapterPosition())).getTitle() + ".");
                }
            });
        }

        @NonNull
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int paramInt)
        {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_item_layout, viewGroup, false));
        }

        class MyViewHolder
                extends RecyclerView.ViewHolder
        {
            private ImageView currentImage;
            private TextView currentLabel;

            MyViewHolder(View paramView)
            {
                super(paramView);
                this.currentImage = ((ImageView)paramView.findViewById(R.id.image));
                this.currentLabel = ((TextView)paramView.findViewById(R.id.text));
            }
        }
    }
}
