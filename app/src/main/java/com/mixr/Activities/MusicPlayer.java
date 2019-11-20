package com.mixr.Activities;

/**
 * Project Name: Mixr
 * Date: 11/6/2019
 * Description: Music Player activity to play received song from Search List activity
 *
 * @Author Elias Afzalzada
 * Copyright © Elias Afzalzada - All Rights Reserved
 */

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mixr.Config.Config;
import com.mixr.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MusicPlayer extends AppCompatActivity {

    // UI elements
    private ImageView albmuCoverIV;
    private SeekBar seekBar;
    private TextView currentSongTimeTV;
    private TextView totalSongTimeTV;
    private ImageButton playPauseButtonIB;

    //MediaPlayer linked to local song for now
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        findViewIds();
        mediaPlayer();
        seekBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void findViewIds() {
        albmuCoverIV = findViewById(R.id.albumCover);
        seekBar = findViewById(R.id.seekBar);
        currentSongTimeTV = findViewById(R.id.currentSongTime);
        totalSongTimeTV = findViewById(R.id.totalSongTime);
        playPauseButtonIB = findViewById(R.id.playButton);
    }

    public void mediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());
        setSong();
    }

    public void setSong() {
        Intent intent = getIntent();
        String streamUrl = intent.getStringExtra("streamUrl");
        String albumUrl = intent.getStringExtra("albumUrl");

        try {
            Picasso.get().load(albumUrl).into(albmuCoverIV);
            mediaPlayer.setDataSource(streamUrl + "?client_id=" + Config.CLIENT_ID);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void seekBar() {
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // thread to update seekbar and song time stamps
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }) {
        }.start();
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            int currentTimeNumb = msg.what;
            seekBar.setProgress(currentTimeNumb);

            String currentTimeStr = songTimeConversion(currentTimeNumb);
            currentSongTimeTV.setText(currentTimeStr);

            String totalTimeStr = songTimeConversion(mediaPlayer.getDuration());
            totalSongTimeTV.setText(totalTimeStr);
        }
    };

    public String songTimeConversion(int time) {
        String timeStr = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeStr = min + ":";
        if (sec < 10) {
            timeStr += "0";

        }
        timeStr += sec;
        return timeStr;
    }


    public void playSong(View view) {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            playPauseButtonIB.setImageResource(R.drawable.pause);
        } else {
            mediaPlayer.pause();
            playPauseButtonIB.setImageResource(R.drawable.play);
        }


        //else {
        //mediaPlayer.pause();
        //playPauseButtonIB.setImageResource(R.drawable.play);
        //}
    }



}
