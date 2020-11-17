package com.pi.gymapp.repo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.pi.gymapp.api.utils.ApiResponse;
import com.pi.gymapp.api.ApiRoutineService;
import com.pi.gymapp.api.models.PagedList;
import com.pi.gymapp.api.models.RoutineModel;
import com.pi.gymapp.db.AppDatabase;
import com.pi.gymapp.utils.AppExecutors;
import com.pi.gymapp.utils.NetworkBoundResource;
import com.pi.gymapp.utils.RateLimiter;
import com.pi.gymapp.utils.Resource;
import com.pi.gymapp.db.entity.RoutineEntity;
import com.pi.gymapp.domain.Routine;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

public class RoutineRepository {

    private static final String RATE_LIMITER_ALL_KEY = "@@all@@";

    private ApiRoutineService api;
    private AppDatabase db;

    private AppExecutors executors;
    private RateLimiter<String> rateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);


    public RoutineRepository(AppExecutors executors, ApiRoutineService api, AppDatabase db) {
        this.executors = executors;
        this.api = api;
        this.db = db;
    }
    
    // ----------------------------------- Mappers ----------------------------------- 

    private Routine entityToDomain(RoutineEntity entity) {
        return new Routine(entity.id, entity.title, entity.rate, entity.isFav);
    }

    private RoutineEntity modelToEntity(RoutineModel model) {
        // Will assume the routine is not favourite, so the property has to be changed if it is
        return new RoutineEntity(model.getId(), model.getName(), model.getAverageRating(), false);
    }

    private Routine modelToDomain(RoutineModel model) {
        // Will assume the routine is not favourite, so the property has to be changed if it is
        return new Routine(model.getId(), model.getName(), model.getAverageRating(), false);
    }

    // ----------------------------------- Methods ----------------------------------- 

    public LiveData<Resource<List<Routine>>> getAll() {

        return new NetworkBoundResource<List<Routine>, List<RoutineEntity>, PagedList<RoutineModel>>(
                executors,
                entities -> entities.stream().map(this::entityToDomain).collect(toList()),
                model -> model.getResults().stream().map(this::modelToEntity).collect(toList()),
                model -> model.getResults().stream().map(this::modelToDomain).collect(toList())
        ) {
            @Override
            protected void saveCallResult(@NonNull List<RoutineEntity> entities) {
                db.routineDao().deleteAll();
                db.routineDao().insert(entities);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<RoutineEntity> entities) {
                return ((entities == null) || (entities.size() == 0)
                        || rateLimit.shouldFetch(RATE_LIMITER_ALL_KEY));
            }

            @Override
            protected boolean shouldPersist(@Nullable PagedList<RoutineModel> model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<RoutineEntity>> loadFromDb() {
                return db.routineDao().getAll();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<RoutineModel>>> createCall() {
                return api.getAll();
            }
        }.asLiveData();

    }

    public LiveData<Resource<List<Routine>>> getSlice(int page, int size) {

        return new NetworkBoundResource<List<Routine>, List<RoutineEntity>, PagedList<RoutineModel>>(
                executors,
                entities -> entities.stream().map(this::entityToDomain).collect(toList()),
                model -> model.getResults().stream().map(this::modelToEntity).collect(toList()),
                model -> model.getResults().stream().map(this::modelToDomain).collect(toList())
        ){
            @Override
            protected void saveCallResult(@NonNull List<RoutineEntity> entities) {
                db.routineDao().deleteSlice(size, page * size);
                db.routineDao().insert(entities);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<RoutineEntity> entities) {
                return ((entities == null) || (entities.size() == 0));
            }

            @Override
            protected boolean shouldPersist(@Nullable PagedList<RoutineModel> model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<RoutineEntity>> loadFromDb() {
                return db.routineDao().getSlice(size, page * size);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<RoutineModel>>> createCall() {
                return api.getSlice(page, size);
            }
        }.asLiveData();
    }
    
    public LiveData<Resource<Routine>> getById(int routineId) {
        return new NetworkBoundResource<Routine, RoutineEntity, RoutineModel>(
            executors, this::entityToDomain, this::modelToEntity, this::modelToDomain
        ) {
            @Override
            protected void saveCallResult(@NonNull RoutineEntity entity) {
                db.routineDao().insert(entity);
            }

            @Override
            protected boolean shouldFetch(@Nullable RoutineEntity entity) {
                return (entity == null);
            }

            @Override
            protected boolean shouldPersist(@Nullable RoutineModel model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<RoutineEntity> loadFromDb() {
                return db.routineDao().getById(routineId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RoutineModel>> createCall() {
                return api.getById(routineId);
            }
        }.asLiveData();
    }
    
}
