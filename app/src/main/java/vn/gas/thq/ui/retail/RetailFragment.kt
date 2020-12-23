package vn.gas.thq.ui.retail

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_retail.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.ui.approvalrequests.ApprovalRequestFragment
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
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_retail
    }

    override fun initObserver() {

    }

    override fun setViewController() {
        viewController = (activity as MainActivity).viewController
    }

    override fun setupViewModel() {

    }

    override fun initData() {
        btnNext.setOnClickListener {
            viewController?.pushFragment("yc", ApprovalRequestFragment.newInstance())
        }
    }
}