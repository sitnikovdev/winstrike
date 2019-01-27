buildscript {
	repositories {
		jcenter()
		google()
	}
	dependencies {
		classpath("com.android.tools.build:gradle:${Versions.gradle}")
		classpath("com.google.gms:google-services:${Versions.googleServicies}")
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
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

tasks.register("clean", Delete::class) {
	delete(rootProject.buildDir)
}

