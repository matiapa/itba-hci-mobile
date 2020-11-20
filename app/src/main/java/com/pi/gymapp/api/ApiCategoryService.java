package com.pi.gymapp.api;

import androidx.lifecycle.LiveData;

import com.pi.gymapp.api.models.CategoryModel;
import com.pi.gymapp.api.models.PagedList;
import com.pi.gymapp.api.models.RoutineModel;
import com.pi.gymapp.api.utils.ApiResponse;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiCategoryService {

    // Getters

    @GET("categories/")
    LiveData<ApiResponse<PagedList<CategoryModel>>> getAll();

}
