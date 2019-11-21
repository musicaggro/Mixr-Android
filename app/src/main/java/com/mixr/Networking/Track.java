package com.mixr.Networking;


import com.google.gson.annotations.SerializedName;

/**
 * Project Name: Mixr
 * Package Name: com.mixr.Application
 * Date: 11/12/2019
 * Description: Track object that receives a Java object from GSON to deserialize into values
 *
 * @Author Elias Afzalzada
 * Copyright © Elias Afzalzada - All Rights Reserved
 */
public class Track {

    @SerializedName("title")
    private String songTitle;

    @SerializedName("id")
    private String songId;

    @SerializedName("stream_url")
    private String streamUrl;

    @SerializedName("artwork_url")
    private String songArtworkUrl;


    public String getSongTitle() {
        return songTitle;
    }

    public String getSongId() {
        return songId;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public String getSongArtworkUrl() {
        return songArtworkUrl;
    }

}
