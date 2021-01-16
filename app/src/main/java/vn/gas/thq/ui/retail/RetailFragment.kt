package vn.gas.thq.ui.retail

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_container_retail.*
import kotlinx.android.synthetic.main.fragment_qlyc_ca_nhan.*
import kotlinx.android.synthetic.main.fragment_retail.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.customview.ItemProductType1
import vn.gas.thq.customview.ItemProductType2
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.ScreenId
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.hongha.ga.R
import java.util.*


class RetailFragment : BaseFragment() {
    private lateinit var viewModel: RetailViewModel
    private var mListCustomer = mutableListOf<Customer>()
    private var tienKhiBan12 = 0
    private var tienKhiBan45 = 0
    private var tienVoBan12 = 0
    private var tienVoBan45 = 0
    private var tienVoMua12 = 0
    private var tienVoMua45 = 0
    private var tongTien = 0
    private var tienNo = 0
    private var tienThucTe = 0
//    private var banKhi12 = productBanKhi12.getEditTextSL().text.toString()
//    private var banKhi45 = productBanKhi45.getEditTextSL().text.toString()

    companion object {
        @JvmStatic
        fun newInstance(step: String?): RetailFragment {
            val bundle = Bundle()
            bundle.apply {
                putString("STEP", step)
            }
            val fragment = RetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
//        imgBack.setOnClickListener {
//            viewController?.popFragment()
//        }
        if ("STEP_2" == arguments?.getString("STEP")) {
            btnSubmit.text = "BÁN HÀNG"
            return
        }
        viewModel.onGetListCustomer("21", "105")
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_retail
    }

    override fun initObserver() {
        viewModel.mLiveDataCustomer.observe(viewLifecycleOwner, {
            mListCustomer.addAll(it)
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
        btnSubmit.setOnClickListener {
            if ("STEP_2" == arguments?.getString("STEP")) {
                (parentFragment as RetailContainerFragment).stepView.setStepDone("2")
                return@setOnClickListener
            }
            childViewController?.pushFragment(
                ScreenId.SCREEN_RETAIL_STEP_2,
                newInstance("STEP_2")
            )
            (parentFragment as RetailContainerFragment).stepView.setStepDone("1")
        }

        tvLabelThuHoiVo.setOnClickListener { this.expand(tvLabelThuHoiVo, linearThuHoiVo) }
        tvLabelBanVo.setOnClickListener { this.expand(tvLabelBanVo, linearBanVo) }
        tvLabelMuaVo.setOnClickListener { this.expand(tvLabelMuaVo, linearMuaVo) }
        tvLabelBanKhi.setOnClickListener { this.expand(tvLabelBanKhi, linearBanKhi) }
        tvLabelCongNoKH.setOnClickListener { this.expand(tvLabelCongNoKH, linearCongNoKH) }

        handleBanKhiChange(
            productBanKhi12,
            productVoThuHoi12,
            productVoBan12,
            productVoMua12,
            btnCongNo12,
            tvTienKhi12
        )
        handleBanKhiChange(
            productBanKhi45,
            productVoThuHoi45,
            productVoBan45,
            productVoMua45,
            btnCongNo45,
            tvTienKhi45
        )
        edtCongNoTien.addTextChangedListener(afterTextChanged = {
            tienNo = getRealNumberV2(it)
            tvTienNo.text = "${CommonUtils.priceWithoutDecimal(tienNo.toDouble())} đ"
            realPay()
        })
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
            }
        }
    }

    private fun expand(titleGroup: TextView, container: View) {
        if (container.isVisible) {
//            container.visibility = View.GONE
            titleGroup.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_right_circle,
                0
            )
            CommonUtils.collapse(container)
        } else {
//            container.visibility = View.VISIBLE
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
            realPay()
        })

        bankhi.getEditTextGia().addTextChangedListener(afterTextChanged = {
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
            realPay()
        })

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
            realPay()
        })

        voBan.getEditTextGia().addTextChangedListener(afterTextChanged = {
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
            realPay()
        })

        voMua.getEditTextSL().addTextChangedListener(afterTextChanged = {
            val slVoMua = getRealNumberV2(it)
            val giaVoMua = getRealNumber(voMua.getEditTextGia())
            val tienMuaVoComponent = slVoMua * giaVoMua

            if (voMua == productVoMua12) {
                tienVoMua12 = tienMuaVoComponent
                tvTienMuaVo.text =
                    "-${CommonUtils.priceWithoutDecimal((tienVoMua45 + tienMuaVoComponent).toDouble())} đ"
            } else {
                tienVoMua45 = tienMuaVoComponent
                tvTienMuaVo.text =
                    "-${CommonUtils.priceWithoutDecimal((tienVoMua12 + tienMuaVoComponent).toDouble())} đ"
            }

            totalMustPay()
            realPay()
        })

        voMua.getEditTextGia().addTextChangedListener(afterTextChanged = {
            val slVoMua = getRealNumber(voMua.getEditTextSL())
            val giaVoMua = getRealNumberV2(it)
            val tienMuaVoComponent = slVoMua * giaVoMua

            if (voMua == productVoMua12) {
                tienVoMua12 = tienMuaVoComponent
                tvTienMuaVo.text =
                    "-${CommonUtils.priceWithoutDecimal((tienVoMua45 + tienMuaVoComponent).toDouble())} đ"
            } else {
                tienVoMua45 = tienMuaVoComponent
                tvTienMuaVo.text =
                    "-${CommonUtils.priceWithoutDecimal((tienVoMua12 + tienMuaVoComponent).toDouble())} đ"
            }

            totalMustPay()
            realPay()
        })
    }

    private fun totalMustPay() {
        tongTien =
            tienKhiBan12 + tienKhiBan45 + tienVoBan12 + tienVoBan45 - (tienVoMua12 + tienVoMua45)
        tvTongTienCanTT.text = "${CommonUtils.priceWithoutDecimal(tongTien.toDouble())} đ"
    }

    private fun realPay() {
        tienThucTe = tongTien - tienNo
        btnTienThucTe.text = CommonUtils.priceWithoutDecimal(tienThucTe.toDouble())
    }

    private fun getRealNumber(view: EditText): Int {
        return if (TextUtils.isEmpty(view.text.toString().trim())) 0 else view.text.toString()
            .trim().toInt()
    }

    private fun getRealNumberV2(view: Editable?): Int {
        return if (TextUtils.isEmpty(view.toString().trim())) 0 else view.toString()
            .trim().toInt()
    }
}