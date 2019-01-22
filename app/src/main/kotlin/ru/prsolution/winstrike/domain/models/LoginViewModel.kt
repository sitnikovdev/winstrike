package ru.prsolution.winstrike.domain.models

/**
 * Created by designer on 07/03/2018.
 */


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class LoginViewModel {

	@SerializedName("username")
	@Expose
	var username: String? = null
	@SerializedName("password")
	@Expose
	var password: String? = null
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

