package vn.gas.thq.ui.qlyeucauduyetkehoach.chitiet

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_detail_ke_hoach.*
import kotlinx.android.synthetic.main.fragment_detail_ke_hoach.rvKeHoach
import kotlinx.android.synthetic.main.fragment_detail_ke_hoach.tvPlanTime
import kotlinx.android.synthetic.main.fragment_detail_ke_hoach.tvStaff
import kotlinx.android.synthetic.main.fragment_detail_ke_hoach.tvTram
import kotlinx.android.synthetic.main.fragment_detail_ke_hoach.tvTuyenBH
import kotlinx.android.synthetic.main.fragment_init_ke_hoach.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.customview.CustomArrayAdapter
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.kehoachbh.DSKeHoachAdapter
import vn.gas.thq.ui.kehoachbh.KeHoachModel
import vn.gas.thq.util.AppDateUtils
import vn.hongha.ga.R

class DetailKeHoachFragment : BaseFragment() {
    private lateinit var viewModel: DetailKeHoachViewModel
    private lateinit var adapter: DSDetailKeHoachAdapter
    private var listKHBH = mutableListOf<KeHoachModel>()

    companion object {
        @JvmStatic
        fun newInstance(id: String) = DetailKeHoachFragment().apply {
            arguments = Bundle().apply {
                putString("ID", id)
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_detail_ke_hoach
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
                .get(DetailKeHoachViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Quản lý yêu cầu KH bán hàng"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun initData() {
        val id = arguments?.getString("ID")
        if (id != null) {
            viewModel.getKeHoachBH(id)
        }
        initRecyclerView()
    }

    override fun initObserver() {
        viewModel.callbackDetailKHBH.observe(viewLifecycleOwner, {
            setViewData(it)
        })
    }

    private fun initRecyclerView() {
        adapter = DSDetailKeHoachAdapter(listKHBH)
//        adapter.setClickListener(this)
        val linearLayoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
        rvKeHoach.layoutManager = linearLayoutManager
        rvKeHoach.adapter = adapter
    }

    private fun setViewData(obj: DetailKHBHModel) {
        tvTram.text = obj.shopName
        tvTuyenBH.text = obj.saleLineName
        tvStaff.text = obj.staffName
        tvPlanTime.text = AppDateUtils.changeDateFormat(
            AppDateUtils.FORMAT_6,
            AppDateUtils.FORMAT_1,
            obj.effectDate
        )

        listKHBH.clear()
        obj.detail?.let { listKHBH.addAll(it) }
        adapter.notifyDataSetChanged()
    }
}