package vn.gas.thq.ui.retail

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_container_retail.*
import kotlinx.android.synthetic.main.fragment_dang_ky_nghi.*
import kotlinx.android.synthetic.main.fragment_qlyc_ca_nhan.*
import kotlinx.android.synthetic.main.fragment_retail.*
import kotlinx.android.synthetic.main.item_product_type_6.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.customview.CustomArrayAdapter
import vn.gas.thq.customview.ItemProductType1
import vn.gas.thq.customview.ItemProductType2
import vn.gas.thq.model.ProductRetailModel
import vn.gas.thq.model.TransferRetailModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.nhapkho.ProductNhapKhoModel
import vn.gas.thq.ui.pheduyetgiabanle.HistoryAcceptBanLeAdapter
import vn.gas.thq.ui.pheduyetgiabanle.HistoryModel
import vn.gas.thq.ui.qlyeucaucanhan.QLYCCaNhanFragment
import vn.gas.thq.util.*
import vn.gas.thq.util.AppDateUtils.*
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.hongha.ga.R
import java.util.*

@SuppressLint("SetTextI18n")
class RetailFragment : BaseFragment() {
    private var custId: String? = null
    private lateinit var viewModel: RetailViewModel
    private var mListCustomer = mutableListOf<Customer>()
    private var listHistory = mutableListOf<HistoryModel>()
    private var alertDialog: AlertDialog? = null
    private var alertDialog2: AlertDialog? = null

    private lateinit var adapterHistory: HistoryAcceptBanLeAdapter

    private var giaTank12: Int? = 0
    private var giaTank45: Int? = 0
    private var transferRetailModel: TransferRetailModel? = null
    private var tienKhiBan12 = 0
    private var tienKhiBan45 = 0
    private var tienVoBan12 = 0
    private var tienVoBan45 = 0
    private var tienVoMua12 = 0
    private var tienVoMua45 = 0
    private var tongTien = 0
    private var tienNo = 0

    //    private var tienThucTe = 0
    private var tienMatTT = 0
    private var tienCKTT = 0

    private var gasRemain = 0f
    private var gasPrice = 0

    private var hinhThucChuyenKhoan = 1

    //    private var banKhi12 = productBanKhi12.getEditTextSL().text.toString()
//    private var banKhi45 = productBanKhi45.getEditTextSL().text.toString()
    private lateinit var suggestAdapter: CustomArrayAdapter
    private lateinit var suggestAdapter2: CustomArrayAdapter

    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    private var PERMISSION_ALL = 1
    private var PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    companion object {
        @JvmStatic
        fun newInstance(step: String?, transferData: TransferRetailModel?): RetailFragment {
            val bundle = Bundle()
            bundle.apply {
                putString("STEP", step)
                transferData?.let { putSerializable("DATA", it) }
            }
            val fragment = RetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_retail
    }

    override fun initView() {
//        imgBack.setOnClickListener {
//            viewController?.popFragment()
//        }
        if ("STEP_2" == arguments?.getString("STEP")) {
            (parentFragment as RetailContainerFragment).stepView.setStepDone("2")
            edtCustomer.visibility = View.GONE
            layoutCustomerInfo.visibility = View.VISIBLE
            linearGasRemain.visibility = View.VISIBLE
            linearGasRemainPrice.visibility = View.VISIBLE
            linearTienGasDu.visibility = View.VISIBLE
            productVoThuHoi12.visibility = View.GONE
            productVoThuHoi45.visibility = View.GONE
            btnSubmit.text = "BÁN HÀNG"
//            tvHistory.visibility = View.VISIBLE
            transferRetailModel = arguments?.getSerializable("DATA") as TransferRetailModel?

            disableInput()
            return
        }
        linearGasRemain.visibility = View.GONE
        linearGasRemainPrice.visibility = View.GONE
        layoutThuHoiStep2.visibility = View.GONE
        tvHistory.visibility = View.GONE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(context, *PERMISSIONS)) {
                requestPermissions(
                    PERMISSIONS,
                    PERMISSION_ALL
                ) // cai nay use cho Fragment, ActivityCompat use cho Activity
                return
            }
            getLocation()
        }
//        viewModel.onGetListCustomer("21", "105")
    }

    override fun initObserver() {
        viewModel.mLiveDataCustomer.observe(viewLifecycleOwner, {
            mListCustomer.addAll(it)
        })

        viewModel.giaTANK12.observe(viewLifecycleOwner, {
            giaTank12 = it
            productVoMua12.setGia(giaTank12?.toString())
            productVoBan12.setGia(giaTank12?.toString())
        })

        viewModel.giaTANK45.observe(viewLifecycleOwner, {
            giaTank45 = it
            productVoMua45.setGia(giaTank45?.toString())
            productVoBan45.setGia(giaTank45?.toString())
        })

        viewModel.initRequestSuccess.observe(viewLifecycleOwner, {
            handleNextPage(it)
        })

        viewModel.doRetailSuccess.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(activity, "Thông báo", "Bán thành công") {
                alertDialog?.dismiss()
                viewController?.popFragment()
            }
        })

        viewModel.callbackHistory.observe(viewLifecycleOwner, {
            listHistory.clear()
            listHistory.addAll(it)
//            adapterHistory.notifyDataSetChanged()
            showDiglogHistory()
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

    override fun setViewController() {
        viewController = (activity as MainActivity).viewController
        childViewController = (parentFragment as RetailContainerFragment).childViewController
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
                .get(RetailViewModel::class.java)
    }

    override fun initData() {
        edtCustomer.setOnClickListener(this::chooseCustomer)
        btnSubmit.setOnClickListener(this::onSubmitData)
        tvHistory.setOnClickListener {
            viewModel.getHistoryAcceptRetail(transferRetailModel?.orderId!!.toInt())
        }

        tvLabelThuHoiVo.setOnClickListener { this.expand(tvLabelThuHoiVo, linearThuHoiVo) }
        tvLabelBanVo.setOnClickListener { this.expand(tvLabelBanVo, linearBanVo) }
        tvLabelMuaVo.setOnClickListener { this.expand(tvLabelMuaVo, linearMuaVo) }
        tvLabelBanKhi.setOnClickListener { this.expand(tvLabelBanKhi, linearBanKhi) }
        tvLabelCongNoKH.setOnClickListener { this.expand(tvLabelCongNoKH, linearCongNoKH) }

        edtExpireDate.setOnClickListener {
            CommonUtils.showCalendarDialog(
                context,
                edtExpireDate.text.toString()
            ) { strDate ->
                val expDate = changeDateFormat(
                    FORMAT_2,
                    FORMAT_5,
                    strDate
                )
                val nowDate = changeDateFormat(
                    FORMAT_2,
                    FORMAT_5,
                    getCurrentDate()
                )
                if (validateEndDateGreaterorEqualThanStartDate(nowDate, expDate)) {
                    edtExpireDate.setText(strDate)
                } else {
                    showMess("Ngày hẹn không được nhỏ hơn ngày hiện tại")
                }
            }
        }

//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        suggestAdapter = CustomArrayAdapter(context, android.R.layout.simple_list_item_1)
        suggestAdapter2 = CustomArrayAdapter(context, android.R.layout.simple_list_item_1)


        handleBanKhiChange(
            productBanKhi12,
            productVoThuHoi12,
            edtSLHongHa12,
            edtSLKhac12,
            edtTongSL12,
            productVoBan12,
            productVoMua12,
            btnCongNo12,
            tvTienKhi12
        )
        handleBanKhiChange(
            productBanKhi45,
            productVoThuHoi45,
            edtSLHongHa45,
            edtSLKhac45,
            edtTongSL45,
            productVoBan45,
            productVoMua45,
            btnCongNo45,
            tvTienKhi45
        )

        radioTienMat.setOnCheckedChangeListener { _, _ ->
            run {
                hinhThucChuyenKhoan = if (radioTienMat.isChecked) 1 else 2
            }
        }
//        edtTienThucTe.addTextChangedListener(
//            NumberTextWatcher(
//                edtTienThucTe,
//                suggestAdapter2,
//                object : CallBackChange {
//                    override fun afterEditTextChange(it: Editable?) {
//                        tienThucTe = getRealNumberV2(it)
//                        totalDebit()
//                    }
//                })
//        )

        edtTienMat.addTextChangedListener(
            NumberTextWatcher(
                edtTienMat,
                suggestAdapter2,
                object : CallBackChange {
                    override fun afterEditTextChange(it: Editable?) {
                        tienMatTT = getRealNumberV2(it)
                        totalDebit()
                    }
                })
        )

        edtTienChuyenKhoan.addTextChangedListener(
            NumberTextWatcher(
                edtTienChuyenKhoan,
                suggestAdapter2,
                object : CallBackChange {
                    override fun afterEditTextChange(it: Editable?) {
                        tienCKTT = getRealNumberV2(it)
                        totalDebit()
                    }
                })
        )

//        edtTienThucTe.setAdapter(suggestAdapter2)

        edtGasRemain.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(3, 1))
        edtGasRemain.addTextChangedListener(afterTextChanged = {
            gasRemain = getRealNumberFloat(it)
            totalGasPrice(
                getRealNumber(productBanKhi12.getEditTextGia()),
                getRealNumber(productBanKhi45.getEditTextGia())
            )
        })

        fillData(transferRetailModel)
    }

    private fun handleNextPage(it: ResponseInitRetail?) {
        if (it?.needApprove == true) {
            CommonUtils.showDiglog1Button(activity, "Thông báo", "Hoàn thành") {
                alertDialog?.dismiss()
                viewController?.pushFragment(
                    ScreenId.SCREEN_QLYC_CA_NHAN,
                    QLYCCaNhanFragment.newInstance(ScreenId.SCREEN_RETAIL_STEP_1)
                )
            }
            return
        }
        val customer = Customer().apply {
            name = tvCustName.text?.toString() ?: ""
            customerId = tvCustId.text?.toString() ?: ""
            telContact = tvPhoneNumber.text?.toString() ?: ""
            address = tvAddress.text?.toString() ?: ""
        }
        transferRetailModel = TransferRetailModel(
            it?.id?.toString(),
            customer,
            getRealNumber(productBanKhi12.getEditTextSL()),
            getRealNumber(productBanKhi12.getEditTextGia()),
            getRealNumber(productBanKhi45.getEditTextSL()),
            getRealNumber(productBanKhi45.getEditTextGia()),
            getRealNumber(productVoThuHoi12.getViewSL()),
            getRealNumber(productVoThuHoi45.getViewSL()),
            getRealNumber(productVoBan12.getEditTextSL()),
            getRealNumber(productVoBan12.getEditTextGia()),
            getRealNumber(productVoBan45.getEditTextSL()),
            getRealNumber(productVoBan45.getEditTextGia()),
            getRealNumber(productVoMua12.getEditTextSL()),
            getRealNumber(productVoMua12.getEditTextGia()),
            getRealNumber(productVoMua45.getEditTextSL()),
            getRealNumber(productVoMua45.getEditTextGia()),
            null,
            null,
            null,
            tienMatTT,
            tienCKTT,
            edtExpireDate.text.toString()
        )
        childViewController?.pushFragment(
            ScreenId.SCREEN_RETAIL_STEP_2,
            newInstance("STEP_2", transferRetailModel)
        )
        (parentFragment as RetailContainerFragment).stepView.setStepDone("2")
    }

    private fun onSubmitData(view: View) {
        // Neu la buoc 2
        if ("STEP_2" == arguments?.getString("STEP")) {

            CommonUtils.showConfirmDiglog2Button(
                activity,
                "Xác nhận",
                "Bạn có chắc chắn thực hiện bán hàng cho khách hàng ${tvCustName.text}??",
                getString(
                    R.string.biometric_negative_button_text
                ),
                getString(R.string.text_ok)
            ) {
                if (it == AppConstants.YES) {
                    val gasRemainModel = GasRemainModel()
                    gasRemainModel.gasRemain = this.gasRemain
                    gasRemainModel.returnItem = mutableListOf<ProductNhapKhoModel>().apply {
                        add(ProductNhapKhoModel().apply {
                            productCode = "TANK12_OTHER"
                            amount = getRealNumber(edtSLKhac12)
                        })
                        add(ProductNhapKhoModel().apply {
                            productCode = "TANK45_OTHER"
                            amount = getRealNumber(edtSLKhac45)
                        })
                        add(ProductNhapKhoModel().apply {
                            productCode = "TANK12"
                            amount = getRealNumber(edtSLHongHa12)
                        })
                        add(ProductNhapKhoModel().apply {
                            productCode = "TANK45"
                            amount = getRealNumber(edtSLHongHa45)
                        })
                    }
                    viewModel.doRetailLXBH(transferRetailModel?.orderId, gasRemainModel)
                }
            }
            return
        }
        if (layoutCustomerInfo.visibility == View.GONE) {
            showMess("Vui lòng chọn khách hàng")
            return
        }
        if (checkGia() != null) {
            showMess(checkGia())
            return
        }
        val requestInitRetail = RequestInitRetail()
        requestInitRetail.customerId = custId?.toInt()
        requestInitRetail.debit = tienNo
        requestInitRetail.lat = latitude.toFloat()
        requestInitRetail.lng = longitude.toFloat()
        requestInitRetail.debtExpireDate = changeDateFormatV2(
            FORMAT_2,
            FORMAT_6,
            tomorrowDateInput(edtExpireDate.text.toString(), FORMAT_2)
        )
        val listProductRetailModel = mutableListOf<ProductRetailModel>()
        listProductRetailModel.add(
            ProductRetailModel(
                "GAS12",
                getRealNumber(productBanKhi12.getEditTextSL()),
                getRealNumber(productBanKhi12.getEditTextGia()),
                "1"
            )
        )
        listProductRetailModel.add(
            ProductRetailModel(
                "GAS45",
                getRealNumber(productBanKhi45.getEditTextSL()),
                getRealNumber(productBanKhi45.getEditTextGia()),
                "1"
            )
        )
        listProductRetailModel.add(
            ProductRetailModel(
                "TANK12",
                getRealNumber(productVoThuHoi12.getViewSL()),
                0,
                "2"
            )
        )
        listProductRetailModel.add(
            ProductRetailModel(
                "TANK45",
                getRealNumber(productVoThuHoi45.getViewSL()),
                0,
                "2"
            )
        )
        listProductRetailModel.add(
            ProductRetailModel(
                "TANK12",
                getRealNumber(productVoBan12.getEditTextSL()),
                getRealNumber(productVoBan12.getEditTextGia()),
                "1"
            )
        )
        listProductRetailModel.add(
            ProductRetailModel(
                "TANK45",
                getRealNumber(productVoBan45.getEditTextSL()),
                getRealNumber(productVoBan45.getEditTextGia()),
                "1"
            )
        )
        listProductRetailModel.add(
            ProductRetailModel(
                "TANK12",
                getRealNumber(productVoMua12.getEditTextSL()),
                getRealNumber(productVoMua12.getEditTextGia()),
                "3"
            )
        )
        listProductRetailModel.add(
            ProductRetailModel(
                "TANK45",
                getRealNumber(productVoMua45.getEditTextSL()),
                getRealNumber(productVoMua45.getEditTextGia()),
                "3"
            )
        )
        requestInitRetail.item = listProductRetailModel
//        requestInitRetail.payMethod = hinhThucChuyenKhoan
        val listTienCK = mutableListOf<PaidAmoutModel>()
        listTienCK.add(PaidAmoutModel().apply {
            amount = tienMatTT
            pay_method = 1
        })
        listTienCK.add(PaidAmoutModel().apply {
            amount = tienCKTT
            pay_method = 2
        })
        requestInitRetail.paidAmounts = listTienCK
        CommonUtils.showConfirmDiglog2Button(
            activity,
            "Xác nhận",
            "KH đã thanh toán ${CommonUtils.priceWithoutDecimal((tienMatTT + tienCKTT).toDouble())} đ \nvà đang nợ ${
                CommonUtils.priceWithoutDecimal(
                    tienNo.toDouble()
                )
            } đ\nBạn có chắc chắn muốn tạo yêu cầu bán lẻ?",
            getString(
                R.string.biometric_negative_button_text
            ),
            getString(R.string.text_ok)
        ) {
            if (it == AppConstants.YES) {
                viewModel.doRequestRetail(requestInitRetail)
            }
        }
    }

    private fun checkGia(): String? {
        if (getRealNumber(productBanKhi12.getEditTextSL()) > 0 && getRealNumber(productBanKhi12.getEditTextGia()) < 1000) {
            return "Giá khí 12kg bạn đang nhập là ${productBanKhi12.getEditTextGia().text}, nhỏ hơn so với quy định.\nKhông cho phép tạo đơn hàng"
        }
        if (getRealNumber(productBanKhi45.getEditTextSL()) > 0 && getRealNumber(productBanKhi45.getEditTextGia()) < 1000) {
            return "Giá khí 45kg bạn đang nhập là ${productBanKhi45.getEditTextGia().text}, nhỏ hơn so với quy định.\nKhông cho phép tạo đơn hàng"
        }
        if (getRealNumber(productVoBan12.getEditTextSL()) > 0 && getRealNumber(productVoBan12.getEditTextGia()) < 1000) {
            return "Giá bán vỏ 12kg bạn đang nhập là ${productVoBan12.getEditTextGia().text}, nhỏ hơn so với quy định.\nKhông cho phép tạo đơn hàng"
        }
        if (getRealNumber(productVoBan45.getEditTextSL()) > 0 && getRealNumber(productVoBan45.getEditTextGia()) < 1000) {
            return "Giá bán vỏ 45kg bạn đang nhập là ${productVoBan45.getEditTextGia().text}, nhỏ hơn so với quy định.\nKhông cho phép tạo đơn hàng"
        }

        if (getRealNumber(productVoMua12.getEditTextSL()) > 0 && getRealNumber(productVoMua12.getEditTextGia()) < 1000) {
            return "Giá mua vỏ 12kg bạn đang nhập là ${productVoMua12.getEditTextGia().text}, nhỏ hơn so với quy định.\nKhông cho phép tạo đơn hàng"
        }
        if (getRealNumber(productVoMua45.getEditTextSL()) > 0 && getRealNumber(productVoMua45.getEditTextGia()) < 1000) {
            return "Giá mua vỏ 45kg bạn đang nhập là ${productVoBan45.getEditTextGia().text}, nhỏ hơn so với quy định.\nKhông cho phép tạo đơn hàng"
        }
        return null
    }

    private fun chooseCustomer(view: View) {
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
//            status = item.id
            edtCustomer.setText(item.name)
            expandCustomerInfor(item.id)
            viewModel.getGiaNiemYet(item.id)
        }
    }

    private fun expandCustomerInfor(id: String) {
        layoutCustomerInfo.visibility = View.VISIBLE
        mListCustomer.forEach {
            if (it.customerId == id) {
                tvCustName.text = it.name
                tvCustId.text = it.customerId
                tvPhoneNumber.text = it.telContact
                tvAddress.text = it.address

                custId = it.customerId
            }
        }
    }

    private fun expand(titleGroup: TextView, container: View) {
        if (container.isVisible) {
            titleGroup.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_right_circle,
                0
            )
            CommonUtils.collapse(container)
        } else {
            titleGroup.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_down_circle,
                0
            )
            CommonUtils.expand(container)
        }
    }

    private fun handleBanKhiChange(
        bankhi: ItemProductType1,
        thuHoiVo: ItemProductType2,
        thuHoiVoHongHa: EditText,
        thuHoiVoKhac: EditText,
        thuHoiVoTong: EditText,
        voBan: ItemProductType1,
        voMua: ItemProductType1,
        congno: Button,
        tienKhiBan: TextView
    ) {
        bankhi.getEditTextSL().addTextChangedListener(afterTextChanged = {
            thuHoiVo.setSoLuong(it.toString())
            val slBanKhi = getRealNumberV2(it)
            val giaBanKhi = getRealNumber(bankhi.getEditTextGia())
            tienKhiBan.text =
                "${CommonUtils.priceWithoutDecimal((slBanKhi * giaBanKhi).toDouble())} đ"
            if (tienKhiBan == tvTienKhi12) {
                tienKhiBan12 = slBanKhi * giaBanKhi
            } else {
                tienKhiBan45 = slBanKhi * giaBanKhi
            }
            totalMustPay()
            totalDebit()
        })

        bankhi.getEditTextGia().setAdapter(suggestAdapter)
        bankhi.getEditTextGia().addTextChangedListener(
            NumberTextWatcher(bankhi.getEditTextGia(), suggestAdapter, object : CallBackChange {
                override fun afterEditTextChange(it: Editable?) {
                    val slBanKhi = getRealNumber(bankhi.getEditTextSL())
                    val giaBanKhi = getRealNumberV2(it)
                    tienKhiBan.text =
                        "${CommonUtils.priceWithoutDecimal((slBanKhi * giaBanKhi).toDouble())} đ"

                    if (tienKhiBan == tvTienKhi12) {
                        tienKhiBan12 = slBanKhi * giaBanKhi
                    } else {
                        tienKhiBan45 = slBanKhi * giaBanKhi
                    }
                    totalMustPay()
                    totalDebit()
                }

            })
        )

        thuHoiVoHongHa.addTextChangedListener(afterTextChanged = {
            val thuHoiVoHongHa = getRealNumberV2(it)
            val thuHoiVoKhac = getRealNumber(thuHoiVoKhac)
            val thuHoiVoTongNumber = thuHoiVoHongHa + thuHoiVoKhac
            thuHoiVoTong.setText(thuHoiVoTongNumber.toString())
        })

        thuHoiVoKhac.addTextChangedListener(afterTextChanged = {
            val thuHoiVoKhac = getRealNumberV2(it)
            val thuHoiVoHongHa = getRealNumber(thuHoiVoHongHa)
            val thuHoiVoTongNumber = thuHoiVoHongHa + thuHoiVoKhac
            thuHoiVoTong.setText(thuHoiVoTongNumber.toString())
        })

        thuHoiVoTong.addTextChangedListener(afterTextChanged = {
            val banKhi = getRealNumber(bankhi.getEditTextSL())
            val thuHoiVo = getRealNumberV2(it)
            val voBan = getRealNumber(voBan.getEditTextSL())

            congno.text = (banKhi - thuHoiVo - voBan).toString()
        })

        // se la tong so
        thuHoiVo.getViewSL().addTextChangedListener(afterTextChanged = {
            val banKhi = getRealNumber(bankhi.getEditTextSL())
            val thuHoiVo = getRealNumberV2(it)
            val voBan = getRealNumber(voBan.getEditTextSL())

            congno.text = (banKhi - thuHoiVo - voBan).toString()
        })

        voBan.getEditTextSL().addTextChangedListener(afterTextChanged = {
            val banKhi = getRealNumber(bankhi.getEditTextSL())
            val thuHoiVo = getRealNumber(thuHoiVo.getViewSL())
            val slVoBan = getRealNumberV2(it)
            val giaVoBan = getRealNumber(voBan.getEditTextGia())

            congno.text = (banKhi - thuHoiVo - slVoBan).toString()
            val tienBanVoComponent = slVoBan * giaVoBan
            if (voBan == productVoBan12) {
                tienVoBan12 = tienBanVoComponent
                tvTienBanVo.text =
                    "${CommonUtils.priceWithoutDecimal((tienVoBan45 + tienBanVoComponent).toDouble())} đ"
            } else {
                tienVoBan45 = tienBanVoComponent
                tvTienBanVo.text =
                    "${CommonUtils.priceWithoutDecimal((tienVoBan12 + tienBanVoComponent).toDouble())} đ"
            }

            totalMustPay()
            totalDebit()
        })

        voBan.getEditTextGia().setAdapter(suggestAdapter)
        voBan.getEditTextGia().addTextChangedListener(
            NumberTextWatcher(
                voBan.getEditTextGia(),
                suggestAdapter,
                object : CallBackChange {
                    override fun afterEditTextChange(it: Editable?) {
                        val giaVoBan = getRealNumberV2(it)
                        val slVoBan = getRealNumber(voBan.getEditTextSL())

                        val tienBanVoComponent = slVoBan * giaVoBan
                        if (voBan == productVoBan12) {
                            tienVoBan12 = tienBanVoComponent
                            tvTienBanVo.text =
                                "${CommonUtils.priceWithoutDecimal((tienVoBan45 + tienBanVoComponent).toDouble())} đ"
                        } else {
                            tienVoBan45 = tienBanVoComponent
                            tvTienBanVo.text =
                                "${CommonUtils.priceWithoutDecimal((tienVoBan12 + tienBanVoComponent).toDouble())} đ"
                        }

                        totalMustPay()
                        totalDebit()
                    }
                })
        )

        voMua.getEditTextSL().addTextChangedListener(afterTextChanged = {
            val slVoMua = getRealNumberV2(it)
            val giaVoMua = getRealNumber(voMua.getEditTextGia())
            val tienMuaVoComponent = slVoMua * giaVoMua

            if (voMua == productVoMua12) {
                tienVoMua12 = tienMuaVoComponent
                if (tienVoMua45 + tienMuaVoComponent == 0) {
                    tvTienMuaVo.text =
                        "0 đ"
                } else {
                    tvTienMuaVo.text =
                        "-${CommonUtils.priceWithoutDecimal((tienVoMua45 + tienMuaVoComponent).toDouble())} đ"
                }
            } else {
                tienVoMua45 = tienMuaVoComponent
                if (tienVoMua12 + tienMuaVoComponent == 0) {
                    tvTienMuaVo.text = "0 đ"
                } else {
                    tvTienMuaVo.text =
                        "-${CommonUtils.priceWithoutDecimal((tienVoMua12 + tienMuaVoComponent).toDouble())} đ"
                }
            }

            totalMustPay()
            totalDebit()
        })

        voMua.getEditTextGia().setAdapter(suggestAdapter)
        voMua.getEditTextGia().addTextChangedListener(
            NumberTextWatcher(
                voMua.getEditTextGia(),
                suggestAdapter,
                object : CallBackChange {
                    override fun afterEditTextChange(it: Editable?) {
                        val slVoMua = getRealNumber(voMua.getEditTextSL())
                        val giaVoMua = getRealNumberV2(it)
                        val tienMuaVoComponent = slVoMua * giaVoMua

                        if (voMua == productVoMua12) {
                            tienVoMua12 = tienMuaVoComponent
                            if (tienVoMua45 + tienMuaVoComponent == 0) {
                                tvTienMuaVo.text =
                                    "0 đ"
                            } else {
                                tvTienMuaVo.text =
                                    "-${CommonUtils.priceWithoutDecimal((tienVoMua45 + tienMuaVoComponent).toDouble())} đ"
                            }
                        } else {
                            tienVoMua45 = tienMuaVoComponent
                            if (tienVoMua12 + tienMuaVoComponent == 0) {
                                tvTienMuaVo.text = "0 đ"
                            } else {
                                tvTienMuaVo.text =
                                    "-${CommonUtils.priceWithoutDecimal((tienVoMua12 + tienMuaVoComponent).toDouble())} đ"
                            }
                        }

                        totalMustPay()
                        totalDebit()
                    }
                })
        )
    }

    //TODO: Tổng tiền KH cần thanh toán
    private fun totalMustPay() {
        tongTien =
            tienKhiBan12 + tienKhiBan45 + tienVoBan12 + tienVoBan45 - (tienVoMua12 + tienVoMua45) - gasPrice
        tvTongTienCanTT.text = "${CommonUtils.priceWithoutDecimal(tongTien.toDouble())} đ"
        if ("STEP_2" != arguments?.getString("STEP"))
            edtTienMat.setText(tongTien.toString())
    }

    private fun totalGasPrice(khiPrice12: Int, khiPrice45: Int) {
        var giaKhi = khiPrice45 / 45
        if (khiPrice45 == 0) {
            giaKhi = khiPrice12 / 12
        }
        gasPrice = lamTronGasPrice((gasRemain * giaKhi).toInt().toString())
        totalMustPay()
        tinhTienSauKhiNhapGasDu(gasPrice)
        totalDebit()
        edtGasRemainPrice.setText(CommonUtils.priceWithoutDecimal(gasPrice.toDouble()))
        if (gasPrice == 0) {
            tvTienGasDu.text = "${CommonUtils.priceWithoutDecimal(gasPrice.toDouble())} đ"
            return
        }
        tvTienGasDu.text = "-${CommonUtils.priceWithoutDecimal(gasPrice.toDouble())} đ"

    }

    private fun lamTronGasPrice(oldTongTien: String): Int {
        if (oldTongTien.toInt() < 500) return 0
        else {
            val hangNghin = oldTongTien.toInt() / 1000
            val soLe = oldTongTien.toInt() % 1000
            if (soLe >= 500) {
                return hangNghin * 1000 + 1000
            } else {
                return hangNghin * 1000
            }
        }
    }

    private fun totalDebit() {
        tienNo =
            if (tongTien - (tienMatTT + tienCKTT) > 0) (tongTien - (tienMatTT + tienCKTT)) else 0
        btnCongNoTien.text = CommonUtils.priceWithoutDecimal(tienNo.toDouble())
        tvTienNo.text = "${CommonUtils.priceWithoutDecimal(tienNo.toDouble())} đ"

        edtExpireDate.isEnabled = tienNo > 0
    }

    private fun getRealNumber(view: EditText): Int {
        return if (TextUtils.isEmpty(
                view.text.toString().trim()
            )
        ) 0 else CommonUtils.getIntFromStringDecimal(
            view.text.toString()
                .trim()
        )
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

    private fun getRealNumberFloat(view: Editable?): Float {
        return if (TextUtils.isEmpty(
                view.toString().trim()
            )
        ) 0f else CommonUtils.getFloatFromStringDecimal(
            view.toString()
                .trim()
        )
    }

    private fun tinhTienSauKhiNhapGasDu(soTienGasDu: Int) {
        var tienMatTemp = CommonUtils.getIntFromStringDecimal(edtTienMat.text.toString())
        var tienCKTemp = CommonUtils.getIntFromStringDecimal(edtTienChuyenKhoan.text.toString())
        if (soTienGasDu <= tienMatTemp) {
            tienMatTemp -= soTienGasDu
        } else {
            val delta = soTienGasDu - tienMatTemp
            tienCKTemp -= tienCKTemp - delta
        }
        edtTienMat.setText(CommonUtils.priceWithoutDecimal(tienMatTemp.toDouble()))
        edtTienChuyenKhoan.setText(CommonUtils.priceWithoutDecimal(tienCKTemp.toDouble()))
    }

    private fun disableInput() {
        productBanKhi12.getEditTextSL().isFocusable = false
        productBanKhi12.getEditTextSL().isEnabled = false
        productBanKhi12.getEditTextGia().isFocusable = false
        productBanKhi12.getEditTextGia().isEnabled = false

        productBanKhi45.getEditTextSL().isFocusable = false
        productBanKhi45.getEditTextSL().isEnabled = false
        productBanKhi45.getEditTextGia().isFocusable = false
        productBanKhi45.getEditTextGia().isEnabled = false

        productVoThuHoi12.getViewSL().isFocusable = false
        productVoThuHoi12.getViewSL().isEnabled = false
        productVoThuHoi45.getViewSL().isFocusable = false
        productVoThuHoi45.getViewSL().isEnabled = false

        productVoBan12.getEditTextSL().isFocusable = false
        productVoBan12.getEditTextSL().isEnabled = false
        productVoBan12.getEditTextGia().isFocusable = false
        productVoBan12.getEditTextGia().isEnabled = false

        productVoBan45.getEditTextSL().isFocusable = false
        productVoBan45.getEditTextSL().isEnabled = false
        productVoBan45.getEditTextGia().isFocusable = false
        productVoBan45.getEditTextGia().isEnabled = false

        productVoMua12.getEditTextSL().isFocusable = false
        productVoMua12.getEditTextSL().isEnabled = false
        productVoMua12.getEditTextGia().isFocusable = false
        productVoMua12.getEditTextGia().isEnabled = false

        productVoMua45.getEditTextSL().isFocusable = false
        productVoMua45.getEditTextSL().isEnabled = false
        productVoMua45.getEditTextGia().isFocusable = false
        productVoMua45.getEditTextGia().isEnabled = false

        Handler(Looper.getMainLooper()).postDelayed({
            edtExpireDate.isEnabled = false
        }, 1000)

        edtTienThucTe.isFocusable = false
        edtTienThucTe.isEnabled = false
    }

    private fun fillData(obj: TransferRetailModel?) {
        if (obj == null) return

        tvCustName.text = obj.customerInfo?.name
        tvCustId.text = obj.customerInfo?.customerId
        tvPhoneNumber.text = obj.customerInfo?.telContact
        tvAddress.text = obj.customerInfo?.address

        productBanKhi12.setSoLuong(obj.khiBan12?.toString())
        productBanKhi12.setGia("${obj.khiBanPrice12}")
        productBanKhi45.setSoLuong(obj.khiBan45?.toString())
        productBanKhi45.setGia("${obj.khiBanPrice45}")

        edtSLHongHa12.setText(obj.voThu12?.toString())
        edtSLHongHa45.setText(obj.voThu45?.toString())

        productVoBan12.setSoLuong(obj.voBan12?.toString())
        productVoBan12.setGia("${obj.voBanPrice12}")
        productVoBan45.setSoLuong(obj.voBan45?.toString())
        productVoBan45.setGia("${obj.voBanPrice45}")

        productVoMua12.setSoLuong(obj.voMua12?.toString())
        productVoMua12.setGia("${obj.voMuaPrice12}")
        productVoMua45.setSoLuong(obj.voMua45?.toString())
        productVoMua45.setGia("${obj.voMuaPrice45}")

        edtExpireDate.setText(obj.ngayHenTra)

        edtTienThucTe.setText(obj.tienThucTe?.toString())
        edtTienMat.setText(CommonUtils.priceWithoutDecimal(obj.tienMatTT?.toDouble()))
        edtTienChuyenKhoan.setText(obj.tienCKTT?.toString())
    }

    private fun hasPermissions(context: Context?, vararg permissions: String?): Boolean {
        if (context != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_ALL -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    getLocation()
                } else {
//                    showMess("NON-ACCEPT")
                }
                return
            }
        }
    }

    private fun getLocation() {
        val location = getLastKnownLocation()
        longitude = location?.longitude ?: 0.0
        latitude = location?.latitude ?: 0.0
        viewModel.onGetListCustomer(
            latitude.toString(),
            longitude.toString()
        )
        Log.e("PHUC", "$longitude : $latitude")
    }

    private fun getLastKnownLocation(): Location? {
        val mLocationManager: LocationManager = context?.getSystemService(
            AppCompatActivity.LOCATION_SERVICE
        ) as LocationManager
        val providers: List<String> = mLocationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                break
            }
            val l: Location = mLocationManager.getLastKnownLocation(provider)
                ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                // Found best last known location: %s", l);
                bestLocation = l
            }
        }
        return bestLocation
    }

    private fun showDiglogHistory() {
        val builder = context?.let { AlertDialog.Builder(it, R.style.AlertDialogNoBG) }
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.layout_dialog_item_history, null)
        builder?.setView(dialogView)
//        val tvTitle1: TextView = dialogView.findViewById(R.id.tvTitle1)
        val imgClose1: ImageView = dialogView.findViewById(R.id.imgClose1)
        val rvHistory: RecyclerView = dialogView.findViewById(R.id.rvHistory)
        imgClose1.setOnClickListener {
            alertDialog2?.dismiss()
        }

        adapterHistory = HistoryAcceptBanLeAdapter(listHistory)

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvHistory.layoutManager = linearLayoutManager
        val dividerItemDecoration = DividerItemDecoration(
            rvHistory.context,
            linearLayoutManager.orientation
        )
        rvHistory.addItemDecoration(dividerItemDecoration)
        rvHistory.adapter = adapterHistory

        alertDialog2 = builder?.create()
        alertDialog2?.window?.setLayout(500, 200)
        alertDialog2?.show()
    }
}