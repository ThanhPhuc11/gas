package vn.gas.thq.ui.qlyeucauduyetkehoach.chitiet

import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_detail_ke_hoach.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.util.AppDateUtils
import vn.hongha.ga.R

class DetailKeHoachFragment : BaseFragment() {
    private lateinit var viewModel: DetailKeHoachViewModel

    companion object {
        @JvmStatic
        fun newInstance() = DetailKeHoachFragment()
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
        viewModel.getKeHoachBH("21")
    }

    override fun initObserver() {
        viewModel.callbackDetailKHBH.observe(viewLifecycleOwner, {
            setViewData(it)
        })
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
    }
}