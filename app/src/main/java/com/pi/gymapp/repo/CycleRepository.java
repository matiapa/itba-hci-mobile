package com.pi.gymapp.repo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.pi.gymapp.api.ApiCycleService;
import com.pi.gymapp.api.models.PagedList;
import com.pi.gymapp.api.models.CycleModel;
import com.pi.gymapp.api.utils.ApiResponse;
import com.pi.gymapp.db.AppDatabase;
import com.pi.gymapp.db.entity.CycleEntity;
import com.pi.gymapp.domain.Cycle;
import com.pi.gymapp.utils.AppExecutors;
import com.pi.gymapp.utils.NetworkBoundResource;
import com.pi.gymapp.utils.RateLimiter;
import com.pi.gymapp.utils.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

public class CycleRepository {

    private static final String RATE_LIMITER_ALL_KEY = "all";

    private ApiCycleService api;
    private AppDatabase db;

    private AppExecutors executors;
    private RateLimiter<String> rateLimit = new RateLimiter<>(5, TimeUnit.MINUTES);


    public CycleRepository(AppExecutors executors, ApiCycleService api, AppDatabase db) {
        this.executors = executors;
        this.api = api;
        this.db = db;
    }


    // ----------------------------------- Mappers -----------------------------------

    Cycle entityToDomain(CycleEntity e, int routineId){
        return new Cycle(e.getId(), routineId, e.getName(), e.getDetail(), e.getRepetitions(),
                e.getOrder(), e.getType());
    }

    CycleEntity modelToEntity(CycleModel m, int routineId){
        return new CycleEntity(m.getId(), routineId, m.getName(), m.getDetail(), m.getRepetitions(),
                m.getOrder(), m.getType());
    }

    Cycle modelToDomain(CycleModel m, int routineId){
        return new Cycle(m.getId(), routineId, m.getName(), m.getDetail(), m.getRepetitions(),
                m.getOrder(), m.getType());
    }


    // ----------------------------------- Methods -----------------------------------

    public LiveData<Resource<List<Cycle>>> getCycles(int routineId) {

        return new NetworkBoundResource<List<Cycle>, List<CycleEntity>, PagedList<CycleModel>>(
            executors,
            entities -> entities.stream().map(e -> entityToDomain(e, routineId)).collect(toList()),
            model -> model.getResults().stream().map(m -> modelToEntity(m, routineId)).collect(toList()),
            model -> model.getResults().stream().map(m -> modelToDomain(m, routineId)).collect(toList())
        ){
            @Override
            protected void saveCallResult(@NonNull List<CycleEntity> entities) {
                db.cycleDao().deleteCycles(routineId);
                db.cycleDao().insert(entities);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<CycleEntity> entities) {
                return ((entities == null) || (entities.size() == 0))
                        || rateLimit.shouldFetch(RATE_LIMITER_ALL_KEY);
            }

            @Override
            protected boolean shouldPersist(@Nullable PagedList<CycleModel> model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<CycleEntity>> loadFromDb() {
                return db.cycleDao().getCycles(routineId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<CycleModel>>> createCall() {
                return api.getCycles(routineId);
            }
        }.asLiveData();

    }


    public LiveData<Resource<Cycle>> getCycleById(int routineId, int cycleId) {
        return new NetworkBoundResource<Cycle, CycleEntity, CycleModel>(
            executors,
            e -> entityToDomain(e, routineId),
            m -> modelToEntity(m, routineId),
            m -> modelToDomain(m, routineId)
        ) {
            @Override
            protected void saveCallResult(@NonNull CycleEntity entity) {
                db.cycleDao().insert(entity);
            }

            @Override
            protected boolean shouldFetch(@Nullable CycleEntity entity) {
                return (entity == null);
            }

            @Override
            protected boolean shouldPersist(@Nullable CycleModel model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<CycleEntity> loadFromDb() {
                return db.cycleDao().getCycle(routineId, cycleId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<CycleModel>> createCall() {
                return api.getCycle(routineId, cycleId);
            }
        }.asLiveData();
    }

}