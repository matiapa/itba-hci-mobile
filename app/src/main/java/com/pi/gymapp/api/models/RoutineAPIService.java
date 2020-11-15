package com.pi.gymapp.api.models;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RoutineAPIService {

    @GET("/user/current/routines/")
    Call<PagedList<Routine>> getOwnRoutines();

}
