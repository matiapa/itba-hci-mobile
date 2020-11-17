package com.pi.gymapp.api;

import androidx.lifecycle.LiveData;


import com.pi.gymapp.api.models.Credentials;
import com.pi.gymapp.api.models.Email;
import com.pi.gymapp.api.models.Token;
import com.pi.gymapp.api.models.User;
import com.pi.gymapp.api.models.UserData;
import com.pi.gymapp.api.models.VerifyEmailData;

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

    @POST("user")
    LiveData<ApiResponse<User>> createUser(@Body UserData userData);

    @POST("user/verify_email")
    LiveData<ApiResponse<Void>> verifyEmail(@Body VerifyEmailData verifyEmailData);

    @POST("user/resend_verification")
    LiveData<ApiResponse<Void>> resendVerificationEmail(@Body Email email);
}
