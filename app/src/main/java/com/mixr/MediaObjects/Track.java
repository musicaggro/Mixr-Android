package com.mixr.MediaObjects;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Project Name: Mixr
 * Package Name: com.mixr.Application
 * Date: 11/12/2019
 * Description: Track object that takes JSON and matches
 * it to the tracks appropriate fields via GSON.
 * <p>
 * Thoughts: I was going to use some form of gson.fromJson(soundcloudJSON, class)
 * but saw an example of how @serializedname is cleaner and a more flexible
 * alternative, json field name being different from class value name wont matter.
 *
 * @Author Elias Afzalzada
 * Copyright © Elias Afzalzada - All Rights Reserved
 */
public class Track implements Parcelable {

    @SerializedName("title")
    private String songTitle;

    @SerializedName("user_id")
    private String songUserID;

    @SerializedName("genre")
    private String songGenre;

    @SerializedName("description")
    private String songDescription;

    @SerializedName("duration")
    private int songDuration;

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

    protected Track(Parcel in) {
        songTitle = in.readString();
        songUserID = in.readString();
        songGenre = in.readString();
        songDescription = in.readString();
        songDuration = in.readInt();
        songTotalPlaybacks = in.readString();
        songTotalLikes = in.readString();
        songArtworkUrl = in.readString();
        songStreamUrl = in.readString();
        songCurrentState = in.readString();
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(songTitle);
        dest.writeString(songUserID);
        dest.writeString(songGenre);
        dest.writeString(songDescription);
        dest.writeInt(songDuration);
        dest.writeString(songTotalPlaybacks);
        dest.writeString(songTotalLikes);
        dest.writeString(songArtworkUrl);
        dest.writeString(songStreamUrl);
        dest.writeString(songCurrentState);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    public int getSongDuration() {
        return songDuration;
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
