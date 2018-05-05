package ru.prsolution.winstrike.di.module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.prsolution.winstrike.db.AppDatabase;
import ru.prsolution.winstrike.db.AppRepository;

/**
 *
 */
@Module
public class AppRepositoryModule {
	private AppDatabase db;

	public AppRepositoryModule(Application context) {
		db = AppDatabase.getInstance(context);
	}


	@Provides
	@Singleton
	public AppRepository provideAppDatabase(){
         return new AppRepository(db);
	}
}
