package vn.gas.thq.ui.pheduyetgia

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_phe_duyet_gia.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.ui.retail.RetailFragment
import vn.gas.thq.util.CommonUtils
import vn.hongha.ga.R

class PheDuyetGiaFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance(): PheDuyetGiaFragment {
            val args = Bundle()

            val fragment = PheDuyetGiaFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setViewController() {
        viewController = (activity as MainActivity).viewController
    }

    override fun setupViewModel() {

    }

    override fun initView() {
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
        edtStartDate.setOnClickListener {
            CommonUtils.showCalendarDialog(
                context,
                edtStartDate.text.toString()
            ) { strDate -> edtStartDate.setText(strDate) }
        }

        edtEndDate.setOnClickListener {
            CommonUtils.showCalendarDialog(
                context,
                edtEndDate.text.toString()
            ) { strDate -> edtEndDate.setText(strDate) }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_phe_duyet_gia
    }

    override fun initObserver() {
    }

    override fun initData() {
        tvTitle.text = "Phê duyệt yêu cầu giảm giá"
    }
}