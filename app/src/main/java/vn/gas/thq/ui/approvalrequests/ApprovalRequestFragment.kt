package vn.gas.thq.ui.approvalrequests

import android.os.Bundle
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.ui.retail.RetailFragment
import vn.hongha.ga.R

class ApprovalRequestFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance(): ApprovalRequestFragment {
            val args = Bundle()

            val fragment = ApprovalRequestFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setViewController() {
        viewController = (activity as MainActivity).viewController
    }

    override fun initView() {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_approval_requests
    }

    override fun initObserver() {
    }

    override fun initData() {
        tvTitle.text = "Quản lý yêu cầu duyệt giá"
    }
}