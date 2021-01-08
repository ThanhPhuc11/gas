package vn.gas.thq.ui.thukho

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_thu_kho_qlyc_xuat_kho.*
import kotlinx.android.synthetic.main.layout_dialog_item_thu_kho.view.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.qlyeucaucanhan.RequestItemAdapter
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.GetListDataDemo
import vn.hongha.ga.R
import java.util.*

class ThuKhoXuatKhoFragment : BaseFragment(), RequestItemAdapter.ItemClickListener {

    private lateinit var viewModel: ThuKhoXuatKhoViewModel
    private lateinit var adapter: RequestItemAdapter
    private lateinit var adapterDetail: DetailItemProduct2Adapter
    private var alertDialog: AlertDialog? = null
    private var mDetalData: RequestDetailModel? = null
    private var status: String? = null
    private var mList = mutableListOf<BussinesRequestModel>()
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
        viewModel.mLiveData.observe(viewLifecycleOwner, {
            mList.clear()
            mList.addAll(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.mDetailData.observe(viewLifecycleOwner, {
            mDetalData = it
            showDiglog1Button()
        })

        viewModel.mAcceptData.observe(viewLifecycleOwner, {
            alertDialog?.dismiss()
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

//        btnItem1.setOnClickListener {
//            showDiglog1Button()
//        }
    }

    private fun initRecyclerView() {
        adapter = RequestItemAdapter(mList)
        adapter.setClickListener(this)

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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
            if (AppConstants.NOT_SELECT == item.id) {
                return@show
            }
            status = item.id
            edtStatus.setText(item.name)
        }
    }

    private fun onSubmitData(view: View) {
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
        viewModel.onSearchRequest(status, fromDate, endDate)
    }

    private fun showDiglog1Button(
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
                1 -> {
                    tvStatus.text = "Chờ duyệt"
                    linearAccept.visibility = View.VISIBLE
                }
                2 -> {
                    tvStatus.text = "Đã duyệt"
                    linearAccept.visibility = View.GONE
                }
                3 -> {
                    tvStatus.text = "Đã huỷ"
                    linearAccept.visibility = View.GONE
                }
            }
            tvName.text = mDetalData?.staffName
            tvDate.text = mDetalData?.createdDate
            tvOrderId.text = "Mã yêu cầu $orderId"

            rvProductDialog.layoutManager = linearLayoutManager
            rvProductDialog.adapter = adapterDetail

            btnTuChoi.setOnClickListener {
                viewModel.acceptOrNotRequest(orderId, false)
            }
            btnDongY.setOnClickListener {
                viewModel.acceptOrNotRequest(orderId, true)
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