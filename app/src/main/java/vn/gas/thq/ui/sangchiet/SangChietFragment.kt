package vn.gas.thq.ui.sangchiet

import kotlinx.android.synthetic.main.fragment_sang_chiet.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.hongha.ga.R

class SangChietFragment : BaseFragment() {
    private lateinit var sangchietPagerAdapter: SangChietPagerAdapter

    companion object {
        @JvmStatic
        fun newInstance() = SangChietFragment()
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
        tvTitle.text = "Nhập thành phẩm sau sang chiết"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun initData() {
        sangchietPagerAdapter = SangChietPagerAdapter(childFragmentManager)
        viewPager.adapter = sangchietPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun initObserver() {

    }
}