package ru.prsolution.winstrike.mvp.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import ru.prsolution.winstrike.BR;

public class UserProfileObservable extends BaseObservable {
    private String name;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }
}
