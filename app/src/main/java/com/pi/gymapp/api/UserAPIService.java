package com.pi.gymapp.api;

import com.pi.gymapp.api.models.Credentials;
import com.pi.gymapp.api.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserAPIService {

    @POST("user/login")
    Call<String> logIn(@Body Credentials credentials);

    @POST("user/logout")
    Call<Void> logOut();

    @GET("user/current")
    Call<User> getCurrentUser();

    @GET("user/{id}")
    Call<User> getUser(@Path("id") String id);


}
