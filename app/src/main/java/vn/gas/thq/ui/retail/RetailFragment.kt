package vn.gas.thq.ui.retail

import android.os.Bundle
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.ui.home.HomeFragment
import vn.hongha.ga.R

class RetailFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance(): RetailFragment {
            val args = Bundle()

            val fragment = RetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView() {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_retail
    }

    override fun initObserver() {

    }

    override fun initData() {

    }
}