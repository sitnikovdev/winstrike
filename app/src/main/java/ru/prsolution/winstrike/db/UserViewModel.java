package ru.prsolution.winstrike.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;


import javax.inject.Inject;

import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.db.entity.UserEntity;

public class UserViewModel extends AndroidViewModel {
    private LiveData<UserEntity> mUser;

    @Inject
    AppRepository mRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        WinstrikeApp.getInstance().getAppComponent().inject(this);
        //mRepository = new AppRepository(application);
        mUser = mRepository.getUser();
    }

    public LiveData<UserEntity> getUser() {
        return mUser;
    }

    public void insert(UserEntity UserEntity) {
        mRepository.insert(UserEntity);
    }
}
