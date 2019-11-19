package com.mixr.Networking;

import com.mixr.Config.Config;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Project Name: Mixr
 * Pacakage Name: com.mixr
 * Date: 11/6/2019
 * Description: Singleton pattern to improve performance
 *
 * @Author Elias Afzalzada
 * Copyright © Elias Afzalzada - All Rights Reserved
 */
public class SoundCloudAPI {

    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final SoundCloudService SC_SERVICE = RETROFIT.create(SoundCloudService.class);

    public static SoundCloudService getScService() {
        return SC_SERVICE;
    }

}
