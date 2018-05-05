package ru.prsolution.winstrike.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.prsolution.winstrike.subnavigation.LocalCiceroneHolder;

/**
 * Created by terrakok 24.11.16
 */

@Module
public class LocalNavigationModule {

    @Provides
    @Singleton
    LocalCiceroneHolder provideLocalNavigationHolder() {
        return new LocalCiceroneHolder();
    }
}
