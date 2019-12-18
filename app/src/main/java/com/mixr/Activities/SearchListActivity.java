package com.mixr.Activities;


/**
 * Project Name: Mixr
 * Date: 11/6/2019
 * Description: Search List activity to allow user menu_layout and display in a recycler view.
 *
 * @Author Elias Afzalzada
 * Copyright © Elias Afzalzada - All Rights Reserved
 */

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mixr.Networking.SoundCloudAPI;
import com.mixr.Networking.SoundCloudService;
import com.mixr.Networking.Track;
import com.mixr.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, RecyclerViewAdapter.OnTrackListener {

    private List<Track> listItems;
    private RecyclerViewAdapter listAdapter;
    private RecyclerView recyclerView;
    private MenuItem searchMenuItem;
    private SearchView searchView;
    private SoundCloudService soundcloudService = SoundCloudAPI.getSoundcloudService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list_layout);
        initRecyclerView();
    }

    private void initRecyclerView() {
        listItems = new ArrayList<Track>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        listAdapter = new RecyclerViewAdapter(listItems, this);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout, menu);

        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        soundcloudService.search(query).enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful()) {
                    List<Track> tracks = response.body();

                    //TODO: check tracks/values received noted users dont fill all values
                    for(Track track: tracks){
                        Log.e("JSON BODY",track.getSongTitle() + "\n"
                                + track.getSongUserID() + "\n"
                                + track.getSongGenre() + "\n"
                                + track.getSongDescription() + "\n"
                                + track.getSongTotalDuration() + "\n"
                                + track.getSongTotalPlaybacks() + "\n"
                                + track.getSongTotalLikes() + "\n"
                                + track.getSongArtworkUrl() + "\n"
                                + track.getSongStreamUrl() + "\n"
                                + track.getSongCurrentState() + "\n\n");
                    }


                    loadTracks(tracks);
                } else {
                    toastMsg("Error code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                toastMsg("Network Error: " + t.getMessage());
            }
        });
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void loadTracks(List<Track> tracks) {
        listItems.clear();
        listItems.addAll(tracks);
        listAdapter.notifyDataSetChanged();
    }

    public void toastMsg(String msg) {
        Toast.makeText(SearchListActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTrackClick(int position) {
        musicPlayerIntent(listItems.get(position).getSongStreamUrl(),
                listItems.get(position).getSongTitle(),
                listItems.get(position).getSongArtworkUrl());
    }

    public void musicPlayerIntent(String streamURl, String songTitle, String albumUrl) {
        Intent musicPlayerIntent = new Intent(this, MusicPlayerActivity.class);
        musicPlayerIntent.putExtra("streamUrl", streamURl);
        musicPlayerIntent.putExtra("albumUrl", albumUrl);
        musicPlayerIntent.putExtra("songTitle", songTitle);
        startActivity(musicPlayerIntent);
    }
}