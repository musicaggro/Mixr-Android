package com.mixr.Activities;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mixr.MediaObjects.Track;
import com.mixr.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Project Name: Mixr
 * Date: 11/6/2019
 * Description: Media Player Activity Receives a track object from
 * main activity (SearchListActivity) then proceeds to set up the activities views
 * using the track objects passed information. MediaPlayer is setup and given a
 * stream url from the track object and a callback is used to determine when its
 * ready for playback. When MediaPlayer has finished loading, the song auto plays
 * and the user may use the seekbar to scrub through specific times in the song.
 *
 * @Author Elias Afzalzada
 * Copyright © Elias Afzalzada - All Rights Reserved
 */

public class MusicPlayerActivity extends AppCompatActivity {

    // UI elements
    private ImageView albumCoverIV;
    private SeekBar seekBar;
    private TextView currentSongTimeTV;
    private TextView totalSongTimeTV;
    private ImageButton playPauseButtonIB;
    private TextView songTitleTV;

    // SoundCloud Track Object
    private Track selectedTrack;

    // Music player object
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player_layout);
        initMediaPlayerViews();
        setTrackInfoViews();
        setMediaPlayer();
    }

    // Releases MediaPlayer when the activity is destroyed
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

    // Grabs Views for updating track info
    public void initMediaPlayerViews() {
        albumCoverIV = findViewById(R.id.albumCover);
        seekBar = findViewById(R.id.seekBar);
        currentSongTimeTV = findViewById(R.id.currentSongTime);
        totalSongTimeTV = findViewById(R.id.totalSongTime);
        songTitleTV = findViewById(R.id.songTitle);
        playPauseButtonIB = findViewById(R.id.playButton);
    }

    // Sets track info views on media player activity screen
    public void setTrackInfoViews(){
        selectedTrack = getIntent().getParcelableExtra("selectedTrack");

        // Sets song title view
        songTitleTV.setText(selectedTrack.getSongTitle());

        // Sets song total time view
        int songDuration = selectedTrack.getSongDuration();
        String songTotalDuration = trackTimeConversion(songDuration);
        totalSongTimeTV.setText(songTotalDuration);


        // Check to see if an album image is provided if not a default one is set
        if (selectedTrack.getSongAlbumUrl() != null) {
            Picasso.get().load(selectedTrack.getSongAlbumUrl()).into(albumCoverIV);
        } else {
            Picasso.get().load(R.drawable.default_album_image).into(albumCoverIV);
        }
    }

    // Sets up MediaPlayer and Sets Source Stream
    public void setMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());
        try {
            mediaPlayer.setDataSource(selectedTrack.getSongStreamUrl());
            mediaPlayer.prepareAsync();
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        // MediaPlayer callback when finished loading
        // starts playing song, sets seekbar, and an
        // OnClick listener for the play/pause button
        mediaPlayer.setOnPreparedListener(mp -> {
            toggleTrackPlayPause();
            setSeekBar();
            playPauseButtonIB.setOnClickListener(v -> toggleTrackPlayPause());
        });
        mediaPlayer.setOnCompletionListener(mp -> playPauseButtonIB.setImageResource(R.drawable.play));
    }

    // Toggles mediaPlayer play or pause state
    public void toggleTrackPlayPause() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            playPauseButtonIB.setImageResource(R.drawable.pause);
        } else {
            mediaPlayer.pause();
            playPauseButtonIB.setImageResource(R.drawable.play);
        }
    }

    // SeekBar allows user to scrub through playback forward, back, etc.
    public void setSeekBar() {
        seekBar.setMax(selectedTrack.getSongDuration());
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

        // Thread to update SeekBar position as song is played
        new Thread(() -> {
            while (mediaPlayer != null) {
                try {
                    Message msg = new Message();
                    msg.what = mediaPlayer.getCurrentPosition();
                    handler.sendMessage(msg);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Log.e("MusicPlayerActivity", "Thread Error", e);
                }
            }
        }) {
        }.start();
    }

    // Updates of current song time view
    Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            int currentTimeNum = msg.what;
            seekBar.setProgress(currentTimeNum);

            String currentTimeStr = trackTimeConversion(currentTimeNum);
            currentSongTimeTV.setText(currentTimeStr);
        }
    };

    // Converts track time from Milliseconds to minutes:seconds
    public String trackTimeConversion(int time) {
        String timeStr;
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeStr = min + ":";
        if (sec < 10) {
            timeStr += "0";
        }

        timeStr += sec;
        return timeStr;
    }

}