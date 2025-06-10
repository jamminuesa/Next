plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    //For Hilt
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.juegosdemesa.next"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.juegosdemesa.next"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // AndroidX and Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose BoM
    implementation(platform(libs.compose.bom))
    androidTestImplementation(platform(libs.compose.bom))
    //Compose dependencies (versions managed by BoM)
    implementation(libs.compose.material3)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.compose.constrainlayout)

    // Testing libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4) // Uses Compose BoM
    debugImplementation(libs.androidx.ui.tooling) // Uses Compose BoM
    debugImplementation(libs.androidx.ui.test.manifest) // Uses Compose BoM


    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.compose)

    // Lifecycle extensions for Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.compose.runtime.livedata)

    //Room
    implementation (libs.androidx.room.runtime)
    ksp (libs.androidx.room.compiler)
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation (libs.androidx.room.ktx)

    //Hilt
    implementation (libs.hilt)
    ksp (libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Other Libraries
    implementation(libs.swipeablecard)
    implementation (libs.coil.compose)
}