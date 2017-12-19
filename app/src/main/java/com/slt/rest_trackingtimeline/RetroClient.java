package com.slt.rest_trackingtimeline;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Usman Ahmad on 20.12.2017.
 */

public class RetroClient {
    /********
     * URLS
     *******/
    private static final String ROOT_URL = "http://pratikbutani.x10.mx";

    /**
     * Get Retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Get API Service
     *
     * @return API Service
     */
    public static Endpoints getApiService() {
        return getRetrofitInstance().create(Endpoints.class);
    }
}
