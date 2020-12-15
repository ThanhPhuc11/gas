package vn.gas.thq.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.hongha.ga.R

class HomeFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_retail
    }

    override fun initObserver() {

    }

    override fun initData() {
        Toast.makeText(context, "hahaha", Toast.LENGTH_SHORT).show()
    }

    override fun setViewController() {
        viewController = (activity as MainActivity).viewController
    }

    override fun initView() {

    }
}