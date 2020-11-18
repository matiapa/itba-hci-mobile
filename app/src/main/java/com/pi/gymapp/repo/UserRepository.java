package com.pi.gymapp.repo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.pi.gymapp.api.ApiRoutineService;
import com.pi.gymapp.api.ApiUserService;
import com.pi.gymapp.api.models.PagedList;
import com.pi.gymapp.api.models.RoutineModel;
import com.pi.gymapp.api.models.UserModel;
import com.pi.gymapp.api.utils.ApiResponse;
import com.pi.gymapp.db.AppDatabase;
import com.pi.gymapp.db.entity.RoutineEntity;
import com.pi.gymapp.db.entity.UserEntity;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.domain.User;
import com.pi.gymapp.utils.AppExecutors;
import com.pi.gymapp.utils.NetworkBoundResource;
import com.pi.gymapp.utils.RateLimiter;
import com.pi.gymapp.utils.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

public class UserRepository {

    private static final String RATE_LIMITER_ALL_KEY = "@@all@@";

    private ApiUserService api;
    private AppDatabase db;

    private AppExecutors executors;
    private RateLimiter<String> rateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);


    public UserRepository(AppExecutors executors, ApiUserService api, AppDatabase db) {
        this.executors = executors;
        this.api = api;
        this.db = db;
    }

    // ----------------------------------- Mappers -----------------------------------

    private User entityToDomain(UserEntity entity) {
        return new User(entity.id, entity.username, entity.fullName, entity.gender,entity.birthdate,entity.email,entity.phone,entity.avatarUrl,entity.dateCreated,entity.dateLastActive,entity.deleted,entity.verified);
    }

    private UserEntity modelToEntity(UserModel model) {
        // Will assume the routine is not favourite, so the property has to be changed if it is
        return new UserEntity(model.getId(), model.getUsername(), model.getFullName(), model.getGender(),model.getBirthdate(),model.getEmail(),model.getPhone(),model.getAvatarUrl(),model.getDateCreated(),model.getDateLastActive(),model.isDeleted(),model.isVerified());
    }

    private User modelToDomain(UserModel model) {
        // Will assume the routine is not favourite, so the property has to be changed if it is
        return new User(model.getId(), model.getUsername(), model.getFullName(), model.getGender(),model.getBirthdate(),model.getEmail(),model.getPhone(),model.getAvatarUrl(),model.getDateCreated(),model.getDateLastActive(),model.isDeleted(),model.isVerified());
    }

    // ----------------------------------- Methods -----------------------------------





    public LiveData<Resource<User>> getById(int userId) {
        return new NetworkBoundResource<User, UserEntity, UserModel>(
                executors, this::entityToDomain, this::modelToEntity, this::modelToDomain
        ) {
            @Override
            protected void saveCallResult(@NonNull UserEntity entity) {
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
                return db.userDao().getById(userId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<UserModel>> createCall() {
                return api.getById(userId);
            }
        }.asLiveData();
    }
//

}

