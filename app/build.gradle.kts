plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //For Room substitute for kapt
    id("com.google.devtools.ksp")
    //For Hilt
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    namespace = "com.juegosdemesa.next"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.juegosdemesa.next"
        minSdk = 28
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //Library for tinder like card

    implementation ("com.github.AsynctaskCoffee:tinderlikecardstack:1.0")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("io.coil-kt:coil-compose:2.1.0")

    val navigationVersion = "2.7.6"
    implementation ("androidx.navigation:navigation-fragment-ktx:$navigationVersion")

    implementation ("com.alexstyl.swipeablecard:swipeablecard:0.1.0")
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.26.1-alpha")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation ("androidx.compose.runtime:runtime-livedata:1.5.4")
    implementation ("androidx.navigation:navigation-compose:2.7.6")

    val roomVersion = "2.6.1"
    //Room
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    //Hilt
    val hiltVersion = 2.44
    implementation ("com.google.dagger:hilt-android:$hiltVersion")
    ksp ("com.google.dagger:hilt-android-compiler:$hiltVersion")
    implementation ("androidx.hilt:hilt-navigation-compose:1.1.0")

    //ConstrainLayout
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.1")
}