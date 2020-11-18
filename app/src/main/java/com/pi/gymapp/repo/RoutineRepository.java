package com.pi.gymapp.repo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.pi.gymapp.api.utils.ApiResponse;
import com.pi.gymapp.api.ApiRoutineService;
import com.pi.gymapp.api.models.PagedList;
import com.pi.gymapp.api.models.RoutineModel;
import com.pi.gymapp.db.AppDatabase;
import com.pi.gymapp.db.entity.RoutineEntity;
import com.pi.gymapp.utils.AppExecutors;
import com.pi.gymapp.utils.NetworkBoundResource;
import com.pi.gymapp.utils.RateLimiter;
import com.pi.gymapp.utils.Resource;
import com.pi.gymapp.domain.Routine;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

public class RoutineRepository {

    private static final String RATE_LIMITER_ALL_KEY = "all";
    private static final String RATE_LIMITER_FAV_KEY = "fav";

    private ApiRoutineService api;
    private AppDatabase db;

    private AppExecutors executors;
    private RateLimiter<String> rateLimit = new RateLimiter<>(1, TimeUnit.SECONDS);


    public RoutineRepository(AppExecutors executors, ApiRoutineService api, AppDatabase db) {
        this.executors = executors;
        this.api = api;
        this.db = db;
    }


    // ----------------------------------- Methods -----------------------------------

    public LiveData<Resource<List<Routine>>> getRoutineSlice(int page, int size) {

        return new NetworkBoundResource<List<Routine>, List<RoutineEntity>, PagedList<RoutineModel>>(
                executors,
                entities -> entities.stream().map(
                        e -> new Routine(e.id, e.title, e.rate, null)
                ).collect(toList()),
                model -> model.getResults().stream().map(
                        m -> new RoutineEntity(m.getId(), m.getName(), m.getAverageRating(), null)
                ).collect(toList()),
                model -> model.getResults().stream().map(
                        m -> new Routine(m.getId(), m.getName(), m.getAverageRating(), null)
                ).collect(toList())
        ){
            @Override
            protected void saveCallResult(@NonNull List<RoutineEntity> entities) {
                db.routineDao().deleteSlice(size, page * size);
                db.routineDao().insert(entities);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<RoutineEntity> entities) {
                return ((entities == null) || (entities.size() == 0))
                        || rateLimit.shouldFetch(RATE_LIMITER_ALL_KEY);
            }

            @Override
            protected boolean shouldPersist(@Nullable PagedList<RoutineModel> model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<RoutineEntity>> loadFromDb() {
                return db.routineDao().getPage(size, page * size);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<RoutineModel>>> createCall() {
                return api.getSlice(page, size);
            }
        }.asLiveData();

    }


    public LiveData<Resource<List<Routine>>> getAllRoutines() {

        return new NetworkBoundResource<List<Routine>, List<RoutineEntity>, PagedList<RoutineModel>>(
                executors,
                entities -> entities.stream().map(
                        e -> new Routine(e.id, e.title, e.rate, null)
                ).collect(toList()),
                model -> model.getResults().stream().map(
                        m -> new RoutineEntity(m.getId(), m.getName(), m.getAverageRating(), null)
                ).collect(toList()),
                model -> model.getResults().stream().map(
                        m -> new Routine(m.getId(), m.getName(), m.getAverageRating(), null)
                ).collect(toList())
        ){
            @Override
            protected void saveCallResult(@NonNull List<RoutineEntity> entities) {
                db.routineDao().deletAll();
                db.routineDao().insert(entities);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<RoutineEntity> entities) {
                return ((entities == null) || (entities.size() == 0))
                        || rateLimit.shouldFetch(RATE_LIMITER_ALL_KEY);
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


    public LiveData<Resource<Routine>> getRoutineById(int routineId) {
        return new NetworkBoundResource<Routine, RoutineEntity, RoutineModel>(
            executors,
            e -> new Routine(e.id, e.title, e.rate, null),
            m -> new RoutineEntity(m.getId(), m.getName(), m.getAverageRating(), null),
            m -> new Routine(m.getId(), m.getName(), m.getAverageRating(), null)
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


    public LiveData<Resource<List<Routine>>> getFavourites() {

        return new NetworkBoundResource<List<Routine>, List<RoutineEntity>, PagedList<RoutineModel>>(
                executors,

                entities -> entities.stream().map(
                        e -> new Routine(e.id, e.title, e.rate, true)
                ).collect(toList()),

                model -> model.getResults().stream().map(
                        m -> new RoutineEntity(m.getId(), m.getName(), m.getAverageRating(), true)
                ).collect(toList()),

                model -> model.getResults().stream().map(
                        m -> new Routine(m.getId(), m.getName(), m.getAverageRating(), true)
                ).collect(toList())
        ){
            @Override
            protected void saveCallResult(@NonNull List<RoutineEntity> entities) {
                db.routineDao().deleteFavs();
                db.routineDao().insert(entities);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<RoutineEntity> entities) {
                return ((entities == null) || (entities.size() == 0))
                        || rateLimit.shouldFetch(RATE_LIMITER_FAV_KEY);
            }

            @Override
            protected boolean shouldPersist(@Nullable PagedList<RoutineModel> model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<RoutineEntity>> loadFromDb() {
                return db.routineDao().getFavs();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<RoutineModel>>> createCall() {
                return api.getFavourites();
            }
        }.asLiveData();

    }

}