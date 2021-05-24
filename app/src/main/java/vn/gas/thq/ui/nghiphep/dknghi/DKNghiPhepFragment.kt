package vn.gas.thq.ui.nghiphep.dknghi

import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_dang_ky_nghi.*
import kotlinx.android.synthetic.main.fragment_dang_ky_nghi.edtEndDate
import kotlinx.android.synthetic.main.fragment_dang_ky_nghi.edtStartDate
import kotlinx.android.synthetic.main.fragment_lich_su_nghi.*
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.CommonUtils
import vn.hongha.ga.R

class DKNghiPhepFragment : BaseFragment() {
    lateinit var viewModel: DKNghiPhepViewModel

    companion object {
        @JvmStatic
        fun newInstance() = DKNghiPhepFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_dang_ky_nghi
    }

    override fun setViewController() {

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
                .get(DKNghiPhepViewModel::class.java)
    }

    override fun initView() {

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
        btnRegister.setOnClickListener(this::registerVacation)
    }

    override fun initObserver() {
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

        viewModel.callbackRegisterSuccess.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(
                activity,
                "Thông báo",
                "Bạn đã đăng ký nghỉ phép thành công"
            ) {

            }
        })
    }

    private fun registerVacation(view: View) {
        val fromDate =
            AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_2,
                AppDateUtils.FORMAT_5,
                edtStartDate.text.toString()
            )
        val endDate =
            AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_2,
                AppDateUtils.FORMAT_5,
                edtEndDate.text.toString()
            )
        if (!TextUtils.isEmpty(edtLyDo.text)) {
            viewModel.registerVacation(1, fromDate, endDate, edtLyDo.text.toString())
        }

    }
}