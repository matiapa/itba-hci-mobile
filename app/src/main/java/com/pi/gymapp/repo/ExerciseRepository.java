package com.pi.gymapp.repo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.pi.gymapp.api.ApiExerciseService;
import com.pi.gymapp.api.ApiUserService;
import com.pi.gymapp.api.models.ExerciseModel;
import com.pi.gymapp.api.models.PagedList;
import com.pi.gymapp.api.models.RoutineModel;
import com.pi.gymapp.api.models.UserModel;
import com.pi.gymapp.api.utils.ApiResponse;
import com.pi.gymapp.db.AppDatabase;
import com.pi.gymapp.db.entity.ExerciseEntity;
import com.pi.gymapp.db.entity.RoutineEntity;
import com.pi.gymapp.db.entity.UserEntity;
import com.pi.gymapp.domain.Exercise;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.domain.User;
import com.pi.gymapp.utils.AppExecutors;
import com.pi.gymapp.utils.NetworkBoundResource;
import com.pi.gymapp.utils.RateLimiter;
import com.pi.gymapp.utils.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

public class ExerciseRepository {
    private static final String RATE_LIMITER_ALL_KEY = "@@all@@";

    private ApiExerciseService api;
    private AppDatabase db;

    private AppExecutors executors;
    private RateLimiter<String> rateLimit = new RateLimiter<>(1, TimeUnit.SECONDS);


    public ExerciseRepository(AppExecutors executors, ApiExerciseService api, AppDatabase db) {
        this.executors = executors;
        this.api = api;
        this.db = db;
    }



    // ----------------------------------- Methods -----------------------------------

    public LiveData<Resource<List<Exercise>>> getExerciseSlice(int page, int size,int routineId,int cycleId) {

        return new NetworkBoundResource<List<Exercise>, List<ExerciseEntity>, PagedList<ExerciseModel>>(
                executors,
                entities -> entities.stream().map(
                        e -> new Exercise(e.id,e.routineId,e.cycleId,e.name,e.detail,e.duration,e.repetitions,e.order)
                ).collect(toList()),
                model -> model.getResults().stream().map(
                        m -> new ExerciseEntity(m.getId(),routineId,cycleId,m.getName(),m.getDetail(),m.getDuration(),m.getRepetitions(),m.getOrder())
                ).collect(toList()),
                model -> model.getResults().stream().map(
                        m -> new Exercise(m.getId(),routineId,cycleId,m.getName(),m.getDetail(),m.getDuration(),m.getRepetitions(),m.getOrder())
                ).collect(toList())
        ){
            @Override
            protected void saveCallResult(@NonNull List<ExerciseEntity> entities) {
                db.exerciseDao().deleteSlice(routineId,cycleId,size, page * size);
                db.exerciseDao().insert(entities);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ExerciseEntity> entities) {
                return ((entities == null) || (entities.size() == 0))
                        || rateLimit.shouldFetch(RATE_LIMITER_ALL_KEY);
            }

            @Override
            protected boolean shouldPersist(@Nullable PagedList<ExerciseModel> model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<ExerciseEntity>> loadFromDb() {
                return db.exerciseDao().getPage(routineId,cycleId,size, page * size);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<ExerciseModel>>> createCall() {
                return api.getSlice(routineId,cycleId,page, size);
            }
        }.asLiveData();

    }

    public LiveData<Resource<List<Exercise>>> getAll(int routineId,int cycleId) {

        return new NetworkBoundResource<List<Exercise>, List<ExerciseEntity>, PagedList<ExerciseModel>>(
                executors,
                entities -> entities.stream().map(
                        e -> new Exercise(e.id,e.routineId,e.cycleId,e.name,e.detail,e.duration,e.repetitions,e.order)
                ).collect(toList()),
                model -> model.getResults().stream().map(
                        m -> new ExerciseEntity(m.getId(),routineId,cycleId,m.getName(),m.getDetail(),m.getDuration(),m.getRepetitions(),m.getOrder())
                ).collect(toList()),
                model -> model.getResults().stream().map(
                        m -> new Exercise(m.getId(),routineId,cycleId,m.getName(),m.getDetail(),m.getDuration(),m.getRepetitions(),m.getOrder())
                ).collect(toList())
        ){
            @Override
            protected void saveCallResult(@NonNull List<ExerciseEntity> entities) {
                db.exerciseDao().deleteAll(routineId,cycleId);
                db.exerciseDao().insert(entities);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ExerciseEntity> entities) {
                return ((entities == null) || (entities.size() == 0))
                        || rateLimit.shouldFetch(RATE_LIMITER_ALL_KEY);
            }

            @Override
            protected boolean shouldPersist(@Nullable PagedList<ExerciseModel> model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<ExerciseEntity>> loadFromDb() {
                return db.exerciseDao().getAll(routineId,cycleId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<ExerciseModel>>> createCall() {
                return api.getAll(routineId,cycleId);
            }
        }.asLiveData();

    }

    public LiveData<Resource<Exercise>> getExerciseById(int routineId,int cycleId, int id) {
        return new NetworkBoundResource<Exercise, ExerciseEntity, ExerciseModel>(
                executors,
                e -> new Exercise(e.id,e.routineId,e.cycleId,e.name,e.detail,e.duration,e.repetitions,e.order),
                m -> new ExerciseEntity(m.getId(),routineId,cycleId,m.getName(),m.getDetail(),m.getDuration(),m.getRepetitions(),m.getOrder()),
                m -> new Exercise(m.getId(),routineId,cycleId,m.getName(),m.getDetail(),m.getDuration(),m.getRepetitions(),m.getOrder())
        ) {
            @Override
            protected void saveCallResult(@NonNull ExerciseEntity entity) {
                db.exerciseDao().insert(entity);
            }

            @Override
            protected boolean shouldFetch(@Nullable ExerciseEntity entity) {
                return (entity == null);
            }

            @Override
            protected boolean shouldPersist(@Nullable ExerciseModel model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<ExerciseEntity> loadFromDb() {
                return db.exerciseDao().getById(routineId,cycleId,id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ExerciseModel>> createCall() {
                return api.getById(routineId,cycleId,id);
            }
        }.asLiveData();
    }




}

