package com.mixr;

/**
 * Project Name: Mixr
 * Date: 11/6/2019
 * Description:
 *
 * @Author Elias Afzalzada
 * Copyright © Elias Afzalzada - All Rights Reserved
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SearchList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
    }

    public void musicPlayer(View view){
        Intent musicPlayerIntent = new Intent(this, MusicPlayer.class);
        startActivity(musicPlayerIntent);
    }

}
