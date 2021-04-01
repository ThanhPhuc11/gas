package vn.gas.thq.ui.qlyeucaucanhan

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_qlyc_ca_nhan.*
import kotlinx.android.synthetic.main.fragment_retail.*
import kotlinx.android.synthetic.main.layout_dialog_item_ycxk.view.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.customview.ItemProductType1
import vn.gas.thq.customview.ItemProductType2
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.model.ProductRetailModel
import vn.gas.thq.model.StatusValueModel
import vn.gas.thq.model.TransferRetailModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.pheduyetgia.HistoryAcceptAdapter
import vn.gas.thq.ui.pheduyetgia.HistoryModel
import vn.gas.thq.ui.retail.ApproveRequestModel
import vn.gas.thq.ui.retail.Customer
import vn.gas.thq.ui.retail.RetailContainerFragment
import vn.gas.thq.ui.thukho.RequestDetailModel
import vn.gas.thq.ui.thukho.ThuKhoXuatKhoViewModel
import vn.gas.thq.util.*
import vn.gas.thq.util.AppDateUtils.*
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.gas.thq.util.dialog.GetListDataDemo
import vn.hongha.ga.R
import java.util.*

class QLYCCaNhanFragment : BaseFragment(), RequestItemAdapter.ItemClickListener {
    private lateinit var viewModel: QLYCCaNhanViewModel
    private lateinit var viewModelThuKho: ThuKhoXuatKhoViewModel
    private lateinit var adapter: RequestItemAdapter
    private lateinit var adapterDetailYCXK: DetailItemProduct4Adapter
    private lateinit var adapterHistory: HistoryAcceptAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var alertDialog: AlertDialog? = null
    private var alertDialog2: AlertDialog? = null
    private var orderId = ""
    private var mDetailYCXKData: RequestDetailModel? = null
    private var mDetailRetailData: ApproveRequestModel? = null
    var mList = mutableListOf<BussinesRequestModel>()
    private var listStatusOrderSale = mutableListOf<StatusValueModel>()
    private var listHistory = mutableListOf<HistoryModel>()
    private var loaiYC: String? = "Xuất kho"
    private var status: String? = null
    private var type: String? = null
    private var isRetail: Boolean = false
    private var isReload: Boolean = false
    private var fromDate: String = ""
    private var endDate: String = ""

    private var tienKhiBan12 = 0
    private var tienKhiBan45 = 0
    private var tienVoBan12 = 0
    private var tienVoBan45 = 0
    private var tienVoMua12 = 0
    private var tienVoMua45 = 0
    private var tongTien = 0
    private var tienNo = 0
    private var tienThucTe = 0

    private var obj: TransferRetailModel? = null

    companion object {
        @JvmStatic
        fun newInstance(mFromScreen: String): QLYCCaNhanFragment {
            val args = Bundle()
            args.putString("SCREEN", mFromScreen)
            val fragment = QLYCCaNhanFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_qlyc_ca_nhan
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
                .get(QLYCCaNhanViewModel::class.java)

        viewModelThuKho =
            ViewModelProviders.of(this,
                context?.let {
                    RetrofitBuilder.getInstance(it)?.create(ApiService::class.java)
                        ?.let { apiService ->
                            ViewModelFactory(apiService, context)
                        }
                })
                .get(ThuKhoXuatKhoViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Quản lý đơn hàng"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun initData() {
//        if (ScreenId.HOME_SCREEN != arguments?.getString("SCREEN", "")) {
        isRetail = true
        loaiYC = "Bán hàng"
        type = "2"
        edtRequestType.setText("Bán lẻ")
        viewModel.onGetSaleOrderStatus()
//        } else {
//            isRetail = false
//            loaiYC = "Xuất kho"
//            type = "1"
//            edtRequestType.setText("Xuất kho")
//        }
        getInfo()
        initRecyclerView()
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
        edtRequestType.setOnClickListener(this::onChooseType)
        edtStatus.setOnClickListener(this::onChooseStatus)
        btnSearch.setOnClickListener(this::onSearchData)
    }

    override fun initObserver() {
        viewModel.mLiveData.observe(viewLifecycleOwner, {
            if (isReload) {
                mList.clear()
            }
            mList.addAll(it)
            adapter.notifyDataSetChanged()
            isReload = false
        })

        viewModel.mCancelData.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(activity, "Thông báo", "Hoàn thành") {
                alertDialog?.dismiss()
                view?.let { it1 -> onSearchData(it1) }
            }
        })

        //TODO: Retail
        viewModel.listStatus.observe(viewLifecycleOwner, {
            listStatusOrderSale.clear()
            listStatusOrderSale.addAll(it)
            initRecyclerView()
        })

        viewModel.detailApproveCallback.observe(viewLifecycleOwner, {
            mDetailRetailData = it
            mapListToRetailProduct()
            if (mDetailRetailData?.status == 8) {
                viewController?.pushFragment(
                    ScreenId.SCREEN_RETAIL_STEP_2, RetailContainerFragment.newInstance(
                        "STEP_2", obj
                    )
                )
            } else {
                showDiglogDetailRetail()
            }
        })

        viewModel.callbackHistory.observe(viewLifecycleOwner, {
            listHistory.clear()
            listHistory.addAll(it)
//            adapterHistory.notifyDataSetChanged()
            showDiglogHistory()
        })

        //TODO: chung
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

        //TODO: Thu Kho
        viewModelThuKho.mDetailData.observe(viewLifecycleOwner, {
            mDetailYCXKData = it
            showDiglogDetailYCXK()
        })

        viewModelThuKho.callbackStart.observe(viewLifecycleOwner, {
            showLoading()
        })

        viewModelThuKho.callbackSuccess.observe(viewLifecycleOwner, {
            hideLoading()
        })

        viewModelThuKho.callbackFail.observe(viewLifecycleOwner, {
            hideLoading()
        })

        viewModelThuKho.showMessCallback.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun mapListToRetailProduct() {
//        var orderId: String? = null

        var khiBan12: Int? = 0
        var khiBanPrice12: Int? = 0
        var khiBan45: Int? = 0
        var khiBanPrice45: Int? = 0

        var voThu12: Int? = 0
        var voThu45: Int? = 0

        var voBan12: Int? = 0
        var voBanPrice12: Int? = 0
        var voBan45: Int? = 0
        var voBanPrice45: Int? = 0

        var voMua12: Int? = 0
        var voMuaPrice12: Int? = 0
        var voMua45: Int? = 0
        var voMuaPrice45: Int? = 0

        var tienThucTe: Int?
        for (it: ProductRetailModel in mDetailRetailData?.item!!) {
            if (it.saleType == "1") {
                when (it.code) {
                    "GAS12" -> {
                        khiBan12 = it.quantity
                        khiBanPrice12 = it.price
                    }
                    "GAS45" -> {
                        khiBan45 = it.quantity
                        khiBanPrice45 = it.price
                    }
                    "TANK12" -> {
                        voBan12 = it.quantity
                        voBanPrice12 = it.price
                    }
                    "TANK45" -> {
                        voBan45 = it.quantity
                        voBanPrice45 = it.price
                    }
                }
            } else if (it.saleType == "2") {
                if (it.code == "TANK12") {
                    voThu12 = it.quantity
                } else {
                    voThu45 = it.quantity
                }
            } else if (it.saleType == "3") {
                if (it.code == "TANK12") {
                    voMua12 = it.quantity
                    voMuaPrice12 = it.price
                } else {
                    voMua45 = it.quantity
                    voMuaPrice45 = it.price
                }
            }
        }

        tienKhiBan12 = khiBan12!! * khiBanPrice12!!
        tienKhiBan45 = khiBan45!! * khiBanPrice45!!

        tienVoMua12 = voMua12!! * voMuaPrice12!!
        tienVoMua45 = voMua45!! * voMuaPrice45!!

        tienVoBan12 = voBan12!! * voBanPrice12!!
        tienVoBan45 = voBan45!! * voBanPrice45!!

        totalMustPay()
        val customer = Customer().apply {
            customerId = mDetailRetailData?.customerId.toString()
            name = mDetailRetailData?.customerName
            telContact = mDetailRetailData?.customerTelContact
            address = mDetailRetailData?.customerAddress
        }
//        totalDebit()
        obj = TransferRetailModel(
            this.orderId,
            customer,
            khiBan12,
            khiBanPrice12,
            khiBan45,
            khiBanPrice45,
            voThu12,
            voThu45,
            voBan12,
            voBanPrice12,
            voBan45,
            voBanPrice45,
            voMua12,
            voMuaPrice12,
            voMua45,
            voMuaPrice45,
            tongTien - mDetailRetailData?.debtAmount!!
        )
    }

    private fun getInfo() {
        val userModel = AppPreferencesHelper(context).userModel
        tvName.text = userModel?.name
        tvTuyen.text = userModel?.saleLineName
    }

    private fun initRecyclerView() {
        adapter = RequestItemAdapter(mList, loaiYC, listStatusOrderSale)
        adapter.setClickListener(this)

        linearLayoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
        rvRequestItem.layoutManager = linearLayoutManager
        rvRequestItem.adapter = adapter

    }

    private fun setEndLessScrollListener() {
        rvRequestItem.clearOnScrollListeners()
        rvRequestItem.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMoreV2(totalItemsCount: Int) {
                Log.e("PHUCDZ", "$totalItemsCount")
                if (isRetail) {
                    viewModel.onSearchRetail(status, fromDate, endDate, totalItemsCount)
                } else {
                    viewModel.onSubmitData(status, fromDate, endDate, totalItemsCount)
                }
            }
        })
    }

    private fun onChooseType(view: View) {
        var doc = DialogList()
        var mArrayList = GetListDataDemo.getListRequestType(Objects.requireNonNull(context))
        doc.show(
            activity, mArrayList,
            getString(R.string.status),
            getString(R.string.enter_text_search)
        ) { item ->
//            if (AppConstants.NOT_SELECT == item.id) {
//                return@show
//            }
            type = item.id
            edtRequestType.setText(item.name)
            handleStatus(type)
        }
    }

    private fun handleStatus(type: String?) {
        status = null
        edtStatus.setText("Tất cả")
        if (type == "2") {
            isRetail = true
            viewModel.onGetSaleOrderStatus()
            loaiYC = "Bán hàng"
        } else if (type == "1") {
            isRetail = false
            loaiYC = "Xuất kho"
            initRecyclerView()
        }
    }

    private fun onChooseStatus(view: View) {
        var doc = DialogList()
        var mArrayList = ArrayList<DialogListModel>()
        if (!isRetail) {
            mArrayList = GetListDataDemo.getListStatus(Objects.requireNonNull(context))
        } else {
            mArrayList.add(0, DialogListModel("-2", "Tất cả"))
            listStatusOrderSale.forEach {
                mArrayList.add(DialogListModel(it.value, it.name))
            }
        }

        doc.show(
            activity, mArrayList,
            getString(R.string.status),
            getString(R.string.enter_text_search)
        ) { item ->
//            if (AppConstants.NOT_SELECT == item.id) {
//                return@show
//            }
            status = item.id
            edtStatus.setText(item.name)
            if (AppConstants.SELECT_ALL == item.id) {
                status = null
            }
        }
    }

    private fun onSearchData(view: View) {
        setEndLessScrollListener()
        isReload = true
        fromDate =
            AppDateUtils.changeDateFormat(FORMAT_2, FORMAT_5, edtStartDate.text.toString())
        endDate =
            AppDateUtils.changeDateFormat(FORMAT_2, FORMAT_5, edtEndDate.text.toString())
        if (isRetail) {
            viewModel.onSearchRetail(status, fromDate, endDate, 0)
            return
        }
        viewModel.onSubmitData(status, fromDate, endDate, 0)
    }

    private fun showDiglogDetailYCXK(
//        title: String,
//        message: String,
//        callback: ConfirmDialogCallback?
    ) {
        val builder = context?.let { AlertDialog.Builder(it, R.style.AlertDialogNoBG) }
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.layout_dialog_item_ycxk, null)
        builder?.setView(dialogView)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapterDetailYCXK = DetailItemProduct4Adapter(mDetailYCXKData!!.item)

        dialogView.apply {
            imgClose.setOnClickListener {
                alertDialog?.dismiss()
            }
            when (mDetailYCXKData?.status) {
                0 -> {
                    tvStatus.text = resources.getString(R.string.cancel_status)
                    tvStatus.setTextColor(resources.getColor(R.color.red_EA7035))
                    linearAccept.visibility = View.GONE
                    adapterDetailYCXK.isReadOnly()
                }
                1 -> {
                    tvStatus.text = resources.getString(R.string.new_status)
                    tvStatus.setTextColor(resources.getColor(R.color.blue_14AFB4))
                    linearAccept.visibility = View.VISIBLE
                }
                3 -> {
                    tvStatus.text = resources.getString(R.string.approved_status)
                    tvStatus.setTextColor(resources.getColor(R.color.blue_14AFB4))
                    linearAccept.visibility = View.GONE
                    adapterDetailYCXK.isReadOnly()
                }
                2 -> {
                    tvStatus.text = resources.getString(R.string.reject_status)
                    tvStatus.setTextColor(resources.getColor(R.color.red_EA7035))
                    linearAccept.visibility = View.GONE
                    adapterDetailYCXK.isReadOnly()
                }
            }
            tvName.text = mDetailYCXKData?.staffName
            tvDate.text = AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_6,
                AppDateUtils.FORMAT_1,
                mDetailYCXKData?.createdDate
            )
//            tvDate.text = mDetailYCXKData?.createdDate
            tvOrderId.text = "Mã yêu cầu $orderId"

            rvProductDialog.layoutManager = linearLayoutManager
            rvProductDialog.adapter = adapterDetailYCXK


            btnHuyYC.setOnClickListener {
                CommonUtils.showConfirmDiglog2Button(
                    activity, "Xác nhận", "Bạn có chắc chắn muốn huỷ yêu cầu?", getString(
                        R.string.biometric_negative_button_text
                    ), getString(R.string.text_ok)
                ) {
                    if (it == AppConstants.YES) {
                        viewModel.onCancelRequest(orderId)
                    }
                }
            }
            btnCapNhat.setOnClickListener {
//                viewModelThuKho.acceptOrNotRequest(orderId, true)
            }
        }
        alertDialog = builder?.create()
        alertDialog?.window?.setLayout(500, 200)
        alertDialog?.show()
    }

    private fun showDiglogDetailRetail() {
        val builder = context?.let { AlertDialog.Builder(it, R.style.AlertDialogNoBG) }
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.layout_dialog_item_retail, null)
        builder?.setView(dialogView)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        adapterDetailYCXK = DetailItemProduct4Adapter(mDetailYCXKData!!.item)

        val imgClose = dialogView.findViewById<ImageView>(R.id.imgClose)
        val btnHuy = dialogView.findViewById<Button>(R.id.btnHuy)
        val tvOrderId = dialogView.findViewById<TextView>(R.id.tvOrderId)

        val tvName: TextView = dialogView.findViewById(R.id.tvName)
        val tvAddress: TextView = dialogView.findViewById(R.id.tvAddress)
        val tvPhone: TextView = dialogView.findViewById(R.id.tvPhone)

        val productBanKhi12: ItemProductType1 = dialogView.findViewById(R.id.productBanKhi12)
        val productBanKhi45: ItemProductType1 = dialogView.findViewById(R.id.productBanKhi45)
        val productVoThuHoi12: ItemProductType2 = dialogView.findViewById(R.id.productVoThuHoi12)
        val productVoThuHoi45: ItemProductType2 = dialogView.findViewById(R.id.productVoThuHoi45)
        val productVoBan12: ItemProductType1 = dialogView.findViewById(R.id.productVoBan12)
        val productVoBan45: ItemProductType1 = dialogView.findViewById(R.id.productVoBan45)
        val productVoMua12: ItemProductType1 = dialogView.findViewById(R.id.productVoMua12)
        val productVoMua45: ItemProductType1 = dialogView.findViewById(R.id.productVoMua45)

        val btnCongNo12: Button = dialogView.findViewById(R.id.btnCongNo12)
        val btnCongNo45: Button = dialogView.findViewById(R.id.btnCongNo45)
        val btnCongNoTien: Button = dialogView.findViewById(R.id.btnCongNoTien)

        val tvTienKhi12: TextView = dialogView.findViewById(R.id.tvTienKhi12)
        val tvTienKhi45: TextView = dialogView.findViewById(R.id.tvTienKhi45)
        val tvTienBanVo: TextView = dialogView.findViewById(R.id.tvTienBanVo)
        val tvTienMuaVo: TextView = dialogView.findViewById(R.id.tvTienMuaVo)
        val tvTienNo: TextView = dialogView.findViewById(R.id.tvTienNo)
        val tvTongTienCanTT: TextView = dialogView.findViewById(R.id.tvTongTienCanTT)

        val tvTienThucTe: TextView = dialogView.findViewById(R.id.tvTienThucTe)

        val tvHistory: TextView = dialogView.findViewById(R.id.tvHistory)

        dialogView.apply {
            imgClose.setOnClickListener {
                alertDialog?.dismiss()
            }
            tvHistory.setOnClickListener {
                viewModel.getHistoryAcceptRetail(orderId.toInt())
            }
            tvOrderId.text = "Mã yêu cầu $orderId"
            tvName.text = mDetailRetailData?.customerName ?: "- -"
            tvAddress.text = mDetailRetailData?.customerAddress ?: "- -"
            tvPhone.text = mDetailRetailData?.customerTelContact ?: "- -"

            productBanKhi12.setSoLuong(obj?.khiBan12?.toString())
            productBanKhi12.setGia(CommonUtils.priceWithoutDecimal(obj?.khiBanPrice12?.toDouble()))
            productBanKhi45.setSoLuong(obj?.khiBan45?.toString())
            productBanKhi45.setGia(CommonUtils.priceWithoutDecimal(obj?.khiBanPrice45?.toDouble()))

            productVoThuHoi12.setSoLuong(obj?.voThu12?.toString())
            productVoThuHoi45.setSoLuong(obj?.voThu45?.toString())

            productVoBan12.setSoLuong(obj?.voBan12?.toString())
            productVoBan12.setGia(CommonUtils.priceWithoutDecimal(obj?.voBanPrice12?.toDouble()))
            productVoBan45.setSoLuong(obj?.voBan45?.toString())
            productVoBan45.setGia(CommonUtils.priceWithoutDecimal(obj?.voBanPrice45?.toDouble()))

            productVoMua12.setSoLuong(obj?.voMua12?.toString())
            productVoMua12.setGia(CommonUtils.priceWithoutDecimal(obj?.voMuaPrice12?.toDouble()))
            productVoMua45.setSoLuong(obj?.voMua45?.toString())
            productVoMua45.setGia(CommonUtils.priceWithoutDecimal(obj?.voMuaPrice45?.toDouble()))

            tvTienThucTe.text = "${CommonUtils.priceWithoutDecimal(obj?.tienThucTe?.toDouble())} đ"

            btnCongNo12.text = mDetailRetailData?.debtAmountTank12?.toString() ?: "0"
            btnCongNo45.text = mDetailRetailData?.debtAmountTank45?.toString() ?: "0"
            btnCongNoTien.text =
                "${CommonUtils.priceWithoutDecimal(mDetailRetailData?.debtAmount?.toDouble())}"
//            rvProductDialog.layoutManager = linearLayoutManager
//            rvProductDialog.adapter = adapterDetailYCXK

            tvTienKhi12.text = "$tienKhiBan12 đ"
            tvTienKhi45.text = "$tienKhiBan45 đ"
            tvTienBanVo.text = "${tienVoBan12 + tienVoBan45} đ"
            tvTienMuaVo.text = "${tienVoMua12 + tienVoMua45} đ"
            tvTienNo.text = "${mDetailRetailData?.debtAmount} đ"
            tvTongTienCanTT.text = "$tongTien đ"


            btnHuy.setOnClickListener {
                CommonUtils.showConfirmDiglog2Button(
                    activity, "Xác nhận", "Bạn có chắc chắn muốn huỷ yêu cầu?", getString(
                        R.string.biometric_negative_button_text
                    ), getString(R.string.text_ok)
                ) {
                    if (it == AppConstants.YES) {
//                        viewModel.onCancelRequest(orderId)
                    }
                }
            }
        }
        alertDialog = builder?.create()
        alertDialog?.window?.setLayout(500, 200)
        alertDialog?.show()
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

        adapterHistory = HistoryAcceptAdapter(listHistory)

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

    private fun totalMustPay() {
        tongTien =
            tienKhiBan12 + tienKhiBan45 + tienVoBan12 + tienVoBan45 - (tienVoMua12 + tienVoMua45)
//        tvTongTienCanTT.text = "${CommonUtils.priceWithoutDecimal(tongTien.toDouble())} đ"
    }

    private fun totalDebit() {
        tienNo = tongTien - tienThucTe
        btnCongNoTien.text = CommonUtils.priceWithoutDecimal(tienNo.toDouble())
        tvTienNo.text = "${CommonUtils.priceWithoutDecimal(tienNo.toDouble())} đ"
    }

    override fun onItemClick(view: View?, position: Int) {
        if (type == "1") {
            orderId = mList[position].stock_trans_id.toString()
            viewModelThuKho.onDetailRequest(orderId)
        } else if (type == "2") {
            orderId = mList[position].order_id.toString()
            viewModel.detailApproveLXBH(orderId)
        }
    }
}