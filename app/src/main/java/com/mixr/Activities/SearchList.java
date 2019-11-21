package com.mixr.Activities;


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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mixr.Adapter.RecyclerViewAdapter;
import com.mixr.Networking.SoundCloudAPI;
import com.mixr.Networking.SoundCloudService;
import com.mixr.Networking.Track;
import com.mixr.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchList extends AppCompatActivity {

    private List<Track> listItems;
    private RecyclerViewAdapter listAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list);
        initRecyclerView();
    }

    public void musicPlayer(String streamURl, String albumUrl) {
        Intent musicPlayerIntent = new Intent(this, MusicPlayer.class);
        musicPlayerIntent.putExtra("streamUrl", streamURl);
        musicPlayerIntent.putExtra("albumUrl", albumUrl);
        startActivity(musicPlayerIntent);
    }

    //TODO: delete this test method after finished
    public void testCall(View view) {
        SearchView userInput = findViewById(R.id.searchView);
        String userInputStr = userInput.getQuery().toString();

        SoundCloudService scRetroService = SoundCloudAPI.getScService();

        if (!userInputStr.isEmpty()) {
            scRetroService.search(userInputStr).enqueue(new Callback<List<Track>>() {
                @Override
                public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                    if (response.isSuccessful()) {
                        List<Track> tracks = response.body();
                        toastMsg(tracks.get(0).getSongTitle());
                        musicPlayer(tracks.get(0).getStreamUrl(), tracks.get(0).getSongArtworkUrl());

                        for (Track currentSong : tracks) {
                            Log.d("Song Title ", currentSong.getSongTitle());
                            Log.d("Stream Url ", currentSong.getStreamUrl());
                        }

                    } else {
                        toastMsg("Error code " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<Track>> call, Throwable t) {
                    toastMsg("Network Error: " + t.getMessage());
                }
            });
        } else if (userInputStr.isEmpty()) {
            toastMsg("Please enter a searchable Track!");
        }
    }

    //TODO: clean up
    public void search(View view) {
        SearchView userInput = findViewById(R.id.searchView);
        String userInputStr = userInput.getQuery().toString();

        SoundCloudService scRetroService = SoundCloudAPI.getScService();

        if (!userInputStr.isEmpty()) {
            scRetroService.search(userInputStr).enqueue(new Callback<List<Track>>() {
                @Override
                public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                    if (response.isSuccessful()) {
                        List<Track> tracks = response.body();
                        loadTracks(tracks);
                        for (Track currentSong : tracks) {
                            Log.d("Song Title ", currentSong.getSongTitle());
                            Log.d("Stream Url ", currentSong.getStreamUrl());
                        }
                    } else {
                        toastMsg("Error code " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<Track>> call, Throwable t) {
                    toastMsg("Network Error: " + t.getMessage());
                }
            });
        } else if (userInputStr.isEmpty()) {
            toastMsg("Please enter a searchable Track!");
        }
    }

    public void toastMsg(String msg) {
        Toast.makeText(SearchList.this, msg, Toast.LENGTH_LONG).show();
    }

    private void initRecyclerView(){
        listItems = new ArrayList<Track>();
        recyclerView = findViewById(R.id.recyclerView);
        listAdapter = new RecyclerViewAdapter(this, listItems);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadTracks(List<Track> tracks){
        listItems.clear();
        listItems.addAll(tracks);
        listAdapter.notifyDataSetChanged();
    }

}
