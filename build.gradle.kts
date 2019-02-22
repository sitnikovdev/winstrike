plugins {
	id("com.gradle.build-scan") version "2.1"
}

buildScan {
	termsOfServiceUrl = "https://gradle.com/terms-of-service"
	termsOfServiceAgree = "yes"
	publishAlways()
}

buildscript {
	repositories {
		jcenter()
		google()
	}
	dependencies {
		classpath("com.android.tools.build:gradle:${Versions.gradle}")
		classpath("com.google.gms:google-services:${Versions.googleServicies}")
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
		classpath("android.arch.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}")
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
				includeGroup("com.github.pakoito")
			}
		}
	}
}

tasks.register("clean", Delete::class) {
	delete(rootProject.buildDir)
}

