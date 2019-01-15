// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        jcenter()
        google()
    }
    dependencies {
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
        classpath("com.android.tools.build:gradle:${Versions.gradle}")
        classpath("com.google.gms:google-services:${Versions.googleServicies}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("com.android.databinding:compiler:${Versions.dataBinding}")
        classpath("net.sf.proguard:proguard-gradle:${Versions.proguardGradle}")
    }
}


allprojects {

    repositories {
        jcenter()
        google()
        maven {
            url = uri("https://jitpack.io")
            content {
                includeGroup("com.github.scottyab")
            }
        }
    }
}

tasks.register("clean",Delete::class) {
    delete(rootProject.buildDir)
}

