package com.pi.gymapp.api;

import androidx.lifecycle.LiveData;

import com.pi.gymapp.api.models.ExecuteRoutine;
import com.pi.gymapp.api.models.FullReviewModel;
import com.pi.gymapp.api.models.ReviewModel;
import com.pi.gymapp.api.utils.ApiResponse;
import com.pi.gymapp.api.models.PagedList;
import com.pi.gymapp.api.models.RoutineModel;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiRoutineService {

    // Getters

    @GET("routines/{routineId}")
    LiveData<ApiResponse<RoutineModel>> getById(@Path("routineId") int routineId);

    @GET("routines/")
    LiveData<ApiResponse<PagedList<RoutineModel>>> getSlice(
            @Query("page") int page, @Query("size") int size
    );

    @GET("routines/")
    LiveData<ApiResponse<PagedList<RoutineModel>>> getAll(
            @Query("orderBy") String orderBy, @Query("direction") String direction
    );

    @POST("routines/{routineId}/executions")
    LiveData<ApiResponse<Void>> createExecution(@Path("routineId") int routineId, @Body ExecuteRoutine executeRoutine);

    @GET("routines/")
    LiveData<ApiResponse<PagedList<RoutineModel>>> getAll(
            @Query("orderBy") String orderBy, @Query("direction") String direction,
            @Query("difficulty") String difficulty
    );

    @GET("user/current/routines/favourites/")
    LiveData<ApiResponse<PagedList<RoutineModel>>> getFavourites();

    // Reviews and Ratings

    @GET("routines/{routineId}/ratings")
    LiveData<ApiResponse<PagedList<FullReviewModel>>> getReviews(@Path("routineId") int routineId, @Query("page") int page, @Query("size") int size);

    @POST("routines/{routineId}/ratings")
    LiveData<ApiResponse<FullReviewModel>> setReview(@Path("routineId") int routineId, @Body ReviewModel reviewModel);

    // Updaters

    @POST("user/current/routines/{routineId}/favourites")
    LiveData<ApiResponse<Void>> favourite(@Path("routineId") int routineId);

    @DELETE("user/current/routines/{routineId}/favourites")
    LiveData<ApiResponse<Void>> unfavourite(@Path("routineId") int routineId);
}
