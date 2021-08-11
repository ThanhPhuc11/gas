package vn.gas.thq.ui.xuatkhoKH.thuchienxuatkhokh

import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.retail.Customer
import vn.hongha.ga.R

class THXuatKhoKHFragment : BaseFragment() {
    private lateinit var viewModel: THXuatKhoKHViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var mListCustomer = mutableListOf<Customer>()

    companion object {
        @JvmStatic
        fun newInstance() = THXuatKhoKHFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_thuc_hien_xuat_kho_kh
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
                .get(THXuatKhoKHViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Xuất kho cho khách hàng"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun initData() {
        val user = AppPreferencesHelper(context).userModel
        val queryKH = "shopId==${user.shopId};status==1"
//        viewModel.onGetListCustomer(queryKH, 0)
    }

    override fun initObserver() {
        viewModel.callbackListKH.observe(viewLifecycleOwner, {
            mListCustomer.clear()
            mListCustomer.addAll(it)
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
}