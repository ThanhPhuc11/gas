package vn.gas.thq.ui.kiemkekho

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_kiem_ke_kho.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.model.ProductModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.nhapkho.ProductNhapKhoModel
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.CommonUtils
import vn.hongha.ga.R

class KiemKeKhoFragment : BaseFragment(), KKKhoItemAdapter.ItemClickListener {
    private lateinit var viewModel: KiemKeKhoViewModel
    private lateinit var productAdapter: KKKhoItemAdapter
    private var alertDialog: AlertDialog? = null
    private var mOldList = mutableListOf<ProductModel>()
    private var mNewList = mutableListOf<ProductModel>()
    private lateinit var kiemKeRequestModel: KiemKeRequestModel

    companion object {
        @JvmStatic
        fun newInstance(): KiemKeKhoFragment {
            val args = Bundle()

            val fragment = KiemKeKhoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_kiem_ke_kho
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
                .get(KiemKeKhoViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Kiểm kê kho"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun initData() {
        viewModel.getDataFromCode("NTL", null)
        tvCheckDate.text = "Ngày kiểm kê: ${AppDateUtils.getCurrentDate()}"
        initRecyclerView()
        btnKiemKe.setOnClickListener(this::onSubmit)
    }

    override fun initObserver() {
        viewModel.mLiveData.observe(viewLifecycleOwner, {
            mOldList.clear()
            mNewList.clear()
            it.forEach {
                mOldList.add(
                    ProductModel(
                        it.productName,
                        it.productCode,
                        "",
                        "",
                        it.currentQuantity,
                        it.unit
                    )
                )
                mNewList.add(
                    ProductModel(
                        it.productName,
                        it.productCode,
                        "",
                        "",
                        0,
                        it.unit
                    )
                )
            }
            productAdapter.notifyDataSetChanged()
        })

        viewModel.callbackKiemKeKho.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(activity, "Thông báo", "Hoàn thành") {
                alertDialog?.dismiss()
                viewModel.getDataFromCode("NTL", "admin")
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

    private fun initRecyclerView() {
        productAdapter = context?.let { KKKhoItemAdapter(mOldList, it) }!!
        productAdapter.setClickListener(this)

        val linearLayoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
        rvKho.layoutManager = linearLayoutManager
        rvKho.adapter = productAdapter
    }

//    private fun onChooseLXBH(view: View) {
//        val doc = DialogList()
//        var mArrayList = ArrayList<DialogListModel>()
//        mArrayList.add(0, DialogListModel(AppConstants.SELECT_ALL, getString(R.string.all)))
//        mListStaff.forEach {
//            mArrayList.add(DialogListModel(it.staffCode, it.name))
//        }
//        doc.show(
//            activity, mArrayList,
//            getString(R.string.lxbh),
//            getString(R.string.enter_text_search)
//        ) { item ->
////            if (AppConstants.NOT_SELECT == item.id) {
////                return@show
////            }
//            staffCode = item.id
//            edtLXBH.setText(item.name)
//
//            if (AppConstants.SELECT_ALL == item.id) {
//                staffCode = null
//            }
//        }
//    }

    private fun onSubmit(view: View) {
//        viewModel.getDataFromShop()
        CommonUtils.showConfirmDiglog2Button(
            activity, "Xác nhận", "Bạn có chắc chắn muốn kiểm kê?", getString(
                R.string.biometric_negative_button_text
            ), getString(R.string.text_ok)
        ) {
            if (it == AppConstants.YES) {
                kiemKeRequestModel = KiemKeRequestModel()
                kiemKeRequestModel.shopCode = "NTL"
                mOldList.forEach {
                    kiemKeRequestModel.originalStock.add(ProductNhapKhoModel().apply {
                        productCode = it.code
                        amount = it.quantity
                    })
                }

                mNewList.forEach {
                    kiemKeRequestModel.newStock.add(ProductNhapKhoModel().apply {
                        productCode = it.code
                        amount = it.quantity
                    })
                }
                viewModel.kiemKeKho(kiemKeRequestModel)
            }
        }
    }

    override fun onItemSLChanged(position: Int, count: Int) {
        mNewList[position].quantity = count
    }
}