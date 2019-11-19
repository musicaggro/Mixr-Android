package com.mixr.Application;


/**
 * Project Name: Mixr
 * Date: 11/6/2019
 * Description: Search List activity to allow user search and display in a recycler view.
 *
 * @Author Elias Afzalzada
 * Copyright © Elias Afzalzada - All Rights Reserved
 */

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mixr.Networking.SoundCloudAPI;
import com.mixr.Networking.SoundCloudService;
import com.mixr.Networking.SoundTrack;
import com.mixr.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
    }

    public void search() {

    }

    public void musicPlayer(View view) {
        Intent musicPlayerIntent = new Intent(this, MusicPlayer.class);
        startActivity(musicPlayerIntent);
    }

    public void testCall(View view) {
        SearchView userInput = findViewById(R.id.searchView);
        String userInputStr = userInput.getQuery().toString();

        SoundCloudService scRetroService = SoundCloudAPI.getScService();

        if(!userInputStr.isEmpty()){
        scRetroService.search(userInputStr).enqueue(new Callback<List<SoundTrack>>() {
            @Override
            public void onResponse(Call<List<SoundTrack>> call, Response<List<SoundTrack>> response) {
                if (response.isSuccessful()) {
                    List<SoundTrack> tracks = response.body();
                    toastMsg(tracks.get(0).getSongTitle());
                    for(SoundTrack currentSong : tracks){
                        Log.d("Song Title ", currentSong.getSongTitle());
                    }
                } else {
                    toastMsg("Error code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<SoundTrack>> call, Throwable t) {
                toastMsg("Network Error: " + t.getMessage());
            }
        });}
        else if(userInputStr.isEmpty()){
            toastMsg("Please enter a searchable Track!");
        }
    }

    public void toastMsg(String msg) {
        Toast.makeText(SearchList.this, msg, Toast.LENGTH_LONG).show();
    }

}
