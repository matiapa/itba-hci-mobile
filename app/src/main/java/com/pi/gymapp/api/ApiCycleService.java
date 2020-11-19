package com.pi.gymapp.api;

import androidx.lifecycle.LiveData;

import com.pi.gymapp.api.models.CycleModel;
import com.pi.gymapp.api.models.PagedList;
import com.pi.gymapp.api.utils.ApiResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiCycleService {

    @GET("routines/{routineId}/cycles/{cycleId}/")
    LiveData<ApiResponse<CycleModel>> getCycle(@Path("routineId") int routineId, @Path("cycleId") int cycleId);

    @GET("routines/{routineId}/cycles/")
    LiveData<ApiResponse<PagedList<CycleModel>>> getCycles(@Path("routineId") int routineId);

}
