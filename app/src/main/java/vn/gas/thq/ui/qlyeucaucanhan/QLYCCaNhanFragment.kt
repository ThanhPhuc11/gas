package vn.gas.thq.ui.qlyeucaucanhan

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_qlyc_ca_nhan.*
import kotlinx.android.synthetic.main.fragment_qlyc_ca_nhan.btnSearch
import kotlinx.android.synthetic.main.fragment_qlyc_ca_nhan.edtEndDate
import kotlinx.android.synthetic.main.fragment_qlyc_ca_nhan.edtStartDate
import kotlinx.android.synthetic.main.fragment_qlyc_ca_nhan.edtStatus
import kotlinx.android.synthetic.main.fragment_qlyc_ca_nhan.rvRequestItem
import kotlinx.android.synthetic.main.fragment_thu_kho_qlyc_xuat_kho.*
import kotlinx.android.synthetic.main.layout_dialog_item_ycxk.view.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.thukho.RequestDetailModel
import vn.gas.thq.ui.thukho.ThuKhoXuatKhoViewModel
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.AppDateUtils.FORMAT_2
import vn.gas.thq.util.AppDateUtils.FORMAT_5
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.dialog.DialogList
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
    private var mDetalData: RequestDetailModel? = null
    var mList = mutableListOf<BussinesRequestModel>()
    private var status: String? = null

    companion object {
        @JvmStatic
        fun newInstance(): QLYCCaNhanFragment {
            val args = Bundle()

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
        viewModel.mLiveData.observe(this, {
            mList.clear()
            mList.addAll(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.mCancelData.observe(viewLifecycleOwner, {
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

        //TODO: Thu Kho
        viewModelThuKho.mDetailData.observe(viewLifecycleOwner, {
            mDetalData = it
            showDiglogDetail()
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
        edtStatus.setOnClickListener(this::onChooseStatus)
        btnSearch.setOnClickListener(this::onSubmitData)
    }

    private fun getInfo() {
        val userModel = AppPreferencesHelper(context).userModel
        tvName.text = userModel?.name
        tvTuyen.text = userModel?.email
    }

    private fun initRecyclerView() {
        adapter = RequestItemAdapter(mList)
        adapter.setClickListener(this)

        val linearLayoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
        rvRequestItem.layoutManager = linearLayoutManager
        rvRequestItem.adapter = adapter
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
        var fromDate =
            AppDateUtils.changeDateFormat(FORMAT_2, FORMAT_5, edtStartDate.text.toString())
        var endDate =
            AppDateUtils.changeDateFormat(FORMAT_2, FORMAT_5, edtEndDate.text.toString())
        viewModel.onSubmitData(status, fromDate, endDate)
    }

    private fun showDiglogDetail(
//        title: String,
//        message: String,
//        callback: ConfirmDialogCallback?
    ) {
        val builder = context?.let { AlertDialog.Builder(it, R.style.AlertDialogNoBG) }
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.layout_dialog_item_ycxk, null)
        builder?.setView(dialogView)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapterDetailYCXK = DetailItemProduct4Adapter(mDetalData!!.item)

        dialogView.apply {
            imgClose.setOnClickListener {
                alertDialog?.dismiss()
            }
            when (mDetalData?.status) {
                0 -> {
                    tvStatus.text = "Đã huỷ"
                    tvStatus.setTextColor(resources.getColor(R.color.red_EA7035))
                    linearAccept.visibility = View.GONE
                    adapterDetailYCXK.isReadOnly()
                }
                1 -> {
                    tvStatus.text = "Chờ duyệt"
                    tvStatus.setTextColor(resources.getColor(R.color.blue_14AFB4))
                    linearAccept.visibility = View.VISIBLE
                }
                2 -> {
                    tvStatus.text = "Đã duyệt"
                    tvStatus.setTextColor(resources.getColor(R.color.blue_14AFB4))
                    linearAccept.visibility = View.GONE
                    adapterDetailYCXK.isReadOnly()
                }
                3 -> {
                    tvStatus.text = "Từ chối"
                    tvStatus.setTextColor(resources.getColor(R.color.red_EA7035))
                    linearAccept.visibility = View.GONE
                    adapterDetailYCXK.isReadOnly()
                }
            }
            tvName.text = mDetalData?.staffName
            tvDate.text = mDetalData?.createdDate
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

    override fun onItemClick(view: View?, position: Int) {
        viewModelThuKho.onDetailRequest(mList[position].stock_trans_id.toString())
        orderId = mList[position].stock_trans_id.toString()
    }
}