package vn.gas.thq.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import vn.hongha.ga.R

class ViewController(var flContainer: Int, var fragmentManager: FragmentManager?) {
    var currentFragment: Fragment? = null

    fun pushFragment(
        screenId: String,
        fragment: Fragment,
    ) {
        fragmentManager?.beginTransaction()
            ?.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_exit, R.anim.pop_enter)
            ?.add(flContainer, fragment, screenId)
            ?.addToBackStack(screenId)
            ?.commitAllowingStateLoss()
        currentFragment = fragment
    }

    fun replaceByFragment(
        screenId: String,
        fragment: Fragment,
    ) {
        fragmentManager?.beginTransaction()
//            ?.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_exit, R.anim.pop_enter)
            ?.replace(flContainer, fragment)
            ?.addToBackStack(screenId)
            ?.commitAllowingStateLoss()
        currentFragment = fragment
    }

    fun popFragment() {
        fragmentManager?.popBackStack()
        if (fragmentManager?.backStackEntryCount!! > 0) {
            val fragmentTag =
                fragmentManager?.getBackStackEntryAt(fragmentManager?.backStackEntryCount!! - 1)?.name
            currentFragment = fragmentManager?.findFragmentByTag(fragmentTag)
        }
    }

    fun popAllFragment() {
        fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        currentFragment = null
    }

    fun popNFragment(n: Int) {
        for (i in 0 until n) {
            fragmentManager?.popBackStack()
        }
        if (fragmentManager?.backStackEntryCount!! > 0) {
            val fragmentTag =
                fragmentManager?.getBackStackEntryAt(fragmentManager?.backStackEntryCount!! - 1)?.name
            currentFragment = fragmentManager?.findFragmentByTag(fragmentTag)
        }
    }
}