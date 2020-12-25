package vn.gas.thq.ui.qlyeucauxuatkho

import android.os.Bundle
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.hongha.ga.R

class QLYCXuatKhoContainFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance(): QLYCXuatKhoContainFragment {
            val args = Bundle()

            val fragment = QLYCXuatKhoContainFragment()
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
        tvTitle.text = "Quản lý yêu cầu"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_qlyc_xuatkho_container
    }

    override fun initObserver() {
    }

    override fun initData() {
    }
}