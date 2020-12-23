package vn.gas.thq.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_main.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.ui.approvalrequests.ApprovalRequestFragment
import vn.gas.thq.ui.home.HomeFragment
import vn.gas.thq.ui.retail.RetailFragment
import vn.gas.thq.util.ScreenId
import vn.hongha.ga.R

class MainFragment : BaseFragment(), BottomNavigationView.OnNavigationItemSelectedListener {
    private var preFragment: String? = null
    lateinit var homeFragment: HomeFragment
    lateinit var retailFragment: RetailFragment
    lateinit var approvalRequestFragment: ApprovalRequestFragment
//    lateinit var homeFragment: HomeFragment

    companion object {
        fun newInstance(): MainFragment {
            val args = Bundle()

            val fragment = MainFragment()
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
        homeFragment = HomeFragment.newInstance()
//        retailFragment = RetailFragment.newInstance()
        approvalRequestFragment = ApprovalRequestFragment.newInstance()
//        val homeFragment = HomeFragment.newInstance()

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }

    override fun initObserver() {

    }

    override fun initData() {
//        pushToTab(HomeFragment.newInstance())

        bottomNavigation?.setOnNavigationItemSelectedListener(this)
        pushToTab(homeFragment)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                pushToTab(homeFragment)
            }
            R.id.navigation_buy -> {
//                pushToTab(retailFragment)
                viewController?.pushFragment(ScreenId.SCREEN_RETAIL, RetailFragment.newInstance())
            }
            R.id.navigation_setting -> {
                pushToTab(approvalRequestFragment)
            }
            R.id.navigation_other -> {
            }
        }
        return true
    }

    fun addShowHideFragment(
        fragmentManager: FragmentManager?,
        fragment: Fragment
    ) {
//        Preconditions.checkNotNull(
//            fragmentManager
//        )
//        Preconditions.checkNotNull(fragment)
        val transaction =
            fragmentManager?.beginTransaction()
        if (fragmentManager?.findFragmentByTag(fragment.tag)?.javaClass?.name == fragment.tag) {
            for (fragment in fragmentManager!!.fragments) {
                if (fragment.isVisible)
                    transaction?.hide(fragment)
            }
            transaction?.show(fragmentManager.findFragmentByTag(fragment.javaClass.name)!!)
        } else {
            transaction?.add(R.id.flContainerFrm, fragment, fragment.javaClass.name)
        }
        transaction?.commit()
    }

    private fun pushToTab(fragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        childFragmentManager.findFragmentByTag(preFragment)?.let { transaction.hide(it) }
        if (childFragmentManager.findFragmentByTag(fragment.javaClass.name)?.javaClass?.name == fragment.javaClass.name) {
            transaction.show(fragment)
        } else {
            transaction.add(R.id.flContainerFrm, fragment, fragment.javaClass.name)
        }
        preFragment = fragment.javaClass.name
        transaction.commitAllowingStateLoss()
    }

//    fun showBottomNavigation() {
//        bottomNavigation.visibility = View.VISIBLE
//    }
//
//    fun hideBottomNavigation() {
//        bottomNavigation.visibility = View.GONE
//    }
}