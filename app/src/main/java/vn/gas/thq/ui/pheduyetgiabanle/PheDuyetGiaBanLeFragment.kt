package vn.gas.thq.ui.pheduyetgiabanle

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_phe_duyet_gia_ban_le.*
import kotlinx.android.synthetic.main.fragment_phe_duyet_gia_ban_le.edtCustomer
import kotlinx.android.synthetic.main.fragment_retail.*
import kotlinx.android.synthetic.main.layout_dialog_item_history.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.layout_toolbar.tvTitle
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.customview.ItemProductType1
import vn.gas.thq.customview.ItemProductType2
import vn.gas.thq.model.*
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.retail.ApproveRequestModel
import vn.gas.thq.ui.retail.Customer
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.EndlessPageRecyclerViewScrollListener
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.hongha.ga.R
import java.util.*


class PheDuyetGiaBanLeFragment : BaseFragment(), ListYCBanLeAdapter.ItemClickListener {
    private lateinit var banLeViewModel: PheDuyetGiaBanLeViewModel
    private lateinit var adapter: ListYCBanLeAdapter
    private lateinit var adapterHistory: HistoryAcceptBanLeAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var mListStaff = mutableListOf<UserModel>()
    private var listStatusOrderSale = mutableListOf<StatusValueModel>()
    private var mList = mutableListOf<BussinesRequestModel>()
    private var listHistory = mutableListOf<HistoryModel>()
    private var mListCustomer = mutableListOf<Customer>()
    private var mDetailRetailData: ApproveRequestModel? = null
    private var type: String = "1"
    private var status: String? = null
    private var staffCode: String? = null
    private var alertDialog: AlertDialog? = null
    private var alertDialog2: AlertDialog? = null
    private var statusShowDialog: Int? = null
    private var orderId: Int? = null
    private var staffName: String? = null
    private var custId: Int? = null
    private var createDate: String? = null
    private var canApproveStatus: String? = null
    private var canCommentStatus: String? = null
    private var obj: TransferRetailModel? = null

    private var fromDate: String = ""
    private var endDate: String = ""
    private var isReload: Boolean = false

    private var soLuong12: Int? = 0
    private var priceKHBH12: Int? = 0
    private var priceNiemYet12: Int? = 0
    private var priceDeXuat12: Int? = 0

    private var soLuong45: Int? = 0
    private var priceKHBH45: Int? = 0
    private var priceNiemYet45: Int? = 0
    private var priceDeXuat45: Int? = 0

    private var congNoLuyKe12: Int? = 0
    private var congNoGiaTang12: Int? = 0
    private var congNoLuyKe45: Int? = 0
    private var congNoGiaTang45: Int? = 0
    private var congNoLuyKe: Int? = 0
    private var congNoGiaTang: Int? = 0

    private var tienKhiBan12 = 0
    private var tienKhiBan45 = 0
    private var tienVoBan12 = 0
    private var tienVoBan45 = 0
    private var tienVoMua12 = 0
    private var tienVoMua45 = 0
    private var tongTien = 0
    private var tienNo = 0
    private var tienThucTe = 0

    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    private var PERMISSION_ALL = 1
    private var PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    companion object {
        @JvmStatic
        fun newInstance(): PheDuyetGiaBanLeFragment {
            val args = Bundle()

            val fragment = PheDuyetGiaBanLeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setViewController() {
        viewController = (activity as MainActivity).viewController
    }

    override fun setupViewModel() {
        banLeViewModel =
            ViewModelProviders.of(this,
                context?.let {
                    RetrofitBuilder.getInstance(it)?.create(ApiService::class.java)
                        ?.let { apiService ->
                            ViewModelFactory(apiService, context)
                        }
                })
                .get(PheDuyetGiaBanLeViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Phê duyệt yêu cầu bán hàng"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_phe_duyet_gia_ban_le
    }

    override fun initObserver() {
        banLeViewModel.mListStaffData.observe(viewLifecycleOwner, {
            mListStaff.clear()
            mListStaff.addAll(it)
        })

        banLeViewModel.listStatus.observe(viewLifecycleOwner, {
            listStatusOrderSale.clear()
            listStatusOrderSale.addAll(it)
        })

        banLeViewModel.callbackListCustomer.observe(viewLifecycleOwner, {
            mListCustomer.addAll(it)
        })

        banLeViewModel.mListDataSearch.observe(viewLifecycleOwner, {
            if (isReload) {
                mList.clear()
            }
            mList.addAll(it)
            adapter.notifyDataSetChanged()
            isReload = false
        })

        banLeViewModel.detailApproveCallback.observe(viewLifecycleOwner, {
            mDetailRetailData = it
            banLeViewModel.getHistoryAcceptRetail(orderId!!)
//            mapListToRetailProduct()
//            autoSelectDialog(it)
        })

        banLeViewModel.callbackComment.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(activity, "Thông báo", "Cho ý kiến thành công") {
                alertDialog?.dismiss()
                view?.let { it1 -> onSearchData(it1) }
            }
        })

        banLeViewModel.callbackAccept.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(activity, "Thông báo", "Phê duyệt yêu cầu thành công") {
                alertDialog?.dismiss()
                view?.let { it1 -> onSearchData(it1) }
            }
        })

        banLeViewModel.callbackReject.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(activity, "Thông báo", "Từ chối yêu cầu thành công") {
                alertDialog?.dismiss()
                view?.let { it1 -> onSearchData(it1) }
            }
        })

        banLeViewModel.callbackHistory.observe(viewLifecycleOwner, {
            listHistory.clear()
            listHistory.addAll(it)
//            adapterHistory.notifyDataSetChanged()
//            showDiglogHistory()
            mapListToRetailProduct()
            autoSelectDialog(mDetailRetailData!!)
        })

        banLeViewModel.callbackHistoryFail.observe(viewLifecycleOwner, {
            mapListToRetailProduct()
            autoSelectDialog(mDetailRetailData!!)
        })

        //TODO: chung
        banLeViewModel.callbackStart.observe(viewLifecycleOwner, {
            showLoading()
        })

        banLeViewModel.callbackSuccess.observe(viewLifecycleOwner, {
            hideLoading()
        })

        banLeViewModel.callbackFail.observe(viewLifecycleOwner, {
            hideLoading()
        })

        banLeViewModel.showMessCallback.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun initData() {
        banLeViewModel.getListStaff()
        banLeViewModel.onGetSaleOrderStatus()
        initRecyclerView()
//        initHistoryRecyclerView()

        edtType.setOnClickListener(this::onChooseType)
        edtLXBH.setOnClickListener(this::onChooseLXBH)
        edtCustomer.setOnClickListener(this::chooseCustomer)
        edtStatus.setOnClickListener(this::onChooseStatus)
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

        btnSearch.setOnClickListener(this::onSearchData)
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
    }

    private fun initRecyclerView() {
        adapter = context?.let { ListYCBanLeAdapter(mList, listStatusOrderSale, it) }!!
        adapter.setClickListener(this)

        linearLayoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
        rvRequestApprove.layoutManager = linearLayoutManager
        rvRequestApprove.adapter = adapter
    }

    private fun initHistoryRecyclerView() {
        adapterHistory = HistoryAcceptBanLeAdapter(listHistory)

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvHistory.layoutManager = linearLayoutManager
        rvHistory.adapter = adapter
    }

    private fun onChooseType(view: View) {
        val doc = DialogList()
        var mArrayList = ArrayList<DialogListModel>()
        mArrayList.add(DialogListModel("1", "Bán lẻ"))
        mArrayList.add(DialogListModel("2", "Bán Tổng đại lý"))
        doc.show(
            activity, mArrayList,
            "Loại",
            getString(R.string.enter_text_search)
        ) { item ->
            type = item.id
            edtType.setText(item.name)
        }
    }

    private fun onChooseLXBH(view: View) {
        val doc = DialogList()
        var mArrayList = ArrayList<DialogListModel>()
        mArrayList.add(0, DialogListModel(AppConstants.SELECT_ALL, getString(R.string.all)))
        mListStaff.forEach {
            mArrayList.add(DialogListModel(it.staffCode, "${it.staffCode} - ${it.name}"))
        }
        doc.show(
            activity, mArrayList,
            getString(R.string.lxbh),
            getString(R.string.enter_text_search)
        ) { item ->
            staffCode = item.id
            edtLXBH.setText(item.name)

            if (AppConstants.SELECT_ALL == item.id) {
                staffCode = null
            }
        }
    }

    private fun onChooseStatus(view: View) {
        var doc = DialogList()
        var mArrayList = ArrayList<DialogListModel>()
        mArrayList.add(0, DialogListModel("-2", "Tất cả"))
        listStatusOrderSale.forEach {
            mArrayList.add(DialogListModel(it.value, it.name))
        }

        doc.show(
            activity, mArrayList,
            getString(R.string.status),
            getString(R.string.enter_text_search)
        ) { item ->
            status = item.id
            edtStatus.setText(item.name)
            if (AppConstants.SELECT_ALL == item.id) {
                status = null
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
            custId = item.id.toInt()
            edtCustomer.setText(item.name)
        }
    }

    private fun onSearchData(view: View) {
        setEndLessScrollListener()
        isReload = true
        fromDate =
            AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_2,
                AppDateUtils.FORMAT_5,
                edtStartDate.text.toString()
            )
        endDate =
            AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_2,
                AppDateUtils.FORMAT_5,
                edtEndDate.text.toString()
            )
        banLeViewModel.onHandleSearch(
            type,
            status,
            staffCode,
            custId,
            cbTick.isChecked,
            fromDate,
            endDate,
            0
        )
    }

    private fun setEndLessScrollListener() {
        rvRequestApprove.clearOnScrollListeners()
        rvRequestApprove.addOnScrollListener(object :
            EndlessPageRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                Log.e("PHUCDZ", "$totalItemsCount")
                banLeViewModel.onHandleSearch(
                    type,
                    status,
                    staffCode,
                    custId,
                    cbTick.isChecked,
                    fromDate,
                    endDate,
                    page
                )
            }
        })
    }

    private fun autoSelectDialog(it: ApproveRequestModel) {
        showLoading()
        //xem xet can_comment_status
        if (it.canCommentStatus?.get(0).toString() == "1") {
            // mo comment khi
            statusShowDialog = 1
            setValueApproveDetail(1)
            showDiglogDetailRetail(true)
            return
        } else if (it.canCommentStatus?.get(1).toString() == "1") {
            // mo comment vo
            statusShowDialog = 2
            setValueApproveDetail(2)
            showDiglogDetailRetail(true)
            return
        } else if (it.canCommentStatus?.get(2).toString() == "1") {
            // mo comment cong no
            statusShowDialog = 3
            congNoGiaTang12 = mDetailRetailData?.debtAmountTank12 ?: 0
            congNoGiaTang45 = mDetailRetailData?.debtAmountTank45 ?: 0
            congNoGiaTang = mDetailRetailData?.debtAmount ?: 0
            showDiglogDetailRetail(true)
            return
        }

        val array = this.canApproveStatus?.toCharArray()
        val a = it.canApproveStatus?.get(0).toString()
        val b = it.canApproveStatus?.get(1).toString()
        val c = it.canApproveStatus?.get(2).toString()
        if (a == "1" || a == "2" || a == "3") {
            // mo khi
            statusShowDialog = 1
            setValueApproveDetail(1)
            showDiglogDetailRetail()
            return
        }

        if (b == "1" || b == "2" || b == "3") {
            // mo vo
            statusShowDialog = 2
            setValueApproveDetail(2)
            showDiglogDetailRetail()
            return
        }

        if (c == "1" || c == "2" || c == "3") {
            // mo cong no
            statusShowDialog = 3
            congNoGiaTang12 = mDetailRetailData?.debtAmountTank12 ?: 0
            congNoGiaTang45 = mDetailRetailData?.debtAmountTank45 ?: 0
            congNoGiaTang = mDetailRetailData?.debtAmount ?: 0
            showDiglogDetailRetail()
            return
        }
        showDiglogDetailRetailV2()
//        showMess("Bạn không có quyền thực hiện với yêu cầu này")
    }

    private fun setValueApproveDetail(type: Int) {
        soLuong12 = 0
        priceKHBH12 = 0
        priceNiemYet12 = 0
        priceDeXuat12 = 0

        soLuong45 = 0
        priceKHBH45 = 0
        priceNiemYet45 = 0
        priceDeXuat45 = 0

        congNoGiaTang12 = 0
        congNoGiaTang45 = 0
        congNoGiaTang = 0


        mDetailRetailData?.item?.forEach {
            when (type) {
                1 -> {
                    if (it.code == "GAS12") {
                        soLuong12 = it.quantity
                        priceKHBH12 = it.pricePlan
                        priceNiemYet12 = it.priceStandard
                        priceDeXuat12 = it.price
                    } else if (it.code == "GAS45") {
                        soLuong45 = it.quantity
                        priceKHBH45 = it.pricePlan
                        priceNiemYet45 = it.priceStandard
                        priceDeXuat45 = it.price
                    }
                }
                2 -> {
                    if (it.code == "TANK12" && it.saleType == "1") {
                        soLuong12 = it.quantity
                        priceKHBH12 = it.pricePlan
                        priceNiemYet12 = it.priceStandard
                        priceDeXuat12 = it.price
                    } else if (it.code == "TANK45" && it.saleType == "1") {
                        soLuong45 = it.quantity
                        priceKHBH45 = it.pricePlan
                        priceNiemYet45 = it.priceStandard
                        priceDeXuat45 = it.price
                    }
                }
            }
        }

    }

    private fun showDiglogDetailRetail(isComment: Boolean = false) {
        val builder = context?.let { AlertDialog.Builder(it, R.style.AlertDialogNoBG) }
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.layout_dialog_item_duyet_gia, null)
        builder?.setView(dialogView)
        val tvTitle: TextView = dialogView.findViewById(R.id.tvTitle)
        val imgClose: ImageView = dialogView.findViewById(R.id.imgClose)

        val tvNameLXBH: TextView = dialogView.findViewById(R.id.tvNameLXBH)
        val tvTuyenXe: TextView = dialogView.findViewById(R.id.tvTuyenXe)
        val tvNameCust: TextView = dialogView.findViewById(R.id.tvNameCust)
        val tvStatus: TextView = dialogView.findViewById(R.id.tvStatus)
        val tvDate: TextView = dialogView.findViewById(R.id.tvDate)

        val product12: TextView = dialogView.findViewById(R.id.product12)
        val product45: TextView = dialogView.findViewById(R.id.product45)

        val tvSL12: TextView = dialogView.findViewById(R.id.tvSL12)
        val tvPricePlan12: TextView = dialogView.findViewById(R.id.tvPricePlan12)
        val tvPriceStandard12: TextView = dialogView.findViewById(R.id.tvPriceStandard12)
        val tvPrice12: TextView = dialogView.findViewById(R.id.tvPrice12)

        val tvSL45: TextView = dialogView.findViewById(R.id.tvSL45)
        val tvPricePlan45: TextView = dialogView.findViewById(R.id.tvPricePlan45)
        val tvPriceStandard45: TextView = dialogView.findViewById(R.id.tvPriceStandard45)
        val tvPrice45: TextView = dialogView.findViewById(R.id.tvPrice45)

        val tvCongNo12: TextView = dialogView.findViewById(R.id.tvCongNo12)
        val tvCongNo45: TextView = dialogView.findViewById(R.id.tvCongNo45)
        val tvCongNo: TextView = dialogView.findViewById(R.id.tvCongNo)
        val llWrapNgayHenTra: LinearLayout = dialogView.findViewById(R.id.llWapNgayHenTra)
        val edtExpireDate: EditText = dialogView.findViewById(R.id.edtExpireDate)

        val edtReason: EditText = dialogView.findViewById(R.id.edtReason)
        val tvHistory: TextView = dialogView.findViewById(R.id.tvHistory)

        val cardViewCongNo: CardView = dialogView.findViewById(R.id.cardViewCongNo)
        val cardViewGia: CardView = dialogView.findViewById(R.id.cardViewGia)
        val btnDongY: Button = dialogView.findViewById(R.id.btnDongY)
        val btnTuChoi: Button = dialogView.findViewById(R.id.btnTuChoi)

        val tvLabelReason: TextView = dialogView.findViewById(R.id.tvLabelReason)
        val rvHistoryDuyetGia: RecyclerView = dialogView.findViewById(R.id.rvHistory)

        tvNameLXBH.text = staffName
        tvTuyenXe.text = mDetailRetailData?.saleLineName
        tvNameCust.text = mDetailRetailData?.customerName
//        tvDate.text = createDate
        tvDate.text = AppDateUtils.changeDateFormat(
            AppDateUtils.FORMAT_6,
            AppDateUtils.FORMAT_1,
            createDate
        )
//        tvStatus.text = "${mDetailRetailData?.status};${mDetailRetailData?.approveStatus}"
//        listStatusOrderSale.forEach {
//            if (it.value == "${mDetailRetailData?.status};${mDetailRetailData?.approveStatus}") {
//                tvStatus.text = it.name
//                return@forEach
//            }
//        }
        val strStatus = "${mDetailRetailData?.status};${mDetailRetailData?.approveStatus}"
        tvStatus.text =
            listStatusOrderSale.find { it.value!!.contains(strStatus) }?.name ?: strStatus

        when (statusShowDialog) {
            1 -> {
                llWrapNgayHenTra.visibility = View.GONE
                tvTitle.text = "Phê duyệt giá khí"
                product12.text = "Khí 12Kg"
                product45.text = "Khí 45Kg"

                tvSL12.text = soLuong12.toString()
                tvPricePlan12.text = "${CommonUtils.priceWithoutDecimal(priceKHBH12?.toDouble())} đ"
                tvPriceStandard12.text =
                    "${CommonUtils.priceWithoutDecimal(priceNiemYet12?.toDouble())} đ"
                tvPrice12.text = "${CommonUtils.priceWithoutDecimal(priceDeXuat12?.toDouble())} đ"

                tvSL45.text = soLuong45.toString()
                tvPricePlan45.text = "${CommonUtils.priceWithoutDecimal(priceKHBH45?.toDouble())} đ"
                tvPriceStandard45.text =
                    "${CommonUtils.priceWithoutDecimal(priceNiemYet45?.toDouble())} đ"
                tvPrice45.text = "${CommonUtils.priceWithoutDecimal(priceDeXuat45?.toDouble())} đ"

                cardViewCongNo.visibility = View.GONE

                if (isComment) {
                    tvTitle.text = "Cho ý kiến"
                    tvLabelReason.text = "Ý kiến"
                    edtReason.hint = "Nhập ý kiến"
                    btnTuChoi.text = "Không đồng ý"
                }
            }
            2 -> {
                llWrapNgayHenTra.visibility = View.GONE
                tvTitle.text = "Phê duyệt giá vỏ"
                product12.text = "Vỏ 12Kg"
                product45.text = "Vỏ 45Kg"

                tvSL12.text = soLuong12.toString()
                tvPricePlan12.text = "${CommonUtils.priceWithoutDecimal(priceKHBH12?.toDouble())} đ"
                tvPriceStandard12.text =
                    "${CommonUtils.priceWithoutDecimal(priceNiemYet12?.toDouble())} đ"
                tvPrice12.text = "${CommonUtils.priceWithoutDecimal(priceDeXuat12?.toDouble())} đ"

                tvSL45.text = soLuong45.toString()
                tvPricePlan45.text = "${CommonUtils.priceWithoutDecimal(priceKHBH45?.toDouble())} đ"
                tvPriceStandard45.text =
                    "${CommonUtils.priceWithoutDecimal(priceNiemYet45?.toDouble())} đ"
                tvPrice45.text = "${CommonUtils.priceWithoutDecimal(priceDeXuat45?.toDouble())} đ"

                cardViewCongNo.visibility = View.GONE

                if (isComment) {
                    tvTitle.text = "Cho ý kiến"
                    tvLabelReason.text = "Ý kiến"
                    edtReason.hint = "Nhập ý kiến"
                    btnTuChoi.text = "Không đồng ý"
                }
            }
            3 -> {
                tvTitle.text = "Phê duyệt công nợ"
                cardViewGia.visibility = View.GONE

                tvCongNo12.text = congNoGiaTang12.toString()
                tvCongNo45.text = congNoGiaTang45.toString()
                tvCongNo.text = "${CommonUtils.priceWithoutDecimal(congNoGiaTang?.toDouble())} đ"
                edtExpireDate.setText(
                    AppDateUtils.changeDateFormat(
                        AppDateUtils.FORMAT_6,
                        AppDateUtils.FORMAT_2,
                        mDetailRetailData?.debtExpireDate
                    )
                )
                if (isComment) {
                    tvTitle.text = "Cho ý kiến"
                    tvLabelReason.text = "Ý kiến"
                    edtReason.hint = "Nhập ý kiến"
                    btnTuChoi.text = "Không đồng ý"
                }
            }
        }

        adapterHistory = HistoryAcceptBanLeAdapter(listHistory)

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvHistoryDuyetGia.layoutManager = linearLayoutManager
        val dividerItemDecoration = DividerItemDecoration(
            rvHistoryDuyetGia.context,
            linearLayoutManager.orientation
        )
        rvHistoryDuyetGia.addItemDecoration(dividerItemDecoration)
        rvHistoryDuyetGia.adapter = adapterHistory

        imgClose.setOnClickListener {
            alertDialog?.dismiss()
        }

        btnDongY.setOnClickListener {
            if (isComment) {
                if (edtReason.text.isEmpty()) {
                    showMess("NVKD bắt buộc phải cho ý kiến trên đơn hàng")
                } else {
                    banLeViewModel.commentBanLe(orderId?.toString(), CommentModel().apply {
                        productType = statusShowDialog.toString()
                        comment = edtReason.text.toString()
                        ok = true
                    })
                }
                return@setOnClickListener
            }
            CommonUtils.showConfirmDiglog2Button(
                activity, "Xác nhận", "Bạn có chắc chắn muốn phê duyệt yêu cầu?", getString(
                    R.string.biometric_negative_button_text
                ), getString(R.string.text_ok)
            ) {
                if (it == AppConstants.YES) {
                    banLeViewModel.onHandleAccept(
                        type,
                        orderId?.toString(),
                        DuyetGiaModel(statusShowDialog)
                    )
                }
            }
        }

        btnTuChoi.setOnClickListener {
            if (isComment) {
                if (edtReason.text.isEmpty()) {
                    showMess("Bạn phải nhập ý kiến nếu không đồng ý với đề xuất của đơn hàng")
                } else {
                    banLeViewModel.commentBanLe(orderId?.toString(), CommentModel().apply {
                        productType = statusShowDialog.toString()
                        comment = edtReason.text.toString()
                        ok = false
                    })
                }
                return@setOnClickListener
            }
            if (edtReason.text.isEmpty()) {
                showMess("Bạn phải nhập ý kiến nếu không đồng ý với đề xuất của đơn hàng")
            } else {
                CommonUtils.showConfirmDiglog2Button(
                    activity, "Xác nhận", "Bạn có chắc chắn muốn từ chối yêu cầu?", getString(
                        R.string.biometric_negative_button_text
                    ), getString(R.string.text_ok)
                ) {
                    if (it == AppConstants.YES) {
                        banLeViewModel.onHandleReject(
                            type,
                            orderId?.toString(),
                            DuyetGiaModel(statusShowDialog, edtReason.text.toString())
                        )
                    }
                }
            }
        }

//        tvHistory.setOnClickListener {
//            banLeViewModel.getHistoryAcceptRetail(orderId!!)
//        }

        alertDialog = builder?.create()
        alertDialog?.window?.setLayout(500, 200)
        alertDialog?.show()
        hideLoading()
    }

    private fun showDiglogDetailRetailV2() {
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
        val edtTienMat: TextView = dialogView.findViewById(R.id.edtTienMat)
        val edtTienChuyenKhoan: TextView = dialogView.findViewById(R.id.edtTienChuyenKhoan)
        val tvNgayHenTra: TextView = dialogView.findViewById(R.id.tvNgayHenTra)
        val tvHistory: TextView = dialogView.findViewById(R.id.tvHistory)

        val tvTienThucTe: TextView = dialogView.findViewById(R.id.tvTienThucTe)

        dialogView.apply {
            imgClose.setOnClickListener {
                alertDialog?.dismiss()
            }
            tvHistory.setOnClickListener {
                banLeViewModel.getHistoryAcceptRetail(orderId!!)
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

            "${CommonUtils.priceWithoutDecimal(tienKhiBan12.toDouble())} đ".also {
                tvTienKhi12.text = it
            }
            "${CommonUtils.priceWithoutDecimal(tienKhiBan45.toDouble())} đ".also {
                tvTienKhi45.text = it
            }
            "${CommonUtils.priceWithoutDecimal((tienVoBan12 + tienVoBan45).toDouble())} đ".also {
                tvTienBanVo.text = it
            }
            "${CommonUtils.priceWithoutDecimal((tienVoMua12 + tienVoMua45).toDouble())} đ".also {
                tvTienMuaVo.text = it
            }
            "${CommonUtils.priceWithoutDecimal(mDetailRetailData?.debtAmount?.toDouble())} đ".also {
                tvTienNo.text = it
            }
            "${CommonUtils.priceWithoutDecimal(tongTien.toDouble())} đ".also {
                tvTongTienCanTT.text = it
            }
            edtTienMat.text =
                CommonUtils.priceWithoutDecimal(mDetailRetailData?.paymentAmountMoney?.toDouble())
            edtTienChuyenKhoan.text =
                CommonUtils.priceWithoutDecimal(mDetailRetailData?.paymentAmountTransfer?.toDouble())
            tvNgayHenTra.text = AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_6,
                AppDateUtils.FORMAT_2,
                mDetailRetailData?.debtExpireDate
            )


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
        hideLoading()
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
//        totalDebit()
        obj = TransferRetailModel(
            this.orderId.toString(),
            null,
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
            mDetailRetailData?.amountGasReturn,
            mDetailRetailData?.feeShip,
            tongTien - mDetailRetailData?.debtAmount!!
        )
    }

    private fun totalMustPay() {
        tongTien =
            tienKhiBan12 + tienKhiBan45 + tienVoBan12 + tienVoBan45 - (tienVoMua12 + tienVoMua45)
//        tvTongTienCanTT.text = "${CommonUtils.priceWithoutDecimal(tongTien.toDouble())} đ"
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
        banLeViewModel.onGetListCustomer(
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

    override fun onItemClick(view: View?, position: Int) {
        orderId = mList[position].order_id
        staffName = mList[position].staff_name
        createDate = mList[position].created_date
        canApproveStatus = mList[position].can_approve_status
        canCommentStatus = mList[position].can_comment_status
//        showMess(mList[position].approve_status)
        banLeViewModel.detailBanLe(orderId.toString())
    }
}