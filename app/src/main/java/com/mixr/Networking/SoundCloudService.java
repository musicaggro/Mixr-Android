package com.mixr.Networking;

import com.mixr.Config.Config;
import com.mixr.Objects.Track;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Project Name: Mixr
 * Package Name: com.mixr.Networking
 * Date: 11/12/2019
 * Description: Using Retrofit this interface allows the declaration of methods that
 * represent Soundcloud api endpoints. Data is downloaded and parsed into a POJO.
 * <p>
 * SoundCloud HTTP API reference: https://developers.soundcloud.com/docs/api/reference#tracks
 *
 * @Author Elias Afzalzada
 * Copyright © Elias Afzalzada - All Rights Reserved
 */
public interface SoundCloudService {

    // The annotation describes how the method maps to an HTTP request
    @GET("/tracks?client_id=" + Config.CLIENT_ID)
    // The query tag allows use of specific filters set by Soundcloud to narrow search results
    Call<List<Track>> search(@Query("q") String userRequestedSearch);

    @GET("/tracks?client_id=" + Config.CLIENT_ID)
    Call<List<Track>> getRecentTracks(@Query("created_at") String date);

}
