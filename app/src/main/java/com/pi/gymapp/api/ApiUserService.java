package com.pi.gymapp.api;

import androidx.lifecycle.LiveData;


import com.pi.gymapp.api.models.RoutineModel;
import com.pi.gymapp.api.models.UserChangeData;
import com.pi.gymapp.api.models.UserModel;
import com.pi.gymapp.api.utils.ApiResponse;
import com.pi.gymapp.api.models.Credentials;
import com.pi.gymapp.api.models.Email;
import com.pi.gymapp.api.models.Token;
import com.pi.gymapp.api.models.UserData;
import com.pi.gymapp.api.models.VerifyEmailData;
import com.pi.gymapp.domain.User;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiUserService {
    @POST("user/login")
    LiveData<ApiResponse<Token>> login(@Body Credentials credentials);

    @POST("user/logout")
    LiveData<ApiResponse<Void>> logout();

    @GET("user/current")
    LiveData<ApiResponse<UserModel>> getCurrentUser();

    @PUT("user/current")
    LiveData<ApiResponse<UserModel>> updateCurrentUser(@Body UserChangeData userData);

    @POST("user")
    LiveData<ApiResponse<UserModel>> createUser(@Body UserData userData);

    @POST("user/verify_email")
    LiveData<ApiResponse<Void>> verifyEmail(@Body VerifyEmailData verifyEmailData);

    @POST("user/resend_verification")
    LiveData<ApiResponse<Void>> resendVerificationEmail(@Body Email email);
}
