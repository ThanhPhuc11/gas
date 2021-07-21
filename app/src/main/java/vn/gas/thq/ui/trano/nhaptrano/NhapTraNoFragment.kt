package vn.gas.thq.ui.trano.nhaptrano

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_init_tra_no.*
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.customview.CustomArrayAdapter
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.retail.Customer
import vn.gas.thq.util.*
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.gas.thq.util.dialog.GetListDataDemo
import vn.hongha.ga.R
import java.util.*

class NhapTraNoFragment : BaseFragment() {
    private lateinit var viewModel: NhapTraNoViewModel
    private lateinit var suggestAdapter: CustomArrayAdapter
    private var alertDialog: AlertDialog? = null
    private var mListCustomer = mutableListOf<Customer>()

    private var custID: Int? = null
    private var typeRequestCongNo: String? = null
    private var debitTypeInt: Int? = null
    private var congNoHienTai: Int = 0
    private var KHTra: Int = 0
    private var tienMat: Int = 0
    private var tienCK: Int = 0
    private var congNoConLai: Int = 0

    companion object {
        @JvmStatic
        fun newInstance() = NhapTraNoFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_init_tra_no
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
                .get(NhapTraNoViewModel::class.java)
    }

    override fun initView() {

    }

    override fun initData() {
        val user = AppPreferencesHelper(context).userModel
        val queryKH = "shopId==${user.shopId};status==1"
        viewModel.onGetListCustomer(queryKH, 0)
        llWrapCongNoTien.visibility = View.GONE
        lblKHTra.visibility = View.GONE
        edtKHTra.visibility = View.GONE

        edtKH.setOnClickListener(this::onChooseCustomer)

        edtLoaiCongNo.setOnClickListener(this::onChooseLoaiCongNo)
        btnThemMoi.setOnClickListener(this::themMoiTraNo)
        suggestAdapter = CustomArrayAdapter(context, android.R.layout.simple_list_item_1)
        edtKHTra.addTextChangedListener(
            NumberTextWatcher(
                edtKHTra,
                suggestAdapter,
                object : CallBackChange {
                    override fun afterEditTextChange(it: Editable?) {
                        KHTra = getRealNumberV2(it)
                        congNoConLai()
                    }
                })
        )
        edtTienMat.addTextChangedListener(
            NumberTextWatcher(
                edtTienMat,
                suggestAdapter,
                object : CallBackChange {
                    override fun afterEditTextChange(it: Editable?) {
                        tienMat = getRealNumberV2(it)
                        congNoConLai()
                    }
                })
        )
        edtTienChuyenKhoan.addTextChangedListener(
            NumberTextWatcher(
                edtTienChuyenKhoan,
                suggestAdapter,
                object : CallBackChange {
                    override fun afterEditTextChange(it: Editable?) {
                        tienCK = getRealNumberV2(it)
                        congNoConLai()
                    }
                })
        )
    }

    @SuppressLint("SetTextI18n")
    override fun initObserver() {
        viewModel.callbackListKH.observe(viewLifecycleOwner, {
            mListCustomer.clear()
            mListCustomer.addAll(it)
        })

        viewModel.callbackCongNoHienTai.observe(viewLifecycleOwner, {
            congNoHienTai = it
            edtCongNoHienTai.setText(CommonUtils.priceWithoutDecimal(it.toDouble()))
        })

        viewModel.callbackTraNoSuccess.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(
                activity,
                "Thông báo",
                "Đã nhập thông tin khách hàng trả nợ thành công"
            ) {
                alertDialog?.dismiss()
                resetAll()
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

    private fun onChooseCustomer(view: View) {
        val doc = DialogList()
        val mArrayList = ArrayList<DialogListModel>()
        mListCustomer.forEach {
            mArrayList.add(DialogListModel(it.customerId ?: "", it.name ?: ""))
        }
        doc.show(
            activity, mArrayList,
            getString(R.string.customer),
            getString(R.string.enter_text_search)
        ) { item ->
            if (AppConstants.NOT_SELECT == item.id) {
                return@show
            }
            resetAll()
            custID = item.id.toInt()
            edtKH.setText(item.name)
            typeRequestCongNo?.let { viewModel.getCongNoHienTai(it, custID!!) }
        }
    }

    private fun onChooseLoaiCongNo(view: View) {
        val doc = DialogList()
        val mArrayList = GetListDataDemo.getListLoaiCongNo()
        doc.show(
            activity, mArrayList,
            "Loại công nợ",
            getString(R.string.enter_text_search)
        ) { item ->
            if (AppConstants.NOT_SELECT == item.id) {
                return@show
            }
            resetAll()
            debitTypeInt = item.id.toInt()
            typeRequestCongNo = mapTypeLoaiCongNo(item.id)
            edtLoaiCongNo.setText(item.name)
            showInputCongNoTien(item.id == "3" || item.id == "4")
            custID?.let { viewModel.getCongNoHienTai(typeRequestCongNo!!, it) }
        }
    }

    private fun mapTypeLoaiCongNo(type: String): String {
        return when (type) {
            "1" -> "TANK12"
            "2" -> "TANK45"
            "3" -> "MONEY_DEBIT"
            "4" -> "ORDER_DEBIT"
            "5" -> "AGENCY_TANK12"
            "6" -> "AGENCY_TANK45"
            else -> ""
        }
    }

    private fun showInputCongNoTien(isCongNoTien: Boolean) {
        if (isCongNoTien) {
            llWrapCongNoTien.visibility = View.VISIBLE
            lblKHTra.visibility = View.GONE
            edtKHTra.visibility = View.GONE
        } else {
            llWrapCongNoTien.visibility = View.GONE
            lblKHTra.visibility = View.VISIBLE
            edtKHTra.visibility = View.VISIBLE
        }
    }

    private fun congNoConLai() {
        congNoConLai = congNoHienTai - KHTra - tienMat - tienCK
        edtCongNoConLai.setText(CommonUtils.priceWithoutDecimal(congNoConLai.toDouble()))
    }

    private fun resetAll() {
        congNoHienTai = 0
        KHTra = 0
        tienMat = 0
        tienCK = 0
        congNoConLai = 0

        edtCongNoHienTai.setText("")
        edtKHTra.setText("")
        edtTienMat.setText("")
        edtTienChuyenKhoan.setText("")
        edtCongNoConLai.setText("")
    }

    private fun themMoiTraNo(view: View) {

        if (TextUtils.isEmpty(edtKH.text)) {
            showMess("Bạn chưa chọn khách hàng")
            return
        }

        if (TextUtils.isEmpty(edtLoaiCongNo.text)) {
            showMess("Bạn chưa chọn Loại công nợ")
            return
        }

        if (TextUtils.isEmpty(edtCongNoHienTai.text)) {
            showMess("Thiếu thông tin Công nợ hiện tại")
            return
        }

        if (edtKHTra.isVisible) {
            if (TextUtils.isEmpty(edtKHTra.text)) {
                showMess("Bạn chưa nhập số lượng Khách hàng trả")
                return
            }
        } else {
            if (TextUtils.isEmpty(edtTienMat.text) && TextUtils.isEmpty(edtTienChuyenKhoan.text)) {
                showMess("Bạn chưa nhập số tiền trả")
                return
            }
        }

        if (TextUtils.isEmpty(edtCongNoConLai.text)) {
            showMess("Thiếu thông tin Công nợ còn lại")
            return
        }

        val obj = InitTraNo().apply {
            this.custId = custID
            this.debitType = debitTypeInt
            this.amountCash = if (tienMat > 0) tienMat else null
            this.amountTransfer = if (tienCK > 0) tienCK else null
            this.tankAmount = if (KHTra > 0) KHTra else null
        }
        CommonUtils.showConfirmDiglog2Button(
            activity,
            "Xác nhận",
            "Bạn có chắc chắn nhập thông tin khách hàng trả nợ không?",
            getString(
                R.string.biometric_negative_button_text
            ),
            getString(R.string.text_ok)
        ) {
            if (it == AppConstants.YES) {
                viewModel.initTraNo(obj)
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