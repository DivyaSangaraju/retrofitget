package com.example.tudaapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {




    @GET("getuserReceiptsByBillNoMobile")
    Call<List<DataAdapter>> getPosts(@Query("billNo") String billNo);


    @POST("saveuserreceipts")
    Call<Post> getUserData(@Body Post post);


    @POST("updateuserbilling")
    Call<Post> updateUserData(@Body Post post);
}
