package vn.gas.thq.ui.retail

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.util.ScreenId
import vn.gas.thq.util.ViewController
import vn.hongha.ga.R

open class RetailContainerFragment : BaseFragment() {
//    open lateinit var childViewController: ViewController

    companion object {
        @JvmStatic
        fun newInstance(): RetailContainerFragment {
            val args = Bundle()

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
        childViewController?.pushFragment(
            ScreenId.SCREEN_RETAIL_STEP_1,
            RetailFragment.newInstance(null)
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