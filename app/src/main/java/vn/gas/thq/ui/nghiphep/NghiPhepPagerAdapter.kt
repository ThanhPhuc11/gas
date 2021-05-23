package vn.gas.thq.ui.nghiphep

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vn.gas.thq.ui.nghiphep.dknghi.DKNghiPhepFragment
import vn.gas.thq.ui.nghiphep.lichsu.LSNghiPhepFragment

class NghiPhepPagerAdapter(private val fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return if (position == 0) DKNghiPhepFragment.newInstance() else LSNghiPhepFragment.newInstance()
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Đăng ký nghỉ"
            1 -> "Lịch sử đăng ký nghỉ"
            else -> ""
        }
    }
}