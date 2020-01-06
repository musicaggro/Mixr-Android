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
import com.mixr.Objects.Track;
import com.mixr.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, RecyclerViewAdapter.OnTrackListener {

    private List<Track> trackArrList;
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
        trackArrList = new ArrayList<Track>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        listAdapter = new RecyclerViewAdapter(trackArrList, this);
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

    // Watches search bar for user submitted string to start the call to Soundcloud
    @Override
    public boolean onQueryTextSubmit(String query) {
        // asynchronous call passing a callback function
        soundcloudService.search(query).enqueue(new Callback<List<Track>>() {

            // Successfully received HTTP response, acts based on response code
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                // Checks to see if response code is in 200-300 range
                if (response.isSuccessful()) {
                    // parses JSON response and converts to track objects thanks to retrofit
                    List<Track> tracks = response.body();
                    loadTracks(tracks);
                } else {
                    // HTTP hard error caught by retrofit passed to a toast message method
                    toastMsg("Error code " + response.code());
                }
            }

            // Catches network exceptions e.g. wrong base api url
            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                toastMsg("Network Error: " + t.getMessage());
            }
        });
        return false;
    }

    // Part of SearchViewListener used for predictive text when typing into search bar
    // currently not in use but could be used by storing user history and predicting text?
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    // Loads tracks into arraylist and notify's recyclerViews adapter
    private void loadTracks(List<Track> tracks) {
        trackArrList.clear();
        trackArrList.addAll(tracks);
        listAdapter.notifyDataSetChanged();
    }

    // Toast message used for debugging and displaying HTTP/Network errors
    public void toastMsg(String msg) {
        Toast.makeText(SearchListActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    // Listens for user selecting a track from list, then passes vars to startMusicPlayer
    // TODO: Possible refactor to send a track object instead of individual attributes
    @Override
    public void onTrackClick(int position) {
        startMusicPlayer(trackArrList.get(position).getSongStreamUrl(),
                trackArrList.get(position).getSongTitle(),
                trackArrList.get(position).getSongArtworkUrl(),
                trackArrList.get(position).getSongDuration());
    }

    // Starts new activity that plays passed track
    public void startMusicPlayer(String streamURl, String songTitle, String albumUrl, int duration) {
        Intent musicPlayerIntent = new Intent(this, MusicPlayerActivity.class);
        musicPlayerIntent.putExtra("streamUrl", streamURl);
        musicPlayerIntent.putExtra("albumUrl", albumUrl);
        musicPlayerIntent.putExtra("songTitle", songTitle);
        musicPlayerIntent.putExtra("songDuration", duration);
        startActivity(musicPlayerIntent);
    }
}