package ru.prsolution.winstrike.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.db.entity.UserEntity;

public class UserViewModel extends AndroidViewModel {
    private final List<UserEntity> mUsersList;
    private LiveData<List<UserEntity>> mUsers;

    @Inject
    AppRepository mRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        WinstrikeApp.getInstance().getAppComponent().inject(this);
        mUsers = mRepository.getUsersList();
        mUsersList = mRepository.getUsers();
    }

    public LiveData<List<UserEntity>> getUser() {
        return mUsers;
    }

    public List<UserEntity> loadUsers() {
        return this.mUsersList;
    }

    public void insert(UserEntity UserEntity) {
        mRepository.insertUser(UserEntity);
    }

    public void delete() {
        mRepository.deleteUser();
    }
}
