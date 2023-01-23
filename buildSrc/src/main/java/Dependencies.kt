package com.app.signal.buildSrc

object Versions {
    const val androidXCore = "1.9.0"
    const val androidXAppCompat = "1.5.1"
    const val material = "1.7.0"
    const val constraintLayout = "2.1.4"

    const val jUnit = "4.13.2"
    const val androidXJUnit = "1.1.4"
    const val espressoCore = "3.5.0"
    const val hilt = "2.44"
    const val ktLint = "0.41.0"
    const val okHttp = "4.10.0"
    const val retrofit = "2.9.0"
    const val glide = "4.13.2"
    const val kotlin = "1.7.21"
    const val coWorker = "2.7.1"
    const val coroutines = "1.6.4"
    const val media = "1.0.0-beta03"
    const val room = "2.5.0-beta02"
    const val lifecycle = "2.5.1"
    const val junit_jupiter = "5.7.0" // 5.8.1

}

object TestLibraries {
    const val jUnit = "junit:junit:${Versions.jUnit}"
    const val androidXJUnit = "androidx.test.ext:junit:${Versions.androidXJUnit}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
}


object Libs {

    const val material = "com.google.android.material:material:${Versions.material}"

    object Util {
        const val ktLint = "com.pinterest:ktlint:${Versions.ktLint}"

        const val desugaring = "com.android.tools:desugar_jdk_libs:1.1.5"
        const val coil = "io.coil-kt:coil:2.2.2"
    }

    object OkHttp {
        const val bom = "com.squareup.okhttp3:okhttp-bom:${Versions.okHttp}"

        const val implementation = "com.squareup.okhttp3:okhttp"
        const val logger = "com.squareup.okhttp3:logging-interceptor"
    }

    object Retrofit {

        const val implementation = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"

        const val logger = "com.squareup.okhttp3:logging-interceptor:4.9.0"
        const val gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"

    }

    object Glide {

        const val implementation = "com.github.bumptech.glide:glide:${Versions.glide}"
        const val kapt = "com.github.bumptech.glide:compiler:${Versions.glide}"
    }

    object Kotlin {

        const val stdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"

        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"

        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:${Versions.kotlin}"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"

        const val serializationPlugin =
            "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0"


        object Coroutines {
            const val android =
                "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        }
    }

    object Hilt {
        const val implementation = "com.google.dagger:hilt-android:${Versions.hilt}"
        const val kapt = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    }

    object Facebook {
        const val shimmer = "com.facebook.shimmer:shimmer:0.5.0"
    }

    object AndroidX {

        object AppCompat {
            const val resources =
                "androidx.appcompat:appcompat-resources:${Versions.androidXAppCompat}"
            const val androidXAppCompat =
                "androidx.appcompat:appcompat:${Versions.androidXAppCompat}"
        }

        const val androidXCore = "androidx.core:core-ktx:${Versions.androidXCore}"

        const val activity = "androidx.activity:activity-ktx:1.7.0-alpha01"

        const val fragment = "androidx.fragment:fragment-ktx:1.5.3"
        const val asyncLayoutInflater = "androidx.asynclayoutinflater:asynclayoutinflater:1.0.0"

        const val constraintLayout =
            "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

        const val webkit = "androidx.webkit:webkit:1.5.0"

        const val securityCrypto = "androidx.security:security-crypto:1.1.0-alpha04"


        object RecyclerView {
            const val implementation = "androidx.recyclerview:recyclerview:1.3.0-rc01"
        }

        object Media {

            const val player = "androidx.media3:media3-exoplayer:${Versions.media}"
            const val ui = "androidx.media3:media3-ui:${Versions.media}"
        }

        object Room {
            const val runtime = "androidx.room:room-runtime:${Versions.room}"
            const val kapt = "androidx.room:room-compiler:${Versions.room}"
            const val ktx = "androidx.room:room-ktx:${Versions.room}"
        }

        object Lifecycle {

            const val common = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
            const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"

            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
            const val viewModelSavedState =
                "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycle}"
        }

    }
}