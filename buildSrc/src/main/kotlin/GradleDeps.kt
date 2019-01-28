object ApplicationId {
	val id = "ru.prsolution.winstrike"
}

object Release {
	val versionCode = 50
	val versionName = "v1.50.22"
}

object Constants {
	val DEBUGURL = "\"http://46.254.21.94:9000/api/v1/\""
	val BASEURL = "\"http://api.winstrike.ru:8000/api/v1/\""
}

object Modules {
	val app = ":app"
	val preference = ":preference"
}

object Versions {
	val kotlin = "1.3.20"
	val androidx = "1.0.2"
	val lifecycle = "1.1.1"
	val ktx = "1.0.0-alpha1"
	val gradle = "3.5.0-alpha02"

	val targetSdk = 28
	val compileSdk = 28
	val minSdk = 21

	val googleServicies = "4.2.0"

	val retrofit = "2.5.0"

	val fresco = "1.11.0"
	val lottie = "2.5.4"
	val decoro = "1.1.1"
	val showhidepasswordedittext = "0.8"

	val chuck = "1.1.0"
	val timber = "4.7.1"
	val fabric = "1.27.0"
	val firebase = "16.0.4"
	val firebaseMessaging = "17.3.4"
}


object Libraries {
	val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
	val androidx = "androidx.appcompat:appcompat:${Versions.androidx}"
	val ktx = "androidx.core:core-ktx:${Versions.ktx}"
	val lifecycle = "android.arch.lifecycle:extensions:${Versions.lifecycle}"

	val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0-alpha3"
	val fresco = "com.facebook.fresco:fresco:1.11.0"

	val lottie = "com.airbnb.android:lottie:2.5.4"
	val decoro = "ru.tinkoff.decoro:decoro:1.1.1"
	val showhidepasswordedittext = "com.github.scottyab:showhidepasswordedittext:0.8"

	val retrofit = "com.squareup.retrofit2:retrofit:2.5.0"
	val retrofitCoroutines = "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2"
	val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.0"
	val moshi = "com.squareup.retrofit2:converter-moshi:2.5.0"

	val okhttp = "com.squareup.okhttp3:okhttp:3.12.0"
	val okhttpLoging = "com.squareup.okhttp3:logging-interceptor:3.12.0"

	val anko = "org.jetbrains.anko:anko:0.10.8"

	val timber = "com.jakewharton.timber:timber:4.7.1"
	val fireBase = "com.google.firebase:firebase-core:16.0.6"
	val fireBaseMessaging = "com.google.firebase:firebase-messaging:17.3.4"

	val chuck = "com.readystatesoftware.chuck:library:1.1.0"
	val chuckRelease = "com.readystatesoftware.chuck:library-no-op:1.1.0"
}


