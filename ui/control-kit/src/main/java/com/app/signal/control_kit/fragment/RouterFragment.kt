package com.app.signal.control_kit.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.app.signal.control_kit.R
import com.app.signal.control_kit.ex.*
import com.app.signal.control_kit.fragment.ex.registerOnBackPressedCallback
import com.app.signal.design_system.Transition

class RouterFragment : Fragment(R.layout.fragment_router) {
    val current: Fragment?
        get() = childFragmentManager.current

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerOnBackPressedCallback {
            it.backStackEntryCount > 1
        }
    }

    fun push(fragment: Fragment, animated: Boolean = true) {
        childFragmentManager.push(fragment, animated)
    }

    fun present(
        fragment: Fragment,
        transition: Transition? = null,
        modalityBehaviour: ModalityBehaviour? = null
    ) {
        childFragmentManager.present(fragment, transition, modalityBehaviour)
    }

    fun pop(tag: String? = null, inclusive: Boolean = false) {
        childFragmentManager.pop(tag, inclusive)
    }

    fun popAll(inclusive: Boolean = false) {
        childFragmentManager.popAll(inclusive)
    }

    fun executePending() {
        childFragmentManager.executePendingTransactions()
    }

    inline fun <reified T : Fragment> fragmentByClass(): T? {
        return childFragmentManager.fragmentByClass()
    }

    inline fun <reified T : Fragment> ensureFragmentByClass(): T {
        return childFragmentManager.fragmentByClass()!!
    }
}
