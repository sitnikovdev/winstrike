/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.prsolution.winstrike.db;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import ru.prsolution.winstrike.db.converter.DateConverter;
import ru.prsolution.winstrike.db.dao.PidDao;
import ru.prsolution.winstrike.db.dao.TokenDao;
import ru.prsolution.winstrike.db.dao.UserDao;
import ru.prsolution.winstrike.db.entity.PidEntity;
import ru.prsolution.winstrike.db.entity.TokenEntity;
import ru.prsolution.winstrike.db.entity.UserEntity;

@Database(entities = {UserEntity.class, PidEntity.class, TokenEntity.class}, version = 2, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "winstrike-db";

    public abstract TokenDao tokenDao();

    public abstract UserDao userDao();

    public abstract PidDao pidDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `pid` (`id` INTEGER NOT NULL, "
                    + "`publickId` TEXT, PRIMARY KEY(`id`))");
        }
    };



    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context,
                            AppDatabase.class, DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return sInstance;
    }

}
