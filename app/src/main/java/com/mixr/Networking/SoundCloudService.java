package com.mixr.Networking;

import com.mixr.Config.Config;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Project Name: Mixr
 * Package Name: com.mixr.Networking
 * Date: 11/12/2019
 * Description: Services offered by SoundCloud to call upon
 *
 * @Author Elias Afzalzada
 * Copyright © Elias Afzalzada - All Rights Reserved
 */
public interface SoundCloudService {

    @GET ("/tracks?client_id=" + Config.CLIENT_ID)
    Call<List<SoundTrack>> getRecentTracks(@Query("created_at") String dateTrackCreated);

    @GET ("/tracks?client_id=" + Config.CLIENT_ID)
    Call<List<SoundTrack>> search(@Query("q") String userRequestedSearch);

}
