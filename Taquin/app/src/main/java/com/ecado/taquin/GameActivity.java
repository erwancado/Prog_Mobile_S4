package com.ecado.taquin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity {

    private Chronometer gameChrono;
    private int gridSize;
    private int imageID;
    private List<ImagePiece> imagePieces;
    private boolean isPaused;
    private int movesNumber;
    private long pauseTime;
    private RecyclerView recyclerView;
    private long score;
    private SharedPreferences sharedPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.gameChrono = ((Chronometer)findViewById(R.id.gameChrono));
        this.movesNumber = 0;
        this.score = 10000;
        this.isPaused = false;
        this.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        this.imageID = this.sharedPrefs.getInt("game_image", 0);
        this.gridSize = this.sharedPrefs.getInt("grid_size", 3);
        getPiecesFromImage();
        recyclerView=findViewById(R.id.recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this, gridSize, 1, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        GameAdapter adapter = new GameAdapter(imagePieces);
        recyclerView.setAdapter(adapter);
        gameChrono.start();

    }

    /**
     * Come back to the menu when the button is pressed
     * @param view
     */
    public void onMenuButton(View view)
    {
        gameChrono.stop();
        startActivity(new Intent(this, MainActivity.class));
    }

    protected void onPause()
    {
        super.onPause();
        pauseTime = (this.gameChrono.getBase() - SystemClock.elapsedRealtime());
        gameChrono.stop();
    }

    /**
     * Pause the game or resume the game if it is already paused
     * @param view
     */
    public void onPauseButton(View view)
    {
        if (isPaused)
        {
            isPaused = false;
            gameChrono.setBase(SystemClock.elapsedRealtime() + this.pauseTime);
            gameChrono.start();
            ((Button)findViewById(R.id.pauseButton)).setText(R.string.pause);
            return;
        }
        isPaused = true;
        pauseTime = (this.gameChrono.getBase() - SystemClock.elapsedRealtime());
        gameChrono.stop();

        ((Button)findViewById(R.id.pauseButton)).setText(R.string.start);
    }

    /**
     * Restart the game
     * @param view
     */
    public void onRestartButton(View view)
    {
        gameChrono.setBase(SystemClock.elapsedRealtime());
        pauseTime = 0L;
        mixImagePieces();
        recyclerView.setAdapter(new GameAdapter(this.imagePieces));
    }

    /**
     * Divide the chosen image into shuffled pieces to fill the grid
     */
    private void getPiecesFromImage()
    {
        imagePieces=new ArrayList<>();
        Bitmap img = BitmapFactory.decodeResource(getResources(),imageID);
        Bitmap[] splitImg = Tools.splitBitmap(img,gridSize,960,960);
        for (int i=0;i<splitImg.length;i++)
            imagePieces.add(new ImagePiece(splitImg[i],i));
        mixImagePieces();
    }

    /**
     * Randomly mix the pieces
     */
    private void mixImagePieces()
    {
        int i = new Random().nextInt(gridSize * gridSize);
        if ((i - 1 >= 0) && (i - 1 < gridSize * gridSize))
        {
            Collections.swap(imagePieces, i - 1, i);
            if ((i - gridSize >= 0) && (i - gridSize< gridSize* gridSize))
            {
                Collections.swap(imagePieces, i - gridSize, i);
            }
        }

    }
    protected void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);
        pauseTime = (gameChrono.getBase() - SystemClock.elapsedRealtime());
        gameChrono.stop();
        bundle.putLong("chrono_pause", this.pauseTime);
    }


    protected void onRestoreInstanceState(Bundle bundle)
    {
        super.onRestoreInstanceState(bundle);
        if ((bundle != null) && (bundle.containsKey("chrono_pause")))
        {
            pauseTime = bundle.getLong("chrono_pause");
            gameChrono.setBase(SystemClock.elapsedRealtime() + this.pauseTime);
            gameChrono.start();
        }
    }

    /**
     * Check if all the pieces came back to their initial position
     * @return true if it is the case or false if it is not
     */
    private boolean checkGameFinished()
    {
        boolean bool = true;
        int i = 0;
        while ((i < imagePieces.size()) && (bool)) {
            if (imagePieces.get(i).getInitialPosition() != i) {
                bool = false;
            } else {
                i += 1;
            }
        }
        return bool;
    }

    private void saveScore(){
        String scores =sharedPrefs.getString("scores",null);
        if(scores==null)
            sharedPrefs.edit().putString("scores",String.valueOf(score)).apply();
        else
            sharedPrefs.edit().putString("scores",scores.concat(String.valueOf(score)+";")).apply();
    }

    /**
     * Adapter for the recycler view containing the pieces with method to move pieces and display
     * dialog at the end of the game
     */
    public class GameAdapter
            extends RecyclerView.Adapter<GameAdapter.MyViewHolder>
    {
        List<ImagePiece> imageDataList = Collections.emptyList();

        GameAdapter(List<ImagePiece> items)
        {
            this.imageDataList = items;
        }

        public int getItemCount()
        {
            return this.imageDataList.size();
        }

        @NonNull
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int paramInt)
        {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_piece_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
            myViewHolder.currentImage.setImageBitmap(imageDataList.get(i).getBmp());
            myViewHolder.currentLabel.setText(String.valueOf(imageDataList.get(i).getInitialPosition()));
            myViewHolder.currentImage.setOnClickListener(new View.OnClickListener() {
                /**
                 * Move the chosen piece in the empty case
                 * @param i position of the chosen piece
                 */
                private void switchPieces(int i)
                {
                    if ((i - 1 >= 0) && (i - 1 < gridSize*gridSize) && ((imagePieces.get(i - 1)).getBmp()==null))
                    {
                        Collections.swap(imagePieces, i - 1, i);
                        notifyItemChanged(i - 1);
                        notifyItemChanged(i);
                        recyclerView.setAdapter(new GameAdapter(imagePieces));
                    }
                    if ((i - gridSize >= 0) && (i - gridSize< gridSize*gridSize) && (imagePieces.get(i - gridSize).getBmp()==null))
                        {
                            Collections.swap(imagePieces, i - gridSize, i);
                            notifyItemChanged(i - gridSize);
                            notifyItemChanged(i);
                            recyclerView.setAdapter(new GameAdapter(imagePieces));
                        }
                    if ((i + 1 >= 0) && (i + 1 < gridSize*gridSize) && ((imagePieces.get(i + 1)).getBmp()==null))
                        {
                            Collections.swap(imagePieces, i + 1, i);
                            notifyItemChanged(i + 1);
                            notifyItemChanged(i);
                            recyclerView.setAdapter(new GameAdapter(imagePieces));
                        }
                    if ((i + gridSize >= 0) && (i + gridSize< gridSize*gridSize) && (imagePieces.get(i + gridSize).getBmp()==null))
                    {
                        Collections.swap(imagePieces, i + gridSize, i);
                        notifyItemChanged(i + gridSize);
                        notifyItemChanged(i);
                        recyclerView.setAdapter(new GameAdapter(imagePieces));
                }

                }
                private void createChooseActionDialog()
                {
                    DialogInterface.OnClickListener restartClick = new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface choiceDialog, int button)
                        {
                            onRestartButton(null);
                        }
                    };
                    DialogInterface.OnClickListener menuClick = new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface choiceDialog, int button)
                        {
                            Intent menuIntent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(menuIntent);
                        }
                    };
                    DialogInterface.OnClickListener scoresClick = new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface choiceDialog, int button)
                        {
                            Intent scoreIntent = new Intent(getApplicationContext(),ScoresActivity.class);
                            startActivity(scoreIntent);
                        }
                    };
                    AlertDialog.Builder chooseActionDialog = new AlertDialog.Builder(myViewHolder.currentImage.getContext());
                    chooseActionDialog.setTitle(R.string.winDialogTitle);
                    chooseActionDialog.setMessage(R.string.winDialogMessage1+score+".\n"+R.string.winDialogMessage2);
                    chooseActionDialog.setPositiveButton(R.string.winDialogNewGame, restartClick);
                    chooseActionDialog.setNeutralButton(R.string.menu, menuClick);
                    chooseActionDialog.setNegativeButton(R.string.scores, scoresClick);
                    chooseActionDialog.create();
                    chooseActionDialog.setCancelable(false);
                    chooseActionDialog.show();
                }
                @Override
                public void onClick(View v) {
                    if (isPaused) {
                        Toast.makeText(getApplicationContext(), R.string.pauseMessage, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        switchPieces(myViewHolder.getAdapterPosition());
                        movesNumber++;
                        if(checkGameFinished()){
                            long finalTime = SystemClock.elapsedRealtime() - gameChrono.getBase();
                            finalTime=TimeUnit.MILLISECONDS.toSeconds(finalTime);
                            gameChrono.stop();
                            score-=finalTime*1000+movesNumber*10;
                            saveScore();
                            createChooseActionDialog();
                        }
                    }
                }
            });
        }

        class MyViewHolder
                extends RecyclerView.ViewHolder
        {
            private ImageView currentImage;
            private TextView currentLabel;

            MyViewHolder(View view)
            {
                super(view);
                this.currentImage = ((ImageView)view.findViewById(R.id.gameImageView));
                this.currentLabel = ((TextView)view.findViewById(R.id.initialPosition));
            }
        }
    }
}
