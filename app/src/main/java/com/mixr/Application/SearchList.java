package com.mixr.Application;


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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mixr.Config.Config;
import com.mixr.Networking.SoundCloudService;
import com.mixr.Networking.SoundTrack;
import com.mixr.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SoundCloudService scRetroService = retrofit.create(SoundCloudService.class);
        scRetroService.getRecentTracks("last_week").enqueue(new Callback<List<SoundTrack>>() {
            @Override
            public void onResponse(Call<List<SoundTrack>> call, Response<List<SoundTrack>> response) {
                if (response.isSuccessful()) {
                    List<SoundTrack> tracks = response.body();
                    toastMsg(tracks.get(0).getSongTitle());
                } else {
                    toastMsg("Error code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<SoundTrack>> call, Throwable t) {
                toastMsg("Network Error: " + t.getMessage());
            }
        });
    }

    public void toastMsg(String msg) {
        Toast.makeText(SearchList.this, msg, Toast.LENGTH_LONG).show();
    }

}
