package vn.gas.thq.ui.retail

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.model.TransferRetailModel
import vn.gas.thq.util.ScreenId
import vn.gas.thq.util.ViewController
import vn.hongha.ga.R

open class RetailContainerFragment : BaseFragment() {
    //    open lateinit var childViewController: ViewController
    private var screen: String = ""
    private var transferRetailModel: TransferRetailModel? = null

    companion object {
        @JvmStatic
        fun newInstance(
            screen: String,
            transferRetailModel: TransferRetailModel?
        ): RetailContainerFragment {
            val args = Bundle()
            args.putString("SCREEN", screen)
            args.putSerializable("DATA", transferRetailModel)
            val fragment = RetailContainerFragment()
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
        childViewController = ViewController(R.id.flContainerRetail, childFragmentManager)

        screen = arguments?.getString("SCREEN").toString()
        transferRetailModel = arguments?.getSerializable("DATA") as TransferRetailModel?
        var step: String? = null
        if (screen != ScreenId.SCREEN_RETAIL_STEP_1) {
            step = "STEP_2"
        }
        childViewController?.pushFragment(
            screen,
            RetailFragment.newInstance(step, transferRetailModel)
        )
//        pushToTab(RetailFragment.newInstance())
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_container_retail
    }

    override fun initObserver() {

    }

    override fun initData() {

    }

    private fun pushToTab(fragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.flContainerRetail, fragment, fragment.javaClass.name)
        transaction.commitAllowingStateLoss()
    }
}