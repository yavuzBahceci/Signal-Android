package com.app.signal.buildSrc

object Module {

    object Core {
        const val app_runtime = ":runtime"
        const val dependencies = ":di"
        const val utils = ":utils"
        const val data = ":data"
        const val domain = ":domain"
        const val core = ":core"
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
        const val webBrowser = ":ui:web-browser"

        const val alertSheet = ":ui:alert-sheet"
        const val pickerSheet = ":ui:picker-sheet"

        const val pdfView = ":ui:pdf-view"
        const val keypad = ":ui:keypad"

        const val picker = ":ui:picker"
    }
}