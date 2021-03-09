package vn.gas.thq.ui.qlyeucauduyetkehoach

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_qlyc_ke_hoach.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.qlyeucauduyetkehoach.chitiet.DetailKeHoachFragment
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.ScreenId
import vn.hongha.ga.R

class QLYCKeHoachFragment : BaseFragment(), RequestItemKHBHAdapter.ItemClickListener {
    private lateinit var viewModel: QLYCKeHoachViewModel
    private var status: Int? = null
    private var staffCode: String? = null
    private var shopCode: String? = null
    private var listKHBH = mutableListOf<KHBHOrderModel>()
    private lateinit var adapterKHBH: RequestItemKHBHAdapter

    companion object {
        @JvmStatic
        fun newInstance() = QLYCKeHoachFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_qlyc_ke_hoach
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
                .get(QLYCKeHoachViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Quản lý yêu cầu KH bán hàng"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun initData() {
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
        btnSearch.setOnClickListener(this::onSearch)
        initRecyclerView()
    }

    override fun initObserver() {
        viewModel.callbackListKHBH.observe(viewLifecycleOwner, {
            listKHBH.clear()
            listKHBH.addAll(it)
            adapterKHBH.notifyDataSetChanged()
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

    private fun initRecyclerView() {
        adapterKHBH = RequestItemKHBHAdapter(listKHBH)
        adapterKHBH.setClickListener(this)

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvKHBH.layoutManager = linearLayoutManager
        rvKHBH.adapter = adapterKHBH
    }

    private fun onSearch(view: View) {
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
        viewModel.getKeHoachBH(status, fromDate, endDate, staffCode, shopCode, null, 0, 1000)
    }

    override fun onItemClick(view: View?, position: Int) {
        viewController?.pushFragment(
            ScreenId.SCREEN_DETAIL_KE_HOACH,
            DetailKeHoachFragment.newInstance()
        )
    }
}