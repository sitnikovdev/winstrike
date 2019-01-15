plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "ru.prsolution.winstrike"
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 50
        versionName = "1.50.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }


    buildTypes {
        getByName("debug") {
            buildConfigField("String", "DEGUGURL", "\"http://46.254.21.94:9000/api/v1/\"")
//            signingConfig signingConfigs.debug
        }
        getByName("release") {
            buildConfigField("String", "BASEURL", "\"http://api.winstrike.ru:8000/api/v1/\"")
            isMinifyEnabled = false
//            signingConfig signingConfigs.release
        }
    }
/*    signingConfigs {
        release
        debug
    }*/
/*    lintOptions {
        checkReleaseBuilds false
    }*/

    compileOptions {
        setSourceCompatibility(1.8)
        setTargetCompatibility(1.8)
    }

    dataBinding {
        isEnabled = true
    }

}


dependencies {
    /** kotlin */
    implementation(Libraries.kotlin)

    /** Android AppCompat*/
    implementation(Libraries.appCompat)

    /**  constraint layout */
    implementation(Libraries.constraintLayout)

    /** recyclerview */
    implementation(Libraries.recyclerView)

    /** cardview */
    implementation(Libraries.cardView)

    /** material desing */
    implementation(Libraries.design)

    /** fresco */
    implementation(Libraries.fresco)

    /** lottie */
    implementation(Libraries.lottie)

    /** phone mask */
    implementation(Libraries.decoro)

    /** password */
    implementation(Libraries.showhidepasswordedittext)

    /** calendar */
    implementation(Libraries.materialCalendarView)

    /** bottom navigation bar */
    implementation(Libraries.ahbottomnavigation)

    /** gson */
    implementation(Libraries.gson)

    /** Retrofit & OkHttp */
    implementation(Libraries.retrofit)
    implementation(Libraries.retrofitAdapterrxJava)
    implementation(Libraries.retrofitGson)
    implementation(Libraries.retrofitConverterScalars)

    /** Rx Java 1 */
    implementation(Libraries.rxAndroid)

    /** butterknife */
    implementation(Libraries.butterKnife)
    annotationProcessor(Libraries.butterKnifeCompiler)

    /** timber log */
    implementation(Libraries.timber)

    /** moxy & cicerone */
    implementation(Libraries.moxy)
    implementation(Libraries.moxyAppCompat)
    annotationProcessor(Libraries.moxyCompiler)
    implementation(Libraries.cicerone)

    /** dagger2 */
    implementation(Libraries.dagger)
    annotationProcessor(Libraries.daggerCompiler)

    /** fcm */
    implementation(Libraries.fireBase)
    implementation(Libraries.fireBaseMessaging)

    /** chuck */
    debugImplementation(Libraries.chuck)
    releaseImplementation(Libraries.chuckRelease)

    /*
        // testing
        testImplementation("org.robolectric:robolectric:4.0.2")
        implementation fileTree("include: [")*.jar")], dir:("libs"))

        // test
        testImplementation "junit:junit:4.12"
        androidTestImplementation "com.android.support.test:runner:1.0.2"
        androidTestImplementation "com.android.support.test.espresso:espresso-core:3.0.2"
    */

    //fabric: crashlytics
/*    implementation("com.crashlytics.sdk.android:crashlytics:2.9.3@aar") {
    transitive = true
}*/

}

//apply from: "../signing/signing.gradle"
