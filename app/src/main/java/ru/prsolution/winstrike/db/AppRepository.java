package ru.prsolution.winstrike.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import ru.prsolution.winstrike.db.dao.UserDao;
import ru.prsolution.winstrike.db.entity.UserEntity;

public class AppRepository {
    private UserDao mUserDao;
    private LiveData<UserEntity> mUser;

    public AppRepository(AppDatabase db) {
//        AppDatabase db = AppDatabase.getInstance(application);
        mUserDao = db.userDao();
        mUser = mUserDao.loadUser();
    }

    public LiveData<UserEntity> getUser() {
        return mUser;
    }

    public void insert(UserEntity user) {
        new insertAsyncTask(mUserDao).execute(user);
    }

    private static class insertAsyncTask extends AsyncTask<UserEntity, Void, Void> {
        private UserDao mAsyncTaskDao;

        public insertAsyncTask(UserDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(UserEntity... userEntities) {
            mAsyncTaskDao.insertUser(userEntities[0]);
            return null;
        }
    }
}
