package com.pi.gymapp.repo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.pi.gymapp.api.ApiCategoryService;
import com.pi.gymapp.api.models.PagedList;
import com.pi.gymapp.api.models.CategoryModel;
import com.pi.gymapp.api.utils.ApiResponse;
import com.pi.gymapp.db.AppDatabase;
import com.pi.gymapp.db.entity.CategoryEntity;
import com.pi.gymapp.domain.Category;
import com.pi.gymapp.utils.AppExecutors;
import com.pi.gymapp.utils.NetworkBoundResource;
import com.pi.gymapp.utils.RateLimiter;
import com.pi.gymapp.utils.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

public class CategoryRepository {

    private static final String RATE_LIMITER_ALL_KEY = "all";

    private ApiCategoryService api;
    private AppDatabase db;

    private AppExecutors executors;
    private RateLimiter<String> rateLimit = new RateLimiter<>(1, TimeUnit.SECONDS);


    public CategoryRepository(AppExecutors executors, ApiCategoryService api, AppDatabase db) {
        this.executors = executors;
        this.api = api;
        this.db = db;
    }


    // ----------------------------------- Mappers -----------------------------------

    Category entityToDomain(CategoryEntity e){
        return new Category(e.getId(), e.getName());
    }

    CategoryEntity modelToEntity(CategoryModel m){
        return new CategoryEntity(m.getId(), m.getName());
    }

    Category modelToDomain(CategoryModel m){
        return new Category(m.getId(), m.getName());
    }


    // ----------------------------------- Methods -----------------------------------

    public LiveData<Resource<List<Category>>> getAll() {

        return new NetworkBoundResource<List<Category>, List<CategoryEntity>, PagedList<CategoryModel>>(
            executors,
            entities -> entities.stream().map(this::entityToDomain).collect(toList()),
            model -> model.getResults().stream().map(this::modelToEntity).collect(toList()),
            model -> model.getResults().stream().map(this::modelToDomain).collect(toList())
        ){
            @Override
            protected void saveCallResult(@NonNull List<CategoryEntity> entities) {
                db.categoryDao().deleteAll();
                db.categoryDao().insert(entities);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<CategoryEntity> entities) {
                return ((entities == null) || (entities.size() == 0))
                        || rateLimit.shouldFetch(RATE_LIMITER_ALL_KEY);
            }

            @Override
            protected boolean shouldPersist(@Nullable PagedList<CategoryModel> model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<CategoryEntity>> loadFromDb() {
                return db.categoryDao().getAll();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<CategoryModel>>> createCall() {
                return api.getAll();
            }
        }.asLiveData();

    }

}