package vn.gas.thq.ui.retail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.fragment_container_retail.*
import kotlinx.android.synthetic.main.fragment_retail.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.ui.qlyeucauduyetgia.QuanLyYeuCauDuyetGiaFragment
import vn.gas.thq.util.ScreenId
import vn.gas.thq.util.ViewController
import vn.hongha.ga.R

class RetailFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance(step: String?): RetailFragment {
            val bundle = Bundle()
            bundle.apply {
                putString("STEP", step)
            }
            val fragment = RetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
//        imgBack.setOnClickListener {
//            viewController?.popFragment()
//        }
        if ("STEP_2" == arguments?.getString("STEP")) {
            btnSubmit.text = "BÁN HÀNG"
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_retail
    }

    override fun initObserver() {

    }

    override fun setViewController() {
        viewController = (activity as MainActivity).viewController
        childViewController = (parentFragment as RetailContainerFragment).childViewController
    }

    override fun setupViewModel() {

    }

    override fun initData() {
        btnSubmit.setOnClickListener {
            if ("STEP_2" == arguments?.getString("STEP")) {
                (parentFragment as RetailContainerFragment).stepView.setStepDone("2")
                return@setOnClickListener
            }
            childViewController?.pushFragment(
                ScreenId.SCREEN_RETAIL_STEP_2,
                newInstance("STEP_2")
            )
            (parentFragment as RetailContainerFragment).stepView.setStepDone("1")
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