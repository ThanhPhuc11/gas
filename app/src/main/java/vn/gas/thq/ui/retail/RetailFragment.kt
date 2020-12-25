package vn.gas.thq.ui.retail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.fragment_retail.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.ui.qlyeucauduyetgia.QuanLyYeuCauDuyetGiaFragment
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
            viewController?.pushFragment("yc", QuanLyYeuCauDuyetGiaFragment.newInstance())
        }

        tvLabelThuHoiVo.setOnClickListener { this.expand(linearThuHoiVo) }
        tvLabelBanVo.setOnClickListener { this.expand(linearBanVo) }
        tvLabelMuaVo.setOnClickListener { this.expand(linearMuaVo) }
        tvLabelBanKhi.setOnClickListener { this.expand(linearBanKhi) }
        tvLabelCongNoKH.setOnClickListener { this.expand(linearCongNoKH) }
    }

    private fun expand(container: View) {
        if (container.isVisible) {
            container.visibility = View.GONE
        } else {
            container.visibility = View.VISIBLE
        }
    }
}