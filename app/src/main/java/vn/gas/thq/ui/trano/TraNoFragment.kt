package vn.gas.thq.ui.trano

import kotlinx.android.synthetic.main.fragment_sang_chiet.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.hongha.ga.R

class TraNoFragment : BaseFragment() {
    private lateinit var pagerAdapter: TraNoPagerAdapter

    companion object {
        @JvmStatic
        fun newInstance() = TraNoFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_sang_chiet
    }

    override fun setViewController() {
        viewController = (activity as MainActivity).viewController
    }

    override fun setupViewModel() {

    }

    override fun initView() {
        tvTitle.text = "Khách hàng trả nợ"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun initData() {
        pagerAdapter = TraNoPagerAdapter(childFragmentManager)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun initObserver() {

    }
}