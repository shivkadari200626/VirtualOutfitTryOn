plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.virtualoutfittryon"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.virtualoutfittryon"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        ndk { abiFilters.addAll(listOf("arm64-v8a", "armeabi-v7a")) }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // CameraX
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera2) // <-- Fixed this line
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    // MediaPipe
    implementation("com.google.mediapipe:tasks-vision:0.10.14")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
}
