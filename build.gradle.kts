buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false

    // KSP
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false

    // Navigation
    id("androidx.navigation.safeargs.kotlin") version "2.6.0" apply false
}