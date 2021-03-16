package vn.gas.thq.ui.sangchiet.qlsangchiet

import kotlinx.android.synthetic.main.fragment_ql_sang_chiet.*
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.CommonUtils
import vn.hongha.ga.R

class QLSangChietFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance() = QLSangChietFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_ql_sang_chiet
    }

    override fun setViewController() {

    }

    override fun setupViewModel() {

    }

    override fun initView() {

    }

    override fun initData() {
        edtStartDate.setText(AppDateUtils.getYesterdayDate())
        edtEndDate.setText(AppDateUtils.getCurrentDate())
        edtStartDate.setOnClickListener {
            CommonUtils.showCalendarDialog(
                context,
                edtStartDate.text.toString()
            ) { strDate -> edtStartDate.setText(strDate) }
        }

        edtEndDate.setOnClickListener {
            CommonUtils.showCalendarDialog(
                context,
                edtEndDate.text.toString()
            ) { strDate -> edtEndDate.setText(strDate) }
        }
        btnSearch.setOnClickListener {

        }
    }

    override fun initObserver() {

    }
}