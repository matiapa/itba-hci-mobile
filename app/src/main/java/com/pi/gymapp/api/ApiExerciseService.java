package com.pi.gymapp.api;

import androidx.lifecycle.LiveData;

import com.pi.gymapp.api.models.ExerciseModel;
import com.pi.gymapp.api.models.PagedList;

import com.pi.gymapp.api.utils.ApiResponse;


import retrofit2.http.GET;

import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiExerciseService {

    @GET("routines/{routineId}/cycles/{cycleId}/exercises/{exerciseId}")
    LiveData<ApiResponse<ExerciseModel>> getById(@Path("routineId") int routineId, @Path("cycleId") int cycleId,@Path("exerciseId") int exerciseId);

    @GET("routines/{routineId}/cycles/{cycleId}/exercises/")
    LiveData<ApiResponse<PagedList<ExerciseModel>>> getSlice(@Path("routineId") int routineId, @Path("cycleId") int cycleId,@Query("page") int page, @Query("size") int size);

    @GET("routines/{routineId}/cycles/{cycleId}/exercises/")
    LiveData<ApiResponse<PagedList<ExerciseModel>>> getAll(@Path("routineId") int routineId, @Path("cycleId") int cycleId);


}
