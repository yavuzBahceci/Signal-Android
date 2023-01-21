package com.app.signal.di

enum class Environment {
    Development, Production;

    companion object {
        fun current(): Environment {
            return if (BuildConfig.DEBUG) {
                Development
            } else {
                return Production
            }
        }
    }
}