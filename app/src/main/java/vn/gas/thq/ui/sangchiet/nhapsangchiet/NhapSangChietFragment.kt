package vn.gas.thq.ui.sangchiet.nhapsangchiet

import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_init_sang_chiet.*
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.customview.CustomArrayAdapter
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.util.*
import vn.hongha.ga.R

class NhapSangChietFragment : BaseFragment() {
    private lateinit var viewModel: NhapSangChietViewModel
    private lateinit var suggestAdapter: CustomArrayAdapter
    private var alertDialog: AlertDialog? = null
    private var availableKHL = 0
    private var useKHL = 0

    companion object {
        @JvmStatic
        fun newInstance() = NhapSangChietFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_init_sang_chiet
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
                .get(NhapSangChietViewModel::class.java)
    }

    override fun initView() {

    }

    override fun initData() {
        viewModel.getAvailableKHL()
        btnThemMoi.setOnClickListener(this::themMoiSangChiet)
        suggestAdapter = CustomArrayAdapter(context, android.R.layout.simple_list_item_1)
        edtUseKHL.addTextChangedListener(
            NumberTextWatcher(
                edtUseKHL,
                suggestAdapter,
                object : CallBackChange {
                    override fun afterEditTextChange(it: Editable?) {
                        useKHL = getRealNumberV2(it)
                    }
                })
        )
    }

    override fun initObserver() {
        viewModel.callbackAvailableKHL.observe(viewLifecycleOwner, {
            availableKHL = it
            if (availableKHL >= 0) {
                edtAvailableKHL.setText("${CommonUtils.priceWithoutDecimal(availableKHL.toDouble())} Kg")
            }
        })

        viewModel.callbackCheckTransfer.observe(viewLifecycleOwner, {
            val obj = InitSangChiet()
            obj.amountGasLiquidBf = edtUseKHL.text.toString().replace(".", "").toInt()
            obj.amountGas12 =
                if (TextUtils.isEmpty(edtAmount12.text)) 0 else edtAmount12.text.toString()
                    .toInt()
            obj.amountGas45 =
                if (TextUtils.isEmpty(edtAmount45.text)) 0 else edtAmount45.text.toString()
                    .toInt()
            if (it) {
                CommonUtils.showConfirmDiglog2Button(
                    activity,
                    "Xác nhận",
                    "Ngày ${AppDateUtils.getCurrentDate()} đã nhập thành phẩm sau sang chiết,\nbạn có chắc chắn tiếp tục nhập?",
                    getString(
                        R.string.biometric_negative_button_text
                    ),
                    getString(R.string.text_ok)
                ) {
                    if (it == AppConstants.YES) {
                        viewModel.initSangChiet(obj)
                    }
                }
            } else {
                viewModel.initSangChiet(obj)
            }
        })

        viewModel.callbackInitSangChietSuccess.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(activity, "Thông báo", "Đã nhập thành phẩm thành công") {
                alertDialog?.dismiss()
                viewModel.getAvailableKHL()
            }
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

    private fun themMoiSangChiet(view: View) {
        if (TextUtils.isEmpty(edtUseKHL.text) || TextUtils.isEmpty(edtAvailableKHL.text)) return
        if (useKHL > availableKHL) {
            showMess("Số lượng khí hóa lỏng sử dụng vượt quá số lượng khí đang có tại trạm")
            return
        }
        if (TextUtils.isEmpty(edtAmount12.text) && TextUtils.isEmpty(edtAmount45.text)) {
            showMess("Bạn chưa nhập số lượng thành phẩm sau khi sang chiết được")
            return
        }

        CommonUtils.showConfirmDiglog2Button(
            activity, "Xác nhận", "Bạn có chắc chắn muốn nhập thông tin sang chiết?", getString(
                R.string.biometric_negative_button_text
            ), getString(R.string.text_ok)
        ) {
            if (it == AppConstants.YES) {
//                viewModel.initSangChiet(obj)
                viewModel.checkTransfer()
            }
        }
    }

    private fun getRealNumberV2(view: Editable?): Int {
        return if (TextUtils.isEmpty(
                view.toString().trim()
            )
        ) 0 else CommonUtils.getIntFromStringDecimal(
            view.toString()
                .trim()
        )
    }
}