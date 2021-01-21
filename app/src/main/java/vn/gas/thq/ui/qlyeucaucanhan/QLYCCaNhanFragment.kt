package vn.gas.thq.ui.qlyeucaucanhan

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_qlyc_ca_nhan.*
import kotlinx.android.synthetic.main.fragment_qlyc_ca_nhan.tvName
import kotlinx.android.synthetic.main.layout_dialog_item_ycxk.view.*
import kotlinx.android.synthetic.main.layout_dialog_item_ycxk.view.btnHuyYC
import kotlinx.android.synthetic.main.layout_dialog_item_ycxk.view.imgClose
import kotlinx.android.synthetic.main.layout_dialog_item_ycxk.view.linearAccept
import kotlinx.android.synthetic.main.layout_dialog_item_ycxk.view.tvName
import kotlinx.android.synthetic.main.layout_dialog_item_ycxk.view.tvOrderId
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.model.StatusValueModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.retail.ApproveRequestModel
import vn.gas.thq.ui.thukho.RequestDetailModel
import vn.gas.thq.ui.thukho.ThuKhoXuatKhoViewModel
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.AppDateUtils.FORMAT_2
import vn.gas.thq.util.AppDateUtils.FORMAT_5
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.ScreenId
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
    private var alertDialog: AlertDialog? = null
    private var orderId = ""
    private var mDetailYCXKData: RequestDetailModel? = null
    private var mDetailRetailData: ApproveRequestModel? = null
    var mList = mutableListOf<BussinesRequestModel>()
    private var listStatusOrderSale = mutableListOf<StatusValueModel>()
    private var loaiYC: String? = "Xuất kho"
    private var status: String? = null
    private var type: String? = null
    private var isRetail: Boolean = false

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
        tvTitle.text = "Quản lý yêu cầu"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_qlyc_ca_nhan
    }

    override fun initObserver() {
        viewModel.mLiveData.observe(viewLifecycleOwner, {
            mList.clear()
            mList.addAll(it)
            adapter.notifyDataSetChanged()
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
        })

        viewModel.detailApproveCallback.observe(viewLifecycleOwner, {
            mDetailRetailData = it
            showDiglogDetailRetail()
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

    override fun initData() {
        if (ScreenId.HOME_SCREEN != arguments?.getString("SCREEN", "")) {
            isRetail = true
            loaiYC = "Bán hàng"
            type = "2"
            edtRequestType.setText("Bán lẻ")
        } else {
            isRetail = false
            loaiYC = "Xuất kho"
            type = "1"
            edtRequestType.setText("Xuất kho")
        }
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

    private fun getInfo() {
        val userModel = AppPreferencesHelper(context).userModel
        tvName.text = userModel?.name
        tvTuyen.text = userModel?.email
    }

    private fun initRecyclerView() {
        adapter = RequestItemAdapter(mList, loaiYC)
        adapter.setClickListener(this)

        val linearLayoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
        rvRequestItem.layoutManager = linearLayoutManager
        rvRequestItem.adapter = adapter
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
            initRecyclerView()
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
        var fromDate =
            AppDateUtils.changeDateFormat(FORMAT_2, FORMAT_5, edtStartDate.text.toString())
        var endDate =
            AppDateUtils.changeDateFormat(FORMAT_2, FORMAT_5, edtEndDate.text.toString())
        if (isRetail) {
            viewModel.onSearchRetail(status, fromDate, endDate)
            return
        }
        viewModel.onSubmitData(status, fromDate, endDate)
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
                2 -> {
                    tvStatus.text = resources.getString(R.string.approved_status)
                    tvStatus.setTextColor(resources.getColor(R.color.blue_14AFB4))
                    linearAccept.visibility = View.GONE
                    adapterDetailYCXK.isReadOnly()
                }
                3 -> {
                    tvStatus.text = resources.getString(R.string.reject_status)
                    tvStatus.setTextColor(resources.getColor(R.color.red_EA7035))
                    linearAccept.visibility = View.GONE
                    adapterDetailYCXK.isReadOnly()
                }
            }
            tvName.text = mDetailYCXKData?.staffName
            tvDate.text = mDetailYCXKData?.createdDate
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

        val btnCongNo12: Button = dialogView.findViewById(R.id.btnCongNo12)
        val btnCongNo45: Button = dialogView.findViewById(R.id.btnCongNo45)
        val btnCongNoTien: Button = dialogView.findViewById(R.id.btnCongNoTien)

        dialogView.apply {
            imgClose.setOnClickListener {
                alertDialog?.dismiss()
            }
            tvOrderId.text = "Mã yêu cầu $orderId"
            tvName.text = mDetailRetailData?.customerName ?: "- -"
            tvAddress.text = mDetailRetailData?.customerAddress ?: "- -"
            tvPhone.text = mDetailRetailData?.customerTelContact ?: "- -"

            btnCongNo12.text = mDetailRetailData?.debtAmountTank12?.toString() ?: "0"
            btnCongNo45.text = mDetailRetailData?.debtAmountTank45?.toString() ?: "0"
            btnCongNoTien.text =
                "${CommonUtils.priceWithoutDecimal(mDetailRetailData?.debtAmount?.toDouble())}"
//            rvProductDialog.layoutManager = linearLayoutManager
//            rvProductDialog.adapter = adapterDetailYCXK


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