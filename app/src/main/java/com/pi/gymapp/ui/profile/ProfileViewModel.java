package com.pi.gymapp.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.pi.gymapp.api.models.UserChangeData;
import com.pi.gymapp.api.models.UserData;
import com.pi.gymapp.domain.User;
import com.pi.gymapp.repo.UserRepository;
import com.pi.gymapp.utils.AbsentLiveData;
import com.pi.gymapp.utils.Resource;
import com.pi.gymapp.utils.RepositoryViewModel;

public class ProfileViewModel extends RepositoryViewModel<UserRepository> {

    private LiveData<Resource<User>> user;
    private Integer userId;

    public ProfileViewModel(UserRepository repository) {
        super(repository);

    }

    public LiveData<Resource<User>> getUser() {
        return repository.getCurrent();
    }

    public LiveData<Resource<User>> sendUserChange(UserChangeData data) {
        return repository.updateCurrent(data);
    }
}