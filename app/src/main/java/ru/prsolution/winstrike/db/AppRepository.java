package ru.prsolution.winstrike.db;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import ru.prsolution.winstrike.db.dao.UserDao;
import ru.prsolution.winstrike.db.entity.UserEntity;

public class AppRepository {
    private UserDao mUserDao;
    private LiveData<List<UserEntity>> mUser;

    public AppRepository(AppDatabase db) {
//        AppDatabase db = AppDatabase.getInstance(application);
        mUserDao = db.userDao();
        mUser = mUserDao.loadAllUsers();
    }

    public LiveData<List<UserEntity>> getUsersList() {
        return mUser;
    }

    public void insert(UserEntity user) {
        new insertAsyncTask(mUserDao).execute(user);
    }

    public void delete() {
        new deleteAsyncTask(mUserDao).execute();
    }

    private static class insertAsyncTask extends android.os.AsyncTask<UserEntity, Void, Void> {
        private UserDao mAsyncTaskDao;

        public insertAsyncTask(UserDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(final UserEntity... userEntities) {
            mAsyncTaskDao.insertUser(userEntities[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<UserEntity, Void, Void> {
        private UserDao mAsyncTaskDao;

        public deleteAsyncTask(UserDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(final UserEntity... userEntities) {
            mAsyncTaskDao.deleteAllUsers();
            return null;
        }
    }
}
