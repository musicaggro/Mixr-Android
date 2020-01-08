package com.mixr.Networking;

import com.mixr.Config.Config;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Project Name: Mixr
 * Pacakage Name: com.mixr
 * Date: 11/6/2019
 * Description: Singleton pattern class to improve performance
 * by having static Retrofit and SoundCloudService objects.
 *
 * @Author Elias Afzalzada
 * Copyright © Elias Afzalzada - All Rights Reserved
 */
public class SoundCloudAPI {

    // REST client library used to create HTTP requests and process responses from a REST API
    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl(Config.BASE_API_URL)
            // adds json converter used for serialization and deserialization of objects
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Implements API interface then uses polymorphism to turn it into an object that makes requests
    private static final SoundCloudService SC_SERVICE = RETROFIT.create(SoundCloudService.class);

    public static SoundCloudService getSoundcloudService() {
        return SC_SERVICE;
    }

}
