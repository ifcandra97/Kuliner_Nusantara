package com.candra.kulinernusantara.API;

import com.candra.kulinernusantara.Model.ModelResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIRequestData
{

    @GET("retrieve.php")
    Call<ModelResponse> ardRetrieve();

    @FormUrlEncoded
    @POST("create.php")
    Call<ModelResponse> ardCreate(
            @Field("nama") String nama,
            @Field("asal") String asal,
            @Field("deskripsi_singkat") String deskripsiSingkat
    );

    @FormUrlEncoded
    @POST("delete.php")
    Call<ModelResponse> ardDelete(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("update.php")
    Call<ModelResponse> ardUpdate(
            @Field("id") int id,
            @Field("nama") String nama,
            @Field("asal") String asal,
            @Field("deskripsi_singkat") String deskripsiSingkat
    );
}
