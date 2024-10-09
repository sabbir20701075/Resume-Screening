// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral() // Ensure mavenCentral is included
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.15") // Google Services plugin
        //classpath("com.apollographql.apollo:apollo:apollo-gradle-plugin:2.5.9")
        classpath("com.apollographql.apollo3:apollo-gradle-plugin:3.7.4")
    }
}

// Apply the Android Gradle Plugin
plugins {
    id("com.android.application") version "8.2.1" apply false
}

// Optionally, you can include a gradle.properties file for additional configurations
// For example, you might want to set the Gradle JVM arguments or Android SDK versions
