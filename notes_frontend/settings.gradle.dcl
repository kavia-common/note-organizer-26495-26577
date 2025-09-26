pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.experimental.android-ecosystem").version("0.1.43")
}

rootProject.name = "notes-frontend"

include("app")

defaults {
    androidApplication {
        jdkVersion = 17
        compileSdk = 34
        minSdk = 24

        versionCode = 1
        versionName = "1.0"
        applicationId = "com.oceanpro.notes"



        testing {
            dependencies {
                implementation("junit:junit:4.13.2")
                runtimeOnly("org.junit.platform:junit-platform-launcher:1.10.2")
            }
        }
    }
}
