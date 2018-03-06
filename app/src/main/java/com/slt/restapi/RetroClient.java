package com.slt.restapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.slt.utils.Constants.BASE_URL;
import static com.slt.utils.Constants.BASE_URL_ONLINE_SERVER;

/**
 * Retroclient
 */

public class RetroClient {

    /**
     * Get Retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL_ONLINE_SERVER)
                .addConverterFactory(GsonConverterFactory.create(gson))
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
