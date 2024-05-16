

plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")

}

android {
    namespace = "com.nustfruta"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nustfruta"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.cardview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("androidx.core:core-splashscreen:1.0.1")

    //Firebase Bom
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))

    // Firebase analytics
    implementation("com.google.firebase:firebase-analytics")

    // Firebase database
    implementation("com.google.firebase:firebase-database")

    // Firebase storage
    implementation("com.google.firebase:firebase-storage")

    // Firebase authentication
    implementation("com.google.firebase:firebase-auth")

    // country code picker
    implementation("com.hbb20:ccp:2.7.3")

    implementation("com.vanniktech:android-image-cropper:4.5.0")

    // FirebaseUI for Firebase Realtime Database
    implementation("com.firebaseui:firebase-ui-database:8.0.2")

    //FirebaseUI for Firebase Auth
    implementation ("com.firebaseui:firebase-ui-auth:8.0.2")

    //FirebaseUI for Firebase Storage
    implementation ("com.firebaseui:firebase-ui-storage:8.0.2")

    // Image cropping library
    implementation("com.vanniktech:android-image-cropper:4.5.0")

    // glide library
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // kotlin annotation processor
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // lottie animations
    implementation("com.airbnb.android:lottie:6.4.0")

    // firebase play integrity
    implementation("com.google.firebase:firebase-appcheck-playintegrity")
}