package com.mixr;

/**
 * Project Name: Mixr
 * Date: 11/6/2019
 * Description:
 *
 * @Author Elias Afzalzada
 * Copyright © Elias Afzalzada - All Rights Reserved
 */

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MusicPlayer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
    }

    public void playSong(View view){

        ImageButton playButton = findViewById(R.id.playButton);
        playButton.setImageResource(R.drawable.stop);


        Toast msg = Toast.makeText(this, "play button works", Toast.LENGTH_SHORT);
        msg.show();

    }

}
