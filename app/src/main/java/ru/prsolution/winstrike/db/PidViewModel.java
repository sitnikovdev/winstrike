package ru.prsolution.winstrike.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.db.entity.PidEntity;

public class PidViewModel extends AndroidViewModel {
    private LiveData<List<PidEntity>> mPids;

    @Inject
    AppRepository mRepository;

    public PidViewModel(@NonNull Application application) {
        super(application);
        WinstrikeApp.getInstance().getAppComponent().inject(this);
        mPids = mRepository.getPids();
    }

    public LiveData<List<PidEntity>> getPids() {
        return mPids;
    }

    public void insert(PidEntity pidEntity) {
        mRepository.insertPid(pidEntity);
    }

    public void delete(int pidEntity){mRepository.deletePid(pidEntity);}
}
