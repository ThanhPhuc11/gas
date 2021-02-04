package vn.gas.thq.ui.pheduyetgia

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_phe_duyet_gia.*
import kotlinx.android.synthetic.main.layout_dialog_item_duyet_gia.*
import kotlinx.android.synthetic.main.layout_dialog_item_thu_kho.view.*
import kotlinx.android.synthetic.main.layout_item_phe_duyet_gia.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.layout_toolbar.tvTitle
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.model.StatusValueModel
import vn.gas.thq.model.UserModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.retail.ApproveRequestModel
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.hongha.ga.R
import java.util.*

class PheDuyetGiaFragment : BaseFragment(), RequestApproveAdapter.ItemClickListener {
    private lateinit var viewModel: PheDuyetGiaViewModel
    private lateinit var adapter: RequestApproveAdapter
    private var mListStaff = mutableListOf<UserModel>()
    private var listStatusOrderSale = mutableListOf<StatusValueModel>()
    private var mList = mutableListOf<BussinesRequestModel>()
    private var mDetailRetailData: ApproveRequestModel? = null
    private var status: String? = null
    private var staffCode: String? = null
    private var alertDialog: AlertDialog? = null
    private var statusShowDialog: Int? = null
    private var orderId: Int? = null
    private var staffName: String? = null
    private var createDate: String? = null

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

    companion object {
        @JvmStatic
        fun newInstance(): PheDuyetGiaFragment {
            val args = Bundle()

            val fragment = PheDuyetGiaFragment()
            fragment.arguments = args
            return fragment
        }
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
                .get(PheDuyetGiaViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Phê duyệt yêu cầu giảm giá"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_phe_duyet_gia
    }

    override fun initObserver() {
        viewModel.mListStaffData.observe(viewLifecycleOwner, {
            mListStaff.clear()
            mListStaff.addAll(it)
        })

        viewModel.listStatus.observe(viewLifecycleOwner, {
            listStatusOrderSale.clear()
            listStatusOrderSale.addAll(it)
        })

        viewModel.mListDataSearch.observe(viewLifecycleOwner, {
            mList.clear()
            mList.addAll(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.detailApproveCallback.observe(viewLifecycleOwner, {
            mDetailRetailData = it
//            mapListToRetailProduct()
            autoSelectDialog(it)
        })

        viewModel.callbackAccept.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(activity, "Thông báo", "Phê duyệt yêu cầu thành công") {
                alertDialog?.dismiss()
                view?.let { it1 -> onSearchData(it1) }
            }
        })

        viewModel.callbackReject.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(activity, "Thông báo", "Từ chối yêu cầu thành công") {
                alertDialog?.dismiss()
                view?.let { it1 -> onSearchData(it1) }
            }
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
    }

    override fun initData() {
        viewModel.getListStaff()
        viewModel.onGetSaleOrderStatus()
        initRecyclerView()

        edtLXBH.setOnClickListener(this::onChooseLXBH)
        edtStatus.setOnClickListener(this::onChooseStatus)
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

        btnSearch.setOnClickListener(this::onSearchData)
    }

    private fun initRecyclerView() {
        adapter = context?.let { RequestApproveAdapter(mList, listStatusOrderSale, it) }!!
        adapter.setClickListener(this)

        val linearLayoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
        rvRequestApprove.layoutManager = linearLayoutManager
        rvRequestApprove.adapter = adapter
    }

    private fun onChooseLXBH(view: View) {
        val doc = DialogList()
        var mArrayList = ArrayList<DialogListModel>()
        mArrayList.add(0, DialogListModel(AppConstants.SELECT_ALL, getString(R.string.all)))
        mListStaff.forEach {
            mArrayList.add(DialogListModel(it.staffCode, it.name))
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

    private fun onSearchData(view: View) {
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
        viewModel.onSearchRetail(status, staffCode, fromDate, endDate)
    }

    private fun autoSelectDialog(it: ApproveRequestModel) {
        val array = it.approveStatus?.toCharArray()
        val a = array?.get(0).toString()
        val b = array?.get(1).toString()
        val c = array?.get(2).toString()
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
            // vo cong no
            statusShowDialog = 3
            congNoGiaTang12 = mDetailRetailData?.debtAmountTank12 ?: 0
            congNoGiaTang45 = mDetailRetailData?.debtAmountTank45 ?: 0
            congNoGiaTang = mDetailRetailData?.debtAmount ?: 0
            showDiglogDetailRetail()
            return
        }
    }

    private fun setValueApproveDetail(type: Int) {
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

    private fun showDiglogDetailRetail() {
        val builder = context?.let { AlertDialog.Builder(it, R.style.AlertDialogNoBG) }
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.layout_dialog_item_duyet_gia, null)
        builder?.setView(dialogView)
        val tvTitle: TextView = dialogView.findViewById(R.id.tvTitle)
        val imgClose: ImageView = dialogView.findViewById(R.id.imgClose)

        val tvNameLXBH: TextView = dialogView.findViewById(R.id.tvNameLXBH)
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

        val edtReason: EditText = dialogView.findViewById(R.id.edtReason)

        val cardViewCongNo: CardView = dialogView.findViewById(R.id.cardViewCongNo)
        val cardViewGia: CardView = dialogView.findViewById(R.id.cardViewGia)
        val btnDongY: Button = dialogView.findViewById(R.id.btnDongY)
        val btnTuChoi: Button = dialogView.findViewById(R.id.btnTuChoi)

        tvNameLXBH.text = staffName
        tvNameCust.text = mDetailRetailData?.customerName
//        tvDate.text = createDate
        tvDate.text = AppDateUtils.changeDateFormat(
            AppDateUtils.FORMAT_6,
            AppDateUtils.FORMAT_1,
            createDate
        )
        tvStatus.text = "${mDetailRetailData?.status};${mDetailRetailData?.approveStatus}"
        listStatusOrderSale.forEach {
            if (it.value == "${mDetailRetailData?.status};${mDetailRetailData?.approveStatus}") {
                tvStatus.text = it.name
                return@forEach
            }
        }

        when (statusShowDialog) {
            1 -> {
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
            }
            2 -> {
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
            }
            3 -> {
                tvTitle.text = "Phê duyệt công nợ"
                cardViewGia.visibility = View.GONE

                tvCongNo12.text = congNoGiaTang12.toString()
                tvCongNo45.text = congNoGiaTang45.toString()
                tvCongNo.text = "${CommonUtils.priceWithoutDecimal(congNoGiaTang?.toDouble())} đ"
            }
        }

        imgClose.setOnClickListener {
            alertDialog?.dismiss()
        }

        btnDongY.setOnClickListener {
            CommonUtils.showConfirmDiglog2Button(
                activity, "Xác nhận", "Bạn có chắc chắn muốn phê duyệt yêu cầu?", getString(
                    R.string.biometric_negative_button_text
                ), getString(R.string.text_ok)
            ) {
                if (it == AppConstants.YES) {
                    viewModel.doAcceptDuyetGia(orderId?.toString(), DuyetGiaModel(statusShowDialog))
                }
            }
        }

        btnTuChoi.setOnClickListener {
            CommonUtils.showConfirmDiglog2Button(
                activity, "Xác nhận", "Bạn có chắc chắn muốn từ chối yêu cầu?", getString(
                    R.string.biometric_negative_button_text
                ), getString(R.string.text_ok)
            ) {
                if (it == AppConstants.YES) {
                    viewModel.doRejectDuyetGia(
                        orderId?.toString(),
                        DuyetGiaModel(statusShowDialog, edtReason.text.toString())
                    )
                }
            }
        }

        alertDialog = builder?.create()
        alertDialog?.window?.setLayout(500, 200)
        alertDialog?.show()
    }

    override fun onItemClick(view: View?, position: Int) {
        orderId = mList[position].order_id
        staffName = mList[position].staff_name
        createDate = mList[position].created_date
        showMess(mList[position].approve_status)
        viewModel.detailApproveLXBH(orderId.toString())
    }
}