package com.pi.gymapp.ui.category;

import androidx.lifecycle.LiveData;

import com.pi.gymapp.api.utils.ApiResponse;
import com.pi.gymapp.domain.Category;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.repo.CategoryRepository;
import com.pi.gymapp.repo.RoutineRepository;
import com.pi.gymapp.utils.RepositoryViewModel;
import com.pi.gymapp.utils.Resource;

import java.util.List;

public class CategoryViewModel extends RepositoryViewModel<CategoryRepository> {

    public CategoryViewModel(CategoryRepository repository) {
        super(repository);
    }


    public LiveData<Resource<List<Category>>> getCategories() {
        return repository.getAll();
    }

}
