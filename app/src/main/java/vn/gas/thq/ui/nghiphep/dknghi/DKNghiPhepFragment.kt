package vn.gas.thq.ui.nghiphep.dknghi

import androidx.lifecycle.ViewModelProviders
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.hongha.ga.R

class DKNghiPhepFragment : BaseFragment() {
    lateinit var viewModel: DKNghiPhepViewModel
    companion object {
        @JvmStatic
        fun newInstance() = DKNghiPhepFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_dang_ky_nghi
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
                .get(DKNghiPhepViewModel::class.java)
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initObserver() {

    }
}