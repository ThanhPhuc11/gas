package vn.gas.thq.ui.sangchiet

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vn.gas.thq.ui.sangchiet.nhapsangchiet.NhapSangChietFragment
import vn.gas.thq.ui.sangchiet.qlsangchiet.QLSangChietFragment

class SangChietPagerAdapter(private val fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return if (position == 0) NhapSangChietFragment.newInstance() else QLSangChietFragment.newInstance()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Tạo mới nhập thành phẩm"
            1 -> "Quản lý nhập thành phẩm"
            else -> ""
        }
    }
}