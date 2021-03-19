package vn.gas.thq.ui.thukho

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_qlyc_ca_nhan.*
import kotlinx.android.synthetic.main.fragment_thu_kho_qlyc_xuat_kho.*
import kotlinx.android.synthetic.main.fragment_thu_kho_qlyc_xuat_kho.btnSearch
import kotlinx.android.synthetic.main.fragment_thu_kho_qlyc_xuat_kho.edtEndDate
import kotlinx.android.synthetic.main.fragment_thu_kho_qlyc_xuat_kho.edtStartDate
import kotlinx.android.synthetic.main.fragment_thu_kho_qlyc_xuat_kho.edtStatus
import kotlinx.android.synthetic.main.fragment_thu_kho_qlyc_xuat_kho.rvRequestItem
import kotlinx.android.synthetic.main.layout_dialog_item_thu_kho.view.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.model.UserModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.qlyeucaucanhan.RequestItemAdapter
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.EndlessRecyclerViewScrollListener
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.gas.thq.util.dialog.GetListDataDemo
import vn.hongha.ga.R
import java.util.*

class ThuKhoXuatKhoFragment : BaseFragment(), RequestItemAdapter.ItemClickListener {

    private lateinit var viewModel: ThuKhoXuatKhoViewModel
    private lateinit var adapter: RequestItemAdapter
    private lateinit var adapterDetail: DetailItemProduct2Adapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var isReload: Boolean = false
    private var fromDate: String = ""
    private var endDate: String = ""
    private var alertDialog: AlertDialog? = null
    private var mDetalData: RequestDetailModel? = null
    private var staffCode: String? = null
    private var status: String? = null
    private var staffId: String? = null
    private var mList = mutableListOf<BussinesRequestModel>()
    private var mListStaff = mutableListOf<UserModel>()
    private var orderId = ""

    companion object {
        @JvmStatic
        fun newInstance(): ThuKhoXuatKhoFragment {
            val args = Bundle()

            val fragment = ThuKhoXuatKhoFragment()
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
                .get(ThuKhoXuatKhoViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Quản lý yêu cầu"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_thu_kho_qlyc_xuat_kho
    }

    override fun initObserver() {
        viewModel.mListStaffData.observe(viewLifecycleOwner, {
            mListStaff.clear()
            mListStaff.addAll(it)
        })

        viewModel.mLiveData.observe(viewLifecycleOwner, {
            if (isReload) {
                mList.clear()
            }
            mList.addAll(it)
            adapter.notifyDataSetChanged()
            isReload = false
        })

        viewModel.mDetailData.observe(viewLifecycleOwner, {
            mDetalData = it
            showDiglogDetail()
        })

        viewModel.mAcceptData.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(activity, "Thông báo", "Hoàn thành") {
                alertDialog?.dismiss()
                view?.let { it1 -> onSubmitData(it1) }
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

    override fun initData() {
        viewModel.getListStaff()
        initRecyclerView()
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
        edtLXBH.setOnClickListener(this::onChooseLXBH)
        edtStatus.setOnClickListener(this::onChooseStatus)
        btnSearch.setOnClickListener(this::onSubmitData)

//        btnItem1.setOnClickListener {
//            showDiglog1Button()
//        }
    }

    private fun initRecyclerView() {
        adapter = RequestItemAdapter(mList, "Xuất kho", null)
        adapter.setClickListener(this)

        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvRequestItem.layoutManager = linearLayoutManager
        rvRequestItem.adapter = adapter
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
//            if (AppConstants.NOT_SELECT == item.id) {
//                return@show
//            }
            staffCode = item.id
            edtLXBH.setText(item.name)

            if (AppConstants.SELECT_ALL == item.id) {
                staffCode = null
            }
        }
    }

    private fun onChooseStatus(view: View) {
        var doc = DialogList()
        var mArrayList = GetListDataDemo.getListStatus(Objects.requireNonNull(context))
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

    private fun onSubmitData(view: View) {
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
        viewModel.onSearchRequest(staffCode, status, fromDate, endDate, 0)
    }

    private fun setEndLessScrollListener() {
        rvRequestItem.clearOnScrollListeners()
        rvRequestItem.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMoreV2(totalItemsCount: Int) {
                Log.e("PHUCDZ", "$totalItemsCount")
                viewModel.onSearchRequest(staffCode, status, fromDate, endDate, totalItemsCount)
            }
        })
    }

    private fun showDiglogDetail(
//        title: String,
//        message: String,
//        callback: ConfirmDialogCallback?
    ) {
        val builder = context?.let { AlertDialog.Builder(it, R.style.AlertDialogNoBG) }
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.layout_dialog_item_thu_kho, null)
        builder?.setView(dialogView)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapterDetail = DetailItemProduct2Adapter(mDetalData!!.item)

        dialogView.apply {
            imgClose.setOnClickListener {
                alertDialog?.dismiss()
            }
            when (mDetalData?.status) {
                0 -> {
                    tvStatus.text = resources.getString(R.string.cancel_status)
                    tvStatus.setTextColor(resources.getColor(R.color.red_EA7035))
                    linearAccept.visibility = View.GONE
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
                }
                2 -> {
                    tvStatus.text = resources.getString(R.string.reject_status)
                    tvStatus.setTextColor(resources.getColor(R.color.red_EA7035))
                    linearAccept.visibility = View.GONE
                }
            }
            tvName.text = mDetalData?.staffName
//            tvDate.text = mDetalData?.createdDate
            tvDate.text = AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_6,
                AppDateUtils.FORMAT_1,
                mDetalData?.createdDate
            )
            tvOrderId.text = "Mã yêu cầu $orderId"

            rvProductDialog.layoutManager = linearLayoutManager
            rvProductDialog.adapter = adapterDetail

            btnTuChoi.setOnClickListener {
                CommonUtils.showConfirmDiglog2Button(
                    activity, "Xác nhận", "Bạn có chắc chắn muốn từ chối yêu cầu?", getString(
                        R.string.biometric_negative_button_text
                    ), getString(R.string.text_ok)
                ) {
                    if (it == AppConstants.YES) {
                        viewModel.acceptOrNotRequest(orderId, false)
                    }
                }
            }
            btnDongY.setOnClickListener {
                CommonUtils.showConfirmDiglog2Button(
                    activity, "Xác nhận", "Bạn có chắc chắn muốn phê duyệt yêu cầu?", getString(
                        R.string.biometric_negative_button_text
                    ), getString(R.string.text_ok)
                ) {
                    if (it == AppConstants.YES) {
                        viewModel.acceptOrNotRequest(orderId, true)
                    }
                }
            }
        }
        alertDialog = builder?.create()
        alertDialog?.window?.setLayout(500, 200)
        alertDialog?.show()
    }

    override fun onItemClick(view: View?, position: Int) {
        viewModel.onDetailRequest(mList[position].stock_trans_id.toString())
        orderId = mList[position].stock_trans_id.toString()
    }
}