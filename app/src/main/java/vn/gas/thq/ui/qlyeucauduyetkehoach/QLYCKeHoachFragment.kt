package vn.gas.thq.ui.qlyeucauduyetkehoach

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_qlyc_ca_nhan.*
import kotlinx.android.synthetic.main.fragment_qlyc_ke_hoach.*
import kotlinx.android.synthetic.main.fragment_qlyc_ke_hoach.btnSearch
import kotlinx.android.synthetic.main.fragment_qlyc_ke_hoach.edtEndDate
import kotlinx.android.synthetic.main.fragment_qlyc_ke_hoach.edtStartDate
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.kehoachbh.LapKeHoachBHViewModel
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.CommonUtils
import vn.hongha.ga.R

class QLYCKeHoachFragment : BaseFragment() {
    private lateinit var viewModel: QLYCKeHoachViewModel
    private var status: Int? = null
    private var staffCode: String? = null
    private var shopCode: String? = null

    companion object {
        @JvmStatic
        fun newInstance() = QLYCKeHoachFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_qlyc_ke_hoach
    }

    override fun setViewController() {
        viewController = (activity as MainActivity).viewController
    }

    override fun setupViewModel() {
        viewModel =
            ViewModelProviders.of(this,
                context?.let {
                    RetrofitBuilder.getInstance(it)?.create(ApiService::class.java)
                        ?.let { apiService ->
                            ViewModelFactory(apiService, context)
                        }
                })
                .get(QLYCKeHoachViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Quản lý yêu cầu KH bán hàng"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun initData() {
        edtStartDate.setText(AppDateUtils.getCurrentDate())
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
        btnSearch.setOnClickListener(this::onSearch)
    }

    override fun initObserver() {
        viewModel.callbackListKHBH.observe(viewLifecycleOwner, {
            Log.e("Phuc", it.toString())
        })

        viewModel.callbackStart.observe(viewLifecycleOwner, {
            showLoading()
        })

        viewModel.callbackSuccess.observe(viewLifecycleOwner, {
            hideLoading()
        })

        viewModel.callbackFail.observe(viewLifecycleOwner, {
            hideLoading()
        })

        viewModel.showMessCallback.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun onSearch(view: View) {
        var fromDate =
            AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_2,
                AppDateUtils.FORMAT_5,
                edtStartDate.text.toString()
            )
        var endDate =
            AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_2,
                AppDateUtils.FORMAT_5,
                edtEndDate.text.toString()
            )
        viewModel.getKeHoachBH(status, fromDate, endDate, staffCode, shopCode, null, 0, 1000)
    }
}