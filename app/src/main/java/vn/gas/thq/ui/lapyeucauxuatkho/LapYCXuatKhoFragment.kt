package vn.gas.thq.ui.lapyeucauxuatkho

import android.os.Bundle
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

class LapYCXuatKhoFragment : BaseFragment() {
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
        viewModel.successCallBack2.observe(this, {
            Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show()
            viewController?.popFragment()
        })
    }

    override fun initData() {
//        mList.add(ProductModel("Khí 12Kg", "Khi12kg", "SKU: Binh gas 12kg", "Khí", 0))
//        mList.add(ProductModel("Khí 45Kg", "Khi45kg", "SKU: Binh gas 45kg", "Khí", 0))
//        mList.add(ProductModel("Vỏ 12Kg", "Vo12kg", "SKU: Binh gas 12kg", "Vỏ", 0))
//        mList.add(ProductModel("Vỏ 45Kg", "Vo45kg", "SKU: Binh gas 45kg", "Vỏ", 0))
        viewModel.getDataFromShop()
        productAdapter = ProductItemAdapter(mList)
//        productAdapter.setClickListener(this)

        val linearLayoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
        rvRequestItem.layoutManager = linearLayoutManager
        rvRequestItem.adapter = productAdapter

        btnSubmit.setOnClickListener(this::onSubmitData)
    }

    private fun onSubmitData(view: View) {
//        for (obj: ProductModel in productAdapter) {
//            val productTemp =
//        }
        var initExportRequest = InitExportRequest()
        var productModel = ProductModel("", "TANK45", "", "", 1)
        initExportRequest.item = mutableListOf()
        initExportRequest.item?.add(productModel)
        viewModel.onSubmitData(initExportRequest)
    }
}