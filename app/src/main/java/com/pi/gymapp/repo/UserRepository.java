package com.pi.gymapp.repo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;


import com.pi.gymapp.api.ApiUserService;
import com.pi.gymapp.api.models.UserData;
import com.pi.gymapp.api.models.UserModel;
import com.pi.gymapp.api.utils.ApiResponse;
import com.pi.gymapp.db.AppDatabase;
import com.pi.gymapp.db.entity.UserEntity;
import com.pi.gymapp.domain.User;
import com.pi.gymapp.utils.AppExecutors;
import com.pi.gymapp.utils.NetworkBoundResource;
import com.pi.gymapp.utils.RateLimiter;
import com.pi.gymapp.utils.Resource;

import java.util.concurrent.TimeUnit;


public class UserRepository {

    private static final String RATE_LIMITER_ALL_KEY = "@@all@@";

    private ApiUserService api;
    private AppDatabase db;

    private AppExecutors executors;
    private final RateLimiter<String> rateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);


    public UserRepository(AppExecutors executors, ApiUserService api, AppDatabase db) {
        this.executors = executors;
        this.api = api;
        this.db = db;
    }


    // ----------------------------------- Mappers -----------------------------------

    private User entityToDomain(UserEntity e) {
        return new User(e.id, e.username, e.fullName, e.gender, e.birthdate, e.email, e.phone,
                e.avatarUrl, e.dateCreated, e.dateLastActive, e.deleted, e.verified);
    }

    private UserEntity modelToEntity(UserModel m) {
        return new UserEntity(m.getId(), m.getUsername(), m.getFullName(), m.getGender(),
                m.getBirthdate(), m.getEmail(), m.getPhone(), m.getAvatarUrl(), m.getDateCreated(),
                m.getDateLastActive(), m.isDeleted(), m.isVerified());
    }

    private User modelToDomain(UserModel m) {
        return new User(m.getId(), m.getUsername(), m.getFullName(), m.getGender(),
                m.getBirthdate(), m.getEmail(), m.getPhone(), m.getAvatarUrl(), m.getDateCreated(),
                m.getDateLastActive(), m.isDeleted(), m.isVerified());
    }


    // ----------------------------------- Methods -----------------------------------

    public LiveData<Resource<User>> getCurrent() {
        return new NetworkBoundResource<User, UserEntity, UserModel>(
                executors, this::entityToDomain, this::modelToEntity, this::modelToDomain
        ) {
            @Override
            protected void saveCallResult(@NonNull UserEntity entity) {
                db.userDao().delete();
                db.userDao().insert(entity);
            }

            @Override
            protected boolean shouldFetch(@Nullable UserEntity entity) {
                return (entity == null);
            }

            @Override
            protected boolean shouldPersist(@Nullable UserModel model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<UserEntity> loadFromDb() {
                return db.userDao().getCurrent();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<UserModel>> createCall() {
                return api.getCurrentUser();
            }
        }.asLiveData();
    }


    public LiveData<Resource<User>> updateCurrent(UserData userData) {
        return new NetworkBoundResource<User, UserEntity, UserModel>(
                executors, this::entityToDomain, this::modelToEntity, this::modelToDomain
        ) {
            @Override
            protected void saveCallResult(@NonNull UserEntity entity) {
                db.userDao().delete();
                db.userDao().insert(entity);
            }

            @Override
            protected boolean shouldFetch(@Nullable UserEntity entity) {
                return (entity != null);
            }

            @Override
            protected boolean shouldPersist(@Nullable UserModel model) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<UserEntity> loadFromDb() {
                return db.userDao().getCurrent();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<UserModel>> createCall() {
                return api.updateCurrentUser(userData);
            }
        }.asLiveData();
    }

}

