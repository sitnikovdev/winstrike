package ru.prsolution.winstrike.domain.models

/**
 * Created by designer on 07/03/2018.
 */


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable


class LoginViewModel : BaseObservable() {

	@SerializedName("username")
	@Expose
	@get:Bindable
	var username: String? = null
	@SerializedName("password")
	@Expose
	var password: String? = null
	@get:Bindable
	var loginMessage: String? = null
		private set

	fun loginSucceeded() {
		loginMessage = "Login Successful!!"
		//        notifyPropertyChanged(BR.loginMessage);
	}

	fun loginFailed() {
		loginMessage = "Login Failed!!"
		//        notifyPropertyChanged(BR.loginMessage);
	}

}

