object ApplicationId {
    val id = "ru.prsolution.winstrike"
}

object Modules {
    val app = ":app"
}

object Release {
    val versionCode = 50
    val versionName = "v1.50.7"
}

object Versions {
    val kotlin = "1.3.20"
    val gradle = "3.5.0-alpha02"
    val proguardGradle = "6.0.3"
    val gradleWrapper = "5.1.1-all"
    val targetSdk = 28
    val compileSdk = 28
    val minSdk = 21
    val googleServicies = "4.2.0"
    val firebase = "16.0.4"
    val fabric = "1.27.0"
    val appCompat = "28.0.0"
    val design = "1.0.0"
    val cardView = "1.0.0"
    val recyclerView = "1.0.0"

    val androidx = "1.0.2"
    val ktx = "1.0.0-alpha1"
    val dataBinding = "3.2.0-alpha10"

    val fresco = "1.11.0"
    val lottie = "2.5.4"
    val decoro = "1.1.1"
    val showhidepasswordedittext = "0.8"
    val materialCalendarView = "1.4.0"
    val ahbottomnavigation = "2.0.4"
    val gson = "2.8.5"
    val retrofit = "2.5.0"
    val retrofitRxjavaAdapter = "2.3.0"
    val converterGson = "2.5.0"
    val converterScalars = "2.1.0"
    val rxandroid = "1.2.1"
    val butterknife = "8.8.1"
    val timber = "4.7.1"
    val moxy = "1.5.5"
    val cicerone = "3.0.0"
    val dagger2 = "2.19"
    val firebaseMessaging = "17.3.4"
    val chuck = "1.1.0"
	val lifecycle = "1.1.1"

}


object Libraries {
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    val appCompat = "com.android.support:appcompat-v7:${Versions.appCompat}"
    val ktx = "androidx.core:core-ktx:${Versions.ktx}"
    val constraintLayout = "com.android.support.constraint:constraint-layout:1.1.3"
    val recyclerView = "com.android.support:recyclerview-v7:${Versions.appCompat}"
    val cardView = "com.android.support:cardview-v7:${Versions.appCompat}"
    val design = "com.android.support:design:${Versions.appCompat}"
    val fresco = "com.facebook.fresco:fresco:1.11.0"
    val lifecycle = "android.arch.lifecycle:extensions:${Versions.lifecycle}"

    val lottie = "com.airbnb.android:lottie:2.5.4"
    val decoro = "ru.tinkoff.decoro:decoro:1.1.1"
    val showhidepasswordedittext = "com.github.scottyab:showhidepasswordedittext:0.8"
    val materialCalendarView = "com.applandeo:material-calendar-view:1.4.0"
    val ahbottomnavigation = "com.aurelhubert:ahbottomnavigation:2.0.4"

    // networking
    val retrofit = "com.squareup.retrofit2:retrofit:2.5.0"
    val retrofitAdapterrxJava  = "com.squareup.retrofit2:adapter-rxjava:2.3.0"
    val retrofitGson = "com.squareup.retrofit2:converter-gson:2.5.0"
    val gson = "com.google.code.gson:gson:2.8.5"
    val retrofitConverterScalars = "com.squareup.retrofit2:converter-scalars:2.1.0"
    val retrofitCoroutines = "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2"
    val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.0"
    val moshi = "com.squareup.retrofit2:converter-moshi:2.5.0"

    val okhttp = "com.squareup.okhttp3:okhttp:3.12.0"
    val okhttpLoging = "com.squareup.okhttp3:logging-interceptor:3.12.0"

    val anko = "org.jetbrains.anko:anko:0.10.8"


    val rxAndroid = "io.reactivex:rxandroid:1.2.1"
    val butterKnife = "com.jakewharton:butterknife:8.8.1"
    val butterKnifeCompiler =  "com.jakewharton:butterknife-compiler:8.8.1"
    val timber =  "com.jakewharton.timber:timber:4.7.1"
    val moxyAppCompat =  "com.arello-mobile:moxy-app-compat:1.5.5"
    val moxy = "com.arello-mobile:moxy:1.5.5"
    val moxyCompiler = "com.arello-mobile:moxy-compiler:1.5.5"
    val cicerone = "ru.terrakok.cicerone:cicerone:3.0.0"
    val dagger = "com.google.dagger:dagger:2.19"
    val daggerCompiler = "com.google.dagger:dagger-compiler:2.19"
    val fireBase =  "com.google.firebase:firebase-core:16.0.6"
    val fireBaseMessaging = "com.google.firebase:firebase-messaging:17.3.4"

    // Loging
    val chuck = "com.readystatesoftware.chuck:library:1.1.0"
    val chuckRelease = "com.readystatesoftware.chuck:library-no-op:1.1.0"
}

object SupportLibraries {
    val androidx = "androidx.appcompat:appcompat:${Versions.androidx}"
}

object Constants {
    val DEBUGURL = "\"http://46.254.21.94:9000/api/v1/\""
    val BASEURL = "\"http://api.winstrike.ru:8000/api/v1/\""

}
