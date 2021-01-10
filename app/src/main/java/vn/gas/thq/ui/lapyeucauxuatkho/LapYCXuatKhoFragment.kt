package vn.gas.thq.ui.lapyeucauxuatkho

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_init_export_request.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.model.ProductModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.hongha.ga.R

class LapYCXuatKhoFragment : BaseFragment(), ProductItemAdapter.ItemClickListener {
    private lateinit var viewModel: LapYCXuatKhoViewModel
    private lateinit var productAdapter: ProductItemAdapter
    var mList = mutableListOf<ProductModel>()


    companion object {
        @JvmStatic
        fun newInstance(): LapYCXuatKhoFragment {
            val args = Bundle()

            val fragment = LapYCXuatKhoFragment()
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
                .get(LapYCXuatKhoViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Tạo yêu cầu xuất kho"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_init_export_request
    }

    override fun initObserver() {
        viewModel.mLiveData.observe(this, {
            it.forEach {
                mList.add(ProductModel(it.productName, it.productCode, "", "", 0))
            }
            productAdapter.notifyDataSetChanged()
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
        viewModel.getDataFromShop()
        productAdapter = ProductItemAdapter(mList)
        productAdapter.setClickListener(this)

        val linearLayoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
        rvRequestItem.layoutManager = linearLayoutManager
        rvRequestItem.adapter = productAdapter

        btnSubmit.setOnClickListener(this::onSubmitData)
    }

    private fun onSubmitData(view: View) {
        var initExportRequest = InitExportRequest()
        initExportRequest.item = mutableListOf()
        initExportRequest.item?.addAll(mList)
        viewModel.onSubmitData(initExportRequest)
    }

    override fun onItemSLChanged(position: Int, count: Int) {
        mList[position].quantity = count
        Log.e("POSITION: $position", "SL $count")
    }
}