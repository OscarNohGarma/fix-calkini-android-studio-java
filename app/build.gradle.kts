plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services") // Agrega esta línea
}

android {
    namespace = "com.example.fixcalkini"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.fixcalkini"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    implementation(platform("com.google.firebase:firebase-bom:32.7.0")) // Usa la última versión estable

    // Firebase Core
    implementation("com.google.firebase:firebase-analytics")

    // Firebase Authentication (si lo necesitas)
    implementation("com.google.firebase:firebase-auth")

    // Firebase Firestore (si lo usas)
    implementation("com.google.firebase:firebase-firestore")

    // Firebase Realtime Database (si lo usas)
    implementation("com.google.firebase:firebase-database")

    // Firebase Storage (si lo usas)
    implementation("com.google.firebase:firebase-storage")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.google.maps)
}