package vn.gas.thq.ui.pheduyetgia

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_phe_duyet_gia.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.model.StatusValueModel
import vn.gas.thq.model.UserModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.hongha.ga.R
import java.util.*

class PheDuyetGiaFragment : BaseFragment() {
    private lateinit var viewModel: PheDuyetGiaViewModel
    private lateinit var adapter: RequestApproveAdapter
    private var mListStaff = mutableListOf<UserModel>()
    private var listStatusOrderSale = mutableListOf<StatusValueModel>()
    private var mList = mutableListOf<BussinesRequestModel>()
    private var status: String? = null
    private var staffCode: String? = null

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
        adapter = RequestApproveAdapter(mList)
//        adapter.setClickListener(this)

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
        viewModel.onSearchRetail(status, fromDate, endDate)
    }
}