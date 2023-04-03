package com.candra.kulinernusantara.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetroServer
{
    private static final String baseURL = "https://mywebsiteif.000webhostapp.com/";
    private static Retrofit retrofit;

    // Method Koneksi Retrofit
    public static Retrofit koneksiRetrofit()
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
