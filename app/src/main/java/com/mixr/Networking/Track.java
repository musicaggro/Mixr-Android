package com.mixr.Networking;


import com.google.gson.annotations.SerializedName;

/**
 * Project Name: Mixr
 * Package Name: com.mixr.Application
 * Date: 11/12/2019
 * Description: Track object that takes JSON and matches
 * it to the tracks appropriate fields via GSON.
 *
 * Thoughts: I was going to use some form of gson.fromJson(soundcloudJSON, class)
 * but saw an example of how @serializedname is cleaner and a more flexible
 * alternative, json field name being different from class value name wont matter.
 *
 * @Author Elias Afzalzada
 * Copyright © Elias Afzalzada - All Rights Reserved
 */
public class Track {

    @SerializedName("title")
    private String songTitle;

    @SerializedName("user_id")
    private String songUserID;

    @SerializedName("genre")
    private String songGenre;

    @SerializedName("description")
    private String songDescription;

    @SerializedName("duration")
    private String songTotalTime;

    @SerializedName("playback_count")
    private String songTotalPlaybacks;

    @SerializedName("favoritings_count")
    private String songTotalLikes;

    @SerializedName("artwork_url")
    private String songArtworkUrl;

    @SerializedName("stream_url")
    private String songStreamUrl;

    @SerializedName("state")
    private String songCurrentState;

    public String getSongTitle() {
        return songTitle;
    }

    public String getSongUserID() {
        return songUserID;
    }

    public String getSongGenre() {
        return songGenre;
    }

    public String getSongDescription() {
        return songDescription;
    }

    public String getSongTotalTime() {
        return songTotalTime;
    }

    public String getSongTotalPlaybacks() {
        return songTotalPlaybacks;
    }

    public String getSongTotalLikes() {
        return songTotalLikes;
    }

    public String getSongArtworkUrl() {
        return songArtworkUrl;
    }

    public String getSongStreamUrl() {
        return songStreamUrl;
    }

    public String getSongCurrentState() {
        return songCurrentState;
    }
}
