package ru.prsolution.winstrike.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.db.entity.UserEntity;

public class PidViewModel extends AndroidViewModel {
    private LiveData<List<UserEntity>> mUsers;

    @Inject
    AppRepository mRepository;

    public PidViewModel(@NonNull Application application) {
        super(application);
        WinstrikeApp.getInstance().getAppComponent().inject(this);
        mUsers = mRepository.getUsersList();
    }

    public LiveData<List<UserEntity>> getUser() {
        return mUsers;
    }

    public void insert(UserEntity UserEntity) {
        mRepository.insertUser(UserEntity);
    }

    public void delete(){mRepository.deleteUser();}
}
