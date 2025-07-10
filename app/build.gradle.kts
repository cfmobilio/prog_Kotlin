plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.wa"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.pro"
        minSdk = 24
        targetSdk = 34
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

    buildFeatures {
        dataBinding = true
    }

    packaging {
        resources {
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/NOTICE.txt"
            excludes += "META-INF/ASL2.0"

        }
    }
}
dependencies {
    // AndroidX e Material
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity-ktx:1.10.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.1")
    implementation("androidx.fragment:fragment-ktx:1.8.8")


    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Firebase (BOM semplifica la gestione delle versioni)
    implementation(platform("com.google.firebase:firebase-bom:33.16.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.4")

    // Google Sign-In
    implementation ("com.google.android.gms:play-services-auth:21.3.0")
    implementation ("com.google.firebase:firebase-auth:23.2.1")
    implementation ("com.google.android.gms:play-services-base:18.6.0")
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("androidx.navigationevent:navigationevent-android:1.0.0-alpha04")
    implementation("androidx.room:room-external-antlr:2.7.2")
    implementation("androidx.test.ext:junit-ktx:1.2.1")
    // Test
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.6.1")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.1")

    implementation ("androidx.cardview:cardview:1.0.0")

    testImplementation ("androidx.arch.core:core-testing:2.2.0")

    // Testing unitario (local tests)
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("org.robolectric:robolectric:4.11.1")

    // Instrumented tests (androidTest)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")
    androidTestImplementation("androidx.fragment:fragment-testing:1.6.2")
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.5")
    androidTestImplementation("io.mockk:mockk-android:1.13.8")
    androidTestImplementation("org.mockito:mockito-android:5.7.0")
    androidTestImplementation("androidx.test:rules:1.5.0")

    testImplementation("com.google.truth:truth:1.1.3")


}
