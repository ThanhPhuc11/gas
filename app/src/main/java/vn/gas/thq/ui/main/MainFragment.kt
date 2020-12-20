package vn.gas.thq.ui.main

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_main.*
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.ui.home.HomeFragment
import vn.hongha.ga.R

class MainFragment : BaseFragment() {
    companion object {
        fun newInstance(): MainFragment {
            val args = Bundle()

            val fragment = MainFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setViewController() {

    }

    override fun initView() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }

    override fun initObserver() {

    }

    override fun initData() {
        fragmentManager?.beginTransaction()?.add(R.id.flContainerFrm, HomeFragment.newInstance())?.commitAllowingStateLoss()
    }

//    fun showBottomNavigation() {
//        bottomNavigation.visibility = View.VISIBLE
//    }
//
//    fun hideBottomNavigation() {
//        bottomNavigation.visibility = View.GONE
//    }
}