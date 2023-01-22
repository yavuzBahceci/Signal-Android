package com.app.signal.buildSrc

object Module {

    object Core {
        const val dependencies = ":di"
        const val utils = ":utils"
        const val data = ":data"
        const val domain = ":domain"
    }

    object Services {
        const val android_services = ":services:android-services"
    }

    object Common {
        const val navigation = ":common:navigation"
    }

    object UI {
        const val designSystem = ":ui:design-system"
        const val controlKit = ":ui:control-kit"
    }
}