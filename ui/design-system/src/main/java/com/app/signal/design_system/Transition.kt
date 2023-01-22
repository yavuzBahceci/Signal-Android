package com.app.signal.design_system

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes

open class Transition(
    @AnimatorRes @AnimRes val enter: Int,
    @AnimatorRes @AnimRes val exit: Int,
    @AnimatorRes @AnimRes val popEnter: Int,
    @AnimatorRes @AnimRes val popExit: Int,
) {
    object Fade : Transition(
        R.anim.fade_enter,
        R.anim.fade_exit,
        R.anim.fade_pop_enter,
        R.anim.fade_pop_exit
    )

    object Push : Transition(
        R.anim.push_enter,
        R.anim.push_exit,
        R.anim.push_pop_enter,
        R.anim.push_pop_exit
    )

    object Slide : Transition(
        R.anim.modal_enter,
        R.anim.modal_exit,
        R.anim.modal_pop_enter,
        R.anim.modal_pop_exit
    )

    object Sheet : Transition(
        R.animator.sheet_animator_enter,
        0,
        0,
        R.animator.sheet_animator_exit
    )

    object None : Transition(0, 0, 0, 0)

    val dropEnterAnimation: Transition
        get() {
            return Transition(0, 0, popEnter, popExit)
        }

    val dropExitAnimation: Transition
        get() {
            return Transition(enter, 0, popEnter, popExit)
        }
}