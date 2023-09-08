package com.cyberclick.cbcakanisa.network;

import com.cyberclick.cbcakanisa.models.Articles;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {
    @GET("articles")
    Call<Object> getAllArticles();
}
