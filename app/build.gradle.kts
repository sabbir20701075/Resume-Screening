plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.resumescreening"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.resumescreening"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        mlModelBinding = true
    }


}


dependencies {
    // Firebase BOM
    implementation(platform("com.google.firebase:firebase-bom:32.0.0"))
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-analytics")

    // Other dependencies
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //implementation("com.google.ai.edge.litert:litert:1.0.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Google Play Services and Maps
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    // Volley for network requests
    implementation("com.android.volley:volley:1.2.1")

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    // Retrofit for networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.8.8")

    // TensorFlow Lite dependencies
    //implementation("org.tensorflow:tensorflow-lite:2.16.1") // Updated version
    //implementation("org.tensorflow:tensorflow-lite-support:2.16.1") // Updated version
   // implementation("org.tensorflow:tensorflow-lite-metadata:2.16.1") // Updated version
    // https://mvnrepository.com/artifact/org.tensorflow/tensorflow-lite-api
    //implementation("org.tensorflow:tensorflow-lite-api:2.16.1")

    // TensorFlow Lite Core library
    implementation("org.tensorflow:tensorflow-lite:2.12.0")

// TensorFlow Lite Support Library
    implementation("org.tensorflow:tensorflow-lite-support:0.3.1")

// TensorFlow Lite Metadata Library
   implementation("org.tensorflow:tensorflow-lite-metadata:0.3.1")

    implementation("com.github.PhilJay:MPAndroidChart:v3.0.3")

   // implementation("com.apollographql.apollo3:apollo-runtime:3.6.0")
    implementation ("com.apollographql.apollo:apollo-runtime:2.5.9")
    implementation ("com.apollographql.apollo:apollo-coroutines-support:2.5.9") // If you plan to use coroutines
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.2")




    // PDF Viewer
    //implementation("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1") // Updated version
    //implementation("com.github.barteksc:android-pdf-viewer:2.8.2") // For displaying PDFs

    // iText dependencies for PDF manipulation
    implementation("com.itextpdf:itextg:5.5.10")
    //implementation("com.itextpdf:kernel:7.1.17")
    //implementation("com.itextpdf:layout:7.1.17")
   // implementation("com.itextpdf:io:7.1.17")
}