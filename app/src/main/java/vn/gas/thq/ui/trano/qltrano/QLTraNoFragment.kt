package vn.gas.thq.ui.trano.qltrano

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_ql_tra_no.*
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.xemkho.KhoModel
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.hongha.ga.R
import java.util.*

class QLTraNoFragment : BaseFragment() {
    private lateinit var viewModel: QLTraNoViewModel
    private lateinit var adapter: TraNoAdapter
    private var listSangChiet = mutableListOf<HistoryTraNoModel>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var listKho = mutableListOf<KhoModel>()
    private var shopId: String? = null

    companion object {
        @JvmStatic
        fun newInstance() = QLTraNoFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_ql_tra_no
    }

    override fun setViewController() {

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
                .get(QLTraNoViewModel::class.java)
    }

    override fun initView() {

    }

    override fun initData() {
        viewModel.getListKho()
        initRecyclerView()
        edtKH.setOnClickListener(this::onChooseShop)
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
        btnSearch.setOnClickListener(this::searchSangChiet)
    }

    override fun initObserver() {
        viewModel.listKho.observe(viewLifecycleOwner, {
            listKho.clear()
            val listOnlyType1 = it.filter { it.type == 1 }
            listKho.addAll(listOnlyType1)
            if (listOnlyType1.isNotEmpty()) {
                shopId = listOnlyType1[0].id.toString()
                edtKH.setText(listOnlyType1[0].code + " - " + listOnlyType1[0].name)
//                viewModel.getDataFromCode(shopCode, null)
            }
        })

        viewModel.listHistory.observe(viewLifecycleOwner, {
            listSangChiet.clear()
            listSangChiet.addAll(it)
            adapter.notifyDataSetChanged()
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

    private fun searchSangChiet(view: View) {
        val fromDate =
            AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_2,
                AppDateUtils.FORMAT_5,
                edtStartDate.text.toString()
            )
        val endDate =
            AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_2,
                AppDateUtils.FORMAT_5,
                edtEndDate.text.toString()
            )
        if (shopId != null) {
            viewModel.historySangChiet(shopId!!.toInt(), fromDate, endDate)
        }
    }

    private fun initRecyclerView() {
        adapter = TraNoAdapter(listSangChiet)
//        adapter.setClickListener(this)

        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvResult.layoutManager = linearLayoutManager
        rvResult.adapter = adapter
    }

    private fun onChooseShop(view: View) {
        val doc = DialogList()
        val mArrayList = ArrayList<DialogListModel>()
        listKho.forEach {
            mArrayList.add(
                DialogListModel(
                    it.id.toString(),
                    "${it.code} - ${it.name}",
                    it.type.toString()
                )
            )
        }

        doc.show(
            activity, mArrayList,
            "Trạm",
            getString(R.string.enter_text_search)
        ) { item ->
            shopId = item.id
//            staffCode = null
//            if (item.other == "1") {
//                shopCode = item.id
//            } else {
//                staffCode = item.id
//            }
            edtKH.setText(item.name)
        }
    }
}