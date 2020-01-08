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
        trackArrList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        // Avoids expensive size checks of items being added/removed
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new RecyclerViewAdapter(trackArrList, this);
        recyclerView.setAdapter(listAdapter);

        // Populates recyclerview with recently uploaded tracks on app start
        loadListOfDefaultRecentTracks();
    }

    // Sets custom menu and binds searchview with custom listener
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout, menu);

        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    // SearchView listener for user song searches, starts call to SoundCloud
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
        searchView.clearFocus();
        return true;
    }

    //TODO: currently not in use but could be used by storing user search history and predicting
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    // TODO: Idea of switching to SoundCloud trending tracks to display on app start
    // API doesn't contain a direct way of getting top tracks
    // https://stackoverflow.com/questions/35688367/access-soundcloud-charts-with-api
    public void loadListOfDefaultRecentTracks() {
        soundcloudService.getRecentTracks("last_two_weeks").enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful()) {
                    List<Track> tracks = response.body();
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
    }

    // Loads received tracks into RecyclerViews list
    private void loadTracks(List<Track> tracks) {
        trackArrList.clear();
        trackArrList.addAll(tracks);
        listAdapter.notifyDataSetChanged();
    }

    // Toast message used for debugging and displaying HTTP/Network errors
    public void toastMsg(String msg) {
        Toast.makeText(SearchListActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    // Custom recyclerView item listener that gets selected track info and passes it to an intent
    // TODO: Possible refactor to send a track object instead of individual attributes
    @Override
    public void onTrackClick(int position) {
        startMusicPlayer(trackArrList.get(position).getSongStreamUrl(),
                trackArrList.get(position).getSongTitle(),
                trackArrList.get(position).getSongArtworkUrl(),
                trackArrList.get(position).getSongDuration());
    }

    // Receives track information to start track playback in a new activity
    public void startMusicPlayer(String streamURl, String songTitle, String albumUrl, int duration) {
        Intent musicPlayerIntent = new Intent(this, MusicPlayerActivity.class);
        musicPlayerIntent.putExtra("streamUrl", streamURl);
        musicPlayerIntent.putExtra("albumUrl", albumUrl);
        musicPlayerIntent.putExtra("songTitle", songTitle);
        musicPlayerIntent.putExtra("songDuration", duration);
        startActivity(musicPlayerIntent);
    }
}