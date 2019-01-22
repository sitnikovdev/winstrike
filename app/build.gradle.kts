plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(Versions.targetSdk)
    defaultConfig {
        applicationId = ApplicationId.id
        targetSdkVersion(Versions.targetSdk)
        minSdkVersion(Versions.minSdk)
        versionCode = Release.versionCode
        versionName = Release.versionName
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }


    buildTypes {
        getByName("debug") {
            buildConfigField("String", "DEBUGURL", Constants.DEBUGURL)
        }
        getByName("release") {
            buildConfigField("String", "BASEURL", Constants.BASEURL)
            isMinifyEnabled = false
        }
    }

    compileOptions {
        setSourceCompatibility(1.8)
        setTargetCompatibility(1.8)
    }

    dataBinding {
        isEnabled = false
    }

    lintOptions {
        isCheckReleaseBuilds = false
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }

}


dependencies {
    /** kotlin */
    implementation(Libraries.kotlin)

    /** Android AppCompat*/
    implementation(Libraries.appCompat)
    implementation (SupportLibraries.androidx)
    implementation(Libraries.lifecycle)

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
//    implementation(Libraries.materialCalendarView)

    /** bottom navigation bar */
//    implementation(Libraries.ahbottomnavigation)

    /** gson */
    implementation(Libraries.gson)

    /** Retrofit  */
    implementation(Libraries.retrofit)
    implementation(Libraries.retrofitAdapterrxJava)
    implementation(Libraries.retrofitGson)
    implementation(Libraries.retrofitConverterScalars)
    implementation(Libraries.retrofitCoroutines)

    /** moshi */
    implementation(Libraries.moshi)

    /** okhttp */
    implementation(Libraries.okhttp)
    implementation(Libraries.okhttpLoging)

    /** coroutines */
    implementation(Libraries.coroutines)

    /** anko from jetbrain */
    implementation(Libraries.anko)


    /** Rx Java 1 */
    implementation(Libraries.rxAndroid)

    /** butterknife */
//    implementation(Libraries.butterKnife)
//    annotationProcessor(Libraries.butterKnifeCompiler)

    /** timber log */
    implementation(Libraries.timber)

    /** moxy & cicerone */
//    implementation(Libraries.moxy)
//    implementation(Libraries.moxyAppCompat)
//    annotationProcessor(Libraries.moxyCompiler)
//    implementation(Libraries.cicerone)

    /** dagger2 */
//    implementation(Libraries.dagger)
//    annotationProcessor(Libraries.daggerCompiler)

    /** fcm */
    implementation(Libraries.fireBase)
    implementation(Libraries.fireBaseMessaging)

    /** chuck */
    debugImplementation(Libraries.chuck)
    releaseImplementation(Libraries.chuckRelease)

}

