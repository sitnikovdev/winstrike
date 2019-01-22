package ru.prsolution.winstrike.di.module;

import ru.prsolution.winstrike.subnavigation.LocalCiceroneHolder;

/**
 * Created by terrakok 24.11.16
 */

public class LocalNavigationModule {

    LocalCiceroneHolder provideLocalNavigationHolder() {
        return new LocalCiceroneHolder();
    }
}
