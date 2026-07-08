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
        
        // API key for Groq/Gemini
        buildConfigField("String", "GROQ_API_KEY", "\"${System.getenv("Default_GROQ_API_KEY") ?: ""}\"")
    }

    signingConfigs {
        create("release") {
            storeFile = file(System.getenv("CM_KEYSTORE_PATH") ?: "debug.keystore")
            storePassword = System.getenv("CM_KEYSTORE_PASSWORD")
            keyAlias = System.getenv("CM_KEY_ALIAS")
            keyPassword = System.getenv("CM_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true  // <-- Must be true for BuildConfig.GROQ_API_KEY
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // CameraX
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera2) // Fixed
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    // MediaPipe
    implementation("com.google.mediapipe:tasks-vision:0.10.14")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Retrofit + OkHttp for Groq/Gemini API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // MLKit Face + Pose - CORRECT
    implementation("com.google.mlkit:face-detection:16.1.7")
    implementation("com.google.mlkit:pose-detection:18.0.0") 
    implementation("com.google.mlkit:pose-detection-common:18.0.0")

    // Gson for JSON
    implementation("com.google.code.gson:gson:2.11.0")
}
