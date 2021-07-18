package vn.gas.thq.ui.trano

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vn.gas.thq.ui.trano.nhaptrano.NhapTraNoFragment
import vn.gas.thq.ui.trano.qltrano.QLTraNoFragment

class TraNoPagerAdapter(private val fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return if (position == 0) NhapTraNoFragment.newInstance() else QLTraNoFragment.newInstance()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Tạo mới thông tin KH trả nợ"
            1 -> "Lịch sử trả nợ"
            else -> ""
        }
    }
}