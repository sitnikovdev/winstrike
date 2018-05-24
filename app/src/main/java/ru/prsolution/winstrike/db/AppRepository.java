package ru.prsolution.winstrike.db;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import ru.prsolution.winstrike.db.dao.PidDao;
import ru.prsolution.winstrike.db.dao.UserDao;
import ru.prsolution.winstrike.db.entity.PidEntity;
import ru.prsolution.winstrike.db.entity.UserEntity;

public class AppRepository {
    private UserDao mUserDao;
    private LiveData<List<UserEntity>> mUser;
    private List<UserEntity> mUsers;

    private PidDao mPidDao;
    private LiveData<List<PidEntity>> mPids;

    public AppRepository(AppDatabase db) {
        mUserDao = db.userDao();
        mUser = mUserDao.loadAllUsers();
        mUsers = mUserDao.loadUsers();

        mPidDao = db.pidDao();
        mPids = mPidDao.loadAllPids();
    }

    public LiveData<List<UserEntity>> getUsersList() {
        return mUser;
    }

    public  List<UserEntity> getUsers() {
        return this.mUsers;
    }

    public LiveData<List<PidEntity>> getPids() {
        return mPids;
    }

    public void insertPid(PidEntity pid) {
        new insertPidAsyncTask(mPidDao).execute(pid);
    }

    private static class insertPidAsyncTask extends android.os.AsyncTask<PidEntity, Void, Void> {
        private PidDao mAsyncTaskDao;

        public insertPidAsyncTask(PidDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(PidEntity... pidEntities) {
            mAsyncTaskDao.insertPid(pidEntities[0]);
            return null;
        }
    }

    public void deletePid(int pidEntity) {
        new deletePidAsyncTask(mPidDao).execute();
    }

    private static class deleteUserAsyncTask extends AsyncTask<UserEntity, Void, Void> {
        private UserDao mAsyncTaskDao;

        public deleteUserAsyncTask(UserDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(final UserEntity... userEntities) {
            mAsyncTaskDao.deleteAllUsers();
            return null;
        }
    }


    public void insertUser(UserEntity user) {
        new insertUserAsyncTask(mUserDao).execute(user);
    }

    public void deleteUser() {
        new deleteUserAsyncTask(mUserDao).execute();
    }

    private static class insertUserAsyncTask extends android.os.AsyncTask<UserEntity, Void, Void> {
        private UserDao mAsyncTaskDao;

        public insertUserAsyncTask(UserDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(final UserEntity... userEntities) {
            mAsyncTaskDao.insertUser(userEntities[0]);
            return null;
        }
    }

    private static class deletePidAsyncTask extends AsyncTask<Integer, Void, Void> {
        private PidDao mAsyncTaskDao;

        public deletePidAsyncTask(PidDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(Integer... pids) {
            mAsyncTaskDao.deletePid(pids[0]);
            return null;
        }

    }
}
