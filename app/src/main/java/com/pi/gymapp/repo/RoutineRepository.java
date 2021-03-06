package com.pi.gymapp.repo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.pi.gymapp.api.models.FullReviewModel;
import com.pi.gymapp.api.models.ReviewModel;
import com.pi.gymapp.api.utils.ApiResponse;
import com.pi.gymapp.api.ApiRoutineService;
import com.pi.gymapp.api.models.PagedList;
import com.pi.gymapp.api.models.RoutineModel;
import com.pi.gymapp.db.AppDatabase;
import com.pi.gymapp.db.entity.ReviewEntity;
import com.pi.gymapp.db.entity.RoutineEntity;
import com.pi.gymapp.domain.Review;
import com.pi.gymapp.utils.AbsentLiveData;
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
    private RateLimiter<String> rateLimit = new RateLimiter<>(5, TimeUnit.MINUTES);;


    public RoutineRepository(AppExecutors executors, ApiRoutineService api, AppDatabase db) {
        this.executors = executors;
        this.api = api;
        this.db = db;
    }


    // ----------------------------------- Mappers -----------------------------------

    Routine entityToDomain(RoutineEntity e, Boolean isFav){
        return new Routine(e.getId(), e.getName(), e.getDetail(), e.getRate(), e.getDifficulty(),
                e.getCategoryId(), e.getCategoryName(), e.getDateCreated(), isFav);
    }

    RoutineEntity modelToEntity(RoutineModel m, Boolean isFav){
        return new RoutineEntity(m.getId(), m.getName(), m.getDetail(), m.getAverageRating(), m.getDifficulty(),
                m.getCategory().getId(), m.getCategory().getName(), m.getDateCreated(), isFav);
    }

    Routine modelToDomain(RoutineModel m, Boolean isFav){
        return new Routine(m.getId(), m.getName(), m.getDetail(), m.getAverageRating(), m.getDifficulty(),
                m.getCategory().getId(), m.getCategory().getName(), m.getDateCreated(), isFav);
    }


    // ----------------------------------- Methods -----------------------------------

    public LiveData<Resource<List<Routine>>> getRoutineSlice(int page, int size) {

        return new NetworkBoundResource<List<Routine>, List<RoutineEntity>, PagedList<RoutineModel>>(
                executors,
                entities -> entities.stream().map(e -> entityToDomain(e, null)).collect(toList()),
                model -> model.getResults().stream().map(m -> modelToEntity(m, null)).collect(toList()),
                model -> model.getResults().stream().map(m -> modelToDomain(m, null)).collect(toList())
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


    public LiveData<Resource<List<Routine>>> getAllRoutines(String orderBy, String direction, String difficulty) {

        return new NetworkBoundResource<List<Routine>, List<RoutineEntity>, PagedList<RoutineModel>>(
            executors,
            entities -> entities.stream().map(e -> entityToDomain(e, null)).collect(toList()),
            model -> model.getResults().stream().map(m -> modelToEntity(m, null)).collect(toList()),
            model -> model.getResults().stream().map(m -> modelToDomain(m, null)).collect(toList())
        ){
            @Override
            protected void saveCallResult(@NonNull List<RoutineEntity> entities) {
                db.routineDao().deleteAll();
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
                if(difficulty != null)
                    return api.getAll(orderBy, direction, difficulty);
                else
                    return api.getAll(orderBy, direction);
            }
        }.asLiveData();

    }

    
    public LiveData<Resource<List<Routine>>> getAllRoutines(String orderBy, String direction){
        return getAllRoutines(orderBy, direction, null);
    }


    public LiveData<Resource<Routine>> getRoutineById(int routineId) {
        return new NetworkBoundResource<Routine, RoutineEntity, RoutineModel>(
            executors,
            e -> entityToDomain(e, null),
            m -> modelToEntity(m, null),
            m -> modelToDomain(m, null)
        ) {
            @Override
            protected void saveCallResult(@NonNull RoutineEntity entity) {
                db.routineDao().deleteById(entity.getId());
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
                entities -> entities.stream().map(e -> entityToDomain(e, true)).collect(toList()),
                model -> model.getResults().stream().map(m -> modelToEntity(m, true)).collect(toList()),
                model -> model.getResults().stream().map(m -> modelToDomain(m, true)).collect(toList())
        ){
            @Override
            protected void saveCallResult(@NonNull List<RoutineEntity> entities) {
                db.routineDao().deleteFavs();

                entities.forEach(e -> db.routineDao().deleteById(e.getId()));
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

    
    public LiveData<Boolean> fetchIsFav(int routineId){
        return Transformations.map(api.getFavourites(), res ->{
            boolean isFav = res.getData().getResults().stream().anyMatch(r -> r.getId() == routineId);

            executors.diskIO().execute(() -> {
                db.routineDao().setFav(routineId, isFav);
            });

            return isFav;
        });
    }

    
    public LiveData<ApiResponse<Void>> setFav(int routineId, boolean value) {
        executors.diskIO().execute(() ->
                db.routineDao().setFav(routineId, value)
        );

        if(value)
            return api.favourite(routineId);
        else
            return api.unfavourite(routineId);
    }



    // ----------------------------------- Mappers -----------------------------------

    Review reviewEntityToDomain(ReviewEntity e){
        return new Review(e.getId(), e.getDate(), e.getScore(), e.getReview(), e.getRoutineId());
    }

    ReviewEntity reviewModelToEntity(FullReviewModel m){
        return new ReviewEntity(m.getId(), m.getDate(), m.getScore(), m.getReview(), m.getRoutine().getId());
    }

    Review reviewModelToDomain(FullReviewModel m){
        return new Review(m.getId(), m.getDate(), m.getScore(), m.getReview(), m.getRoutine().getId());
    }


    // ----------------------------------- Methods -----------------------------------

    public LiveData<Resource<List<Review>>> getReviews(int routineId) {
        return new NetworkBoundResource<List<Review>, List<ReviewEntity>, PagedList<FullReviewModel>>(
            executors,
            entities -> entities.stream().map(this::reviewEntityToDomain).collect(toList()),
            model -> model.getResults().stream().map(this::reviewModelToEntity).collect(toList()),
            model -> model.getResults().stream().map(this::reviewModelToDomain).collect(toList())
        ){
            @Override
            protected void saveCallResult(@NonNull List<ReviewEntity> entities) {
                db.reviewDao().deleteByRoutineId(routineId);
                db.reviewDao().insert(entities);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ReviewEntity> entities) {
                return ((entities == null) || (entities.size() == 0))
                        || rateLimit.shouldFetch(RATE_LIMITER_FAV_KEY);
            }

            @Override
            protected boolean shouldPersist(@Nullable PagedList<FullReviewModel> model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<ReviewEntity>> loadFromDb() {
                return db.reviewDao().getByRoutineId(routineId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PagedList<FullReviewModel>>> createCall() {
                return api.getReviews(routineId);
            }
        }.asLiveData();
    }


    public LiveData<Resource<Review>> postReview(int routineId, ReviewModel review) {
        return new NetworkBoundResource<Review, ReviewEntity, FullReviewModel>(
            executors,
            this::reviewEntityToDomain,
            this::reviewModelToEntity,
            this::reviewModelToDomain
        ){
            int reviewId = -1;

            @Override
            protected void saveCallResult(@NonNull ReviewEntity entities) {
                reviewId = entities.getId();
                db.reviewDao().insert(entities);
            }

            @Override
            protected boolean shouldFetch(@Nullable ReviewEntity entities) {
                return true;
            }

            @Override
            protected boolean shouldPersist(@Nullable FullReviewModel model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<ReviewEntity> loadFromDb() {
                if (reviewId == -1)
                    return AbsentLiveData.create();
                else
                    return db.reviewDao().getById(reviewId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<FullReviewModel>> createCall() {
                return api.postReview(routineId, review);
            }
        }.asLiveData();
    }
    
}