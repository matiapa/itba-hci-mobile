package com.pi.gymapp.api;

import androidx.lifecycle.LiveData;


import com.pi.gymapp.api.models.Credentials;
import com.pi.gymapp.api.models.Token;
import com.pi.gymapp.api.models.User;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiUserService {
    @POST("user/login")
    LiveData<ApiResponse<Token>> login(@Body Credentials credentials);

    @POST("user/logout")
    LiveData<ApiResponse<Void>> logout();

    @GET("user/current")
    LiveData<ApiResponse<User>> getCurrentUser();
}
