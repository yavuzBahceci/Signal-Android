package com.app.signal.buildSrc

object TestConfig {
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

object AndroidTarget {
    const val nameSpace = "com.app.signal"
    const val applicationId = "com.app.signal"
    const val compileSdk = 33

    const val buildToolsVersion = "30.0.3"

    const val minSdk = 21
    const val targetSdk = 33
}

object Version {
    const val appVersion = "1.0.0"
    const val versionCode = 1
}

object Java {
    const val jvmTarget = "1.8"
}

object Proguard {
    const val optimizeTxt = "proguard-android-optimize.txt"
    const val rulesPro = "proguard-rules.pro"
}