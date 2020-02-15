package com.mixr.Activities;

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

import com.mixr.Adapters.RecyclerViewAdapter;
import com.mixr.MediaObjects.Track;
import com.mixr.Networking.SoundCloudAPI;
import com.mixr.Networking.SoundCloudService;
import com.mixr.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Project Name: Mixr
 * Date: 11/6/2019
 * Description: Activity that is the "main menu" of the app. It displays a default list of
 * recently uploaded tracks from SoundCloud and allows users to search for songs via a SearchView.
 * Requested songs then get sent via Retrofit, received as a JSON that's converted via GSON,
 * then passed to a RecyclerView with a custom adapter that displays a list of tracks.
 * Selecting a track starts a new activity that preps asynchronously for playback.
 *
 * @Author Elias Afzalzada
 * Copyright © Elias Afzalzada - All Rights Reserved
 */

public class SearchListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, RecyclerViewAdapter.OnTrackListener {

    private List<Track> trackArrList;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter listAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;
    private SoundCloudService soundcloudService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list_layout);
        initSoundCloudService();
        initRecyclerView();
    }

    private void initSoundCloudService() {
        soundcloudService = SoundCloudAPI.getSoundcloudService();
    }

    // RecyclerView initialization
    private void initRecyclerView() {
        trackArrList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        // Avoids expensive checks of item containers size being changed
        recyclerView.setHasFixedSize(true);

        // Sets layout style in this case LinearLayout for Views in ViewHolders
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Sets custom adapter I have it set to take an onClickListener specified in this class
        listAdapter = new RecyclerViewAdapter(trackArrList, this);
        recyclerView.setAdapter(listAdapter);

        // Populates RecyclerView with recently uploaded tracks on app start
        loadListOfDefaultRecentTracks();
    }

    // Replaces default menu with custom menu and sets SearchView with custom listener
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    // SearchView listener for user song searches, starts call to SoundCloud
    @Override
    public boolean onQueryTextSubmit(String query) {
        // Asynchronous call passing a callback function
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
                    toastMsg("HTTP code " + response.code());
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

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    // TODO: Idea of switching to SoundCloud trending tracks to display on app start
    // API doesn't contain a direct way of getting top tracks
    // https://stackoverflow.com/questions/35688367/access-soundcloud-charts-with-api
    private void loadListOfDefaultRecentTracks() {
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
    private void toastMsg(String msg) {
        Toast.makeText(SearchListActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    // Custom recyclerView item listener that gets selected track then passes object to an intent
    @Override
    public void onTrackClick(int position) {
        Track selectedTrack = trackArrList.get(position);
        Intent musicPlayerIntent = new Intent(this, MusicPlayerActivity.class);
        musicPlayerIntent.putExtra("selectedTrack", selectedTrack);
        startActivity(musicPlayerIntent);
    }

}