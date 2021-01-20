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
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.model.ProductModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.CommonUtils
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
        viewModel.mLiveData.observe(viewLifecycleOwner, {
            mList.clear()
            it.forEach {
                mList.add(ProductModel(it.productName, it.productCode, "", "", 0))
            }
            productAdapter.notifyDataSetChanged()
        })

        viewModel.callbackInitSuccess.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(activity, "Thông báo", "Lập yêu cầu thành công") {
                viewModel.getDataFromShop()
            }
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
        getInfo()
        viewModel.getDataFromShop()
        productAdapter = ProductItemAdapter(mList)
        productAdapter.setClickListener(this)

        val linearLayoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
        rvRequestItem.layoutManager = linearLayoutManager
        rvRequestItem.adapter = productAdapter

        btnSubmit.setOnClickListener(this::onSubmitData)
    }

    private fun getInfo() {
        val userModel = AppPreferencesHelper(context).userModel
        tvName.text = userModel?.name
        tvTuyen.text = userModel?.email
    }

    private fun onSubmitData(view: View) {
        var initExportRequest = InitExportRequest()
        initExportRequest.item = mutableListOf()
        val requestList = mList.filter { it.quantity!! > 0 }
        if (requestList.isNotEmpty()) {
            initExportRequest.item?.addAll(requestList)
            CommonUtils.showConfirmDiglog2Button(
                activity, "Xác nhận", "Bạn có chắc chắn muốn tạo yêu cầu xuất kho?", getString(
                    R.string.biometric_negative_button_text
                ), getString(R.string.text_ok)
            ) {
                if (it == AppConstants.YES) {
                    viewModel.onSubmitData(initExportRequest)
                }
            }
//            viewModel.onSubmitData(initExportRequest)
            return
        }
        showMess("Bạn chưa chọn sản phẩm nào")

    }

    override fun onItemSLChanged(position: Int, count: Int) {
        mList[position].quantity = count
        Log.e("POSITION: $position", "SL $count")
    }
}