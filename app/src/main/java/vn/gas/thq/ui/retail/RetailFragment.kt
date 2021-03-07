package vn.gas.thq.ui.retail

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_container_retail.*
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
import vn.gas.thq.ui.qlyeucaucanhan.QLYCCaNhanFragment
import vn.gas.thq.util.*
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.hongha.ga.R
import java.util.*


class RetailFragment : BaseFragment() {
    private var custId: String? = null
    private lateinit var viewModel: RetailViewModel
    private var mListCustomer = mutableListOf<Customer>()
    private var alertDialog: AlertDialog? = null

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
    private var tienThucTe = 0
    private var gasRemain = 0f
    private var gasPrice = 0

    private var hinhThucChuyenKhoan = 1

    //    private var banKhi12 = productBanKhi12.getEditTextSL().text.toString()
//    private var banKhi45 = productBanKhi45.getEditTextSL().text.toString()
    private lateinit var suggestAdapter: CustomArrayAdapter

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
            linearGasRemain.visibility = View.VISIBLE
            linearGasRemainPrice.visibility = View.VISIBLE
            linearTienGasDu.visibility = View.VISIBLE
            productVoThuHoi12.visibility = View.GONE
            productVoThuHoi45.visibility = View.GONE
            btnSubmit.text = "BÁN HÀNG"
            transferRetailModel = arguments?.getSerializable("DATA") as TransferRetailModel?

            disableInput()
            return
        }
        linearGasRemain.visibility = View.GONE
        linearGasRemainPrice.visibility = View.GONE
        layoutThuHoiStep2.visibility = View.GONE
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

        tvLabelThuHoiVo.setOnClickListener { this.expand(tvLabelThuHoiVo, linearThuHoiVo) }
        tvLabelBanVo.setOnClickListener { this.expand(tvLabelBanVo, linearBanVo) }
        tvLabelMuaVo.setOnClickListener { this.expand(tvLabelMuaVo, linearMuaVo) }
        tvLabelBanKhi.setOnClickListener { this.expand(tvLabelBanKhi, linearBanKhi) }
        tvLabelCongNoKH.setOnClickListener { this.expand(tvLabelCongNoKH, linearCongNoKH) }

        //        mList.add("34");
//        mList.add("340");
//        mList.add("3400");

//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        suggestAdapter = CustomArrayAdapter(context, android.R.layout.simple_list_item_1)


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
        edtTienThucTe.addTextChangedListener(
            NumberTextWatcher(
                edtTienThucTe,
                suggestAdapter,
                object : CallBackChange {
                    override fun afterEditTextChange(it: Editable?) {
                        tienThucTe = getRealNumberV2(it)
                        totalDebit()
                    }
                })
        )

//        edtTienThucTe.setOnFocusChangeListener { v, hasFocus ->
//            if (!hasFocus) {
//                edtTienThucTe.setText("${edtTienThucTe.text}000")
//            }
//        }

        edtTienThucTe.setAdapter(suggestAdapter)

//        edtGasRemain.addTextChangedListener(
//            NumberTextWatcher(
//                edtGasRemain,
//                suggestAdapter,
//                object : CallBackChange {
//                    override fun afterEditTextChange(it: Editable?) {
//                        gasRemain = getRealNumberV2(it)
//                        totalGasPrice(
//                            getRealNumber(productBanKhi12.getEditTextGia()),
//                            getRealNumber(productBanKhi45.getEditTextGia())
//                        )
//                    }
//                })
//        )
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
        transferRetailModel = TransferRetailModel(
            it?.id?.toString(),
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
            tienThucTe
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
                activity, "Xác nhận", "Bạn có chắc chắn muốn Bán lẻ?", getString(
                    R.string.biometric_negative_button_text
                ), getString(R.string.text_ok)
            ) {
                if (it == AppConstants.YES) {
                    val gasRemainModel = GasRemainModel()
                    gasRemainModel.gasRemain = this.gasRemain
                    viewModel.doRetailLXBH(transferRetailModel?.orderId, gasRemainModel)
                }
            }
            return
        }
        if (layoutCustomerInfo.visibility == View.GONE) {
            showMess("Vui lòng chọn khách hàng")
            return
        }
//        childViewController?.pushFragment(
//            ScreenId.SCREEN_RETAIL_STEP_2,
//            newInstance("STEP_2")
//        )
//        (parentFragment as RetailContainerFragment).stepView.setStepDone("1")
        val requestInitRetail = RequestInitRetail()
        requestInitRetail.customerId = custId?.toInt()
        requestInitRetail.debit = tienNo
        requestInitRetail.lat = latitude.toInt()
        requestInitRetail.lng = longitude.toInt()
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
        requestInitRetail.payMethod = hinhThucChuyenKhoan
        CommonUtils.showConfirmDiglog2Button(
            activity, "Xác nhận", "Bạn có chắc chắn muốn tạo yêu cầu bán lẻ?", getString(
                R.string.biometric_negative_button_text
            ), getString(R.string.text_ok)
        ) {
            if (it == AppConstants.YES) {
                viewModel.doRequestRetail(requestInitRetail)
            }
        }
    }

    private fun chooseCustomer(view: View) {
        var doc = DialogList()
        var mArrayList = ArrayList<DialogListModel>()
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

//        bankhi.getEditTextGia().addTextChangedListener(afterTextChanged = {
//            val slBanKhi = getRealNumber(bankhi.getEditTextSL())
//            val giaBanKhi = getRealNumberV2(it)
//            tienKhiBan.text =
//                "${CommonUtils.priceWithoutDecimal((slBanKhi * giaBanKhi).toDouble())} đ"
//
//            if (tienKhiBan == tvTienKhi12) {
//                tienKhiBan12 = slBanKhi * giaBanKhi
//            } else {
//                tienKhiBan45 = slBanKhi * giaBanKhi
//            }
//            totalMustPay()
//            totalDebit()
//        })

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

    private fun totalMustPay() {
        tongTien =
            tienKhiBan12 + tienKhiBan45 + tienVoBan12 + tienVoBan45 - (tienVoMua12 + tienVoMua45) - gasPrice
        tvTongTienCanTT.text = "${CommonUtils.priceWithoutDecimal(tongTien.toDouble())} đ"
    }

//    private fun realPay() {
//        tienThucTe = tongTien - tienNo
//        btnTienThucTe.text = CommonUtils.priceWithoutDecimal(tienThucTe.toDouble())
//    }

    private fun totalGasPrice(khiPrice12: Int, khiPrice45: Int) {
        var giaKhi = khiPrice45 / 45
        if (khiPrice45 == 0) {
            giaKhi = khiPrice12 / 12
        }
        gasPrice = lamTronGasPrice((gasRemain * giaKhi).toInt().toString())
        totalMustPay()
        totalDebit()
        edtGasRemainPrice.setText("${CommonUtils.priceWithoutDecimal(gasPrice.toDouble())}")
        if (gasPrice == 0) {
            tvTienGasDu.text = "${CommonUtils.priceWithoutDecimal(gasPrice.toDouble())} đ"
            return
        }
        tvTienGasDu.text = "-${CommonUtils.priceWithoutDecimal(gasPrice.toDouble())} đ"

    }

    fun lamTronGasPrice(oldTongTien: String): Int {
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
        tienNo = if (tongTien - tienThucTe > 0) (tongTien - tienThucTe) else 0
        btnCongNoTien.text = CommonUtils.priceWithoutDecimal(tienNo.toDouble())
        tvTienNo.text = "${CommonUtils.priceWithoutDecimal(tienNo.toDouble())} đ"
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

        edtTienThucTe.isFocusable = false
        edtTienThucTe.isEnabled = false
    }

    private fun fillData(obj: TransferRetailModel?) {
        if (obj == null) return
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

        edtTienThucTe.setText(obj.tienThucTe?.toString())
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
            String.format("%.0f", latitude),
            String.format("%.0f", longitude)
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
                    context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context!!,
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
}