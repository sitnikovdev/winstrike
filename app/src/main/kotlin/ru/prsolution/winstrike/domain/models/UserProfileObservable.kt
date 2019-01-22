package ru.prsolution.winstrike.domain.models


import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class UserProfileObservable : BaseObservable() {
	@get:Bindable
	//        notifyPropertyChanged(name);
	var name: String? = null
}
