package vn.gas.thq.ui.nghiphep

import kotlinx.android.synthetic.main.fragment_sang_chiet.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.hongha.ga.R

class NghiPhepFragment : BaseFragment() {
    private lateinit var nghỉPhepPagerAdapter: NghiPhepPagerAdapter

    companion object {
        @JvmStatic
        fun newInstance() = NghiPhepFragment()
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
        tvTitle.text = "Đăng ký nghỉ"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun initData() {
        nghỉPhepPagerAdapter = NghiPhepPagerAdapter(childFragmentManager)
        viewPager.adapter = nghỉPhepPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun initObserver() {

    }
}