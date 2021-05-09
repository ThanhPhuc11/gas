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
import vn.gas.thq.model.ProductNhapKhoV2Model
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.nhapkho.ProductNhapKhoModel
import vn.gas.thq.ui.nhapkho.ProductNhapKhoV3Model
import vn.gas.thq.ui.xemkho.KhoModel
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.hongha.ga.R
import java.util.*

class KiemKeKhoFragment : BaseFragment(), KKKhoItemAdapter.ItemClickListener {
    private var shopCode: String? = null
    private lateinit var viewModel: KiemKeKhoViewModel
    private lateinit var productAdapter: KKKhoItemAdapter
    private var alertDialog: AlertDialog? = null
    private var mOldList = mutableListOf<ProductNhapKhoV2Model>()
    private var mNewList = mutableListOf<ProductNhapKhoV2Model>()
    private var listKho = mutableListOf<KhoModel>()
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
        viewModel.getListKho()
//        viewModel.getDataFromCode("NTL", null)
        tvCheckDate.text = "Ngày kiểm kê: ${AppDateUtils.getCurrentDate()}"
        initRecyclerView()
        edtTram.setOnClickListener(this::onChooseShop)
        btnKiemKe.setOnClickListener(this::onSubmit)
    }

    override fun initObserver() {
        viewModel.listKho.observe(viewLifecycleOwner, {
            listKho.clear()
            val listOnlyType1 = it.filter { it.type == 1 }
            listKho.addAll(listOnlyType1)
            if (listOnlyType1.isNotEmpty()) {
                shopCode = listOnlyType1[0].code
                edtTram.setText(listOnlyType1[0].code + " - " + listOnlyType1[0].name)
                viewModel.getDataFromCode(shopCode, null)
            }
        })

        viewModel.mLiveData.observe(viewLifecycleOwner, {
            mOldList.clear()
            mNewList.clear()
            it.forEach {
                mOldList.add(
                    ProductNhapKhoV2Model(
                        it.productName,
                        it.productCode,
                        "",
                        "",
                        it.currentQuantity,
                        it.unit
                    )
                )
                mNewList.add(
                    ProductNhapKhoV2Model(
                        it.productName,
                        it.productCode,
                        "",
                        "",
                        null,
                        it.unit
                    )
                )
            }
            productAdapter.notifyDataSetChanged()
        })

        viewModel.callbackKiemKeKho.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(activity, "Thông báo", "Hoàn thành") {
                alertDialog?.dismiss()
                viewModel.getDataFromCode(shopCode, null)
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
        if (mNewList.filter { it.quantity != 0f && it.quantity != null }.isEmpty()) {
            showMess("Bạn phải nhập ít nhất 1 số lượng kiểm kê")
            return
        }
        CommonUtils.showConfirmDiglog2Button(
            activity, "Xác nhận", "Bạn có chắc chắn muốn kiểm kê?", getString(
                R.string.biometric_negative_button_text
            ), getString(R.string.text_ok)
        ) {
            if (it == AppConstants.YES) {
                kiemKeRequestModel = KiemKeRequestModel()
                kiemKeRequestModel.shopCode = shopCode
                mOldList.forEach {
                    kiemKeRequestModel.originalStock.add(ProductNhapKhoV3Model().apply {
                        productCode = it.code
                        amount = it.quantity
                    })
                }

                mNewList.filter { it1 -> it1.quantity != null }.forEach {
                    kiemKeRequestModel.newStock.add(ProductNhapKhoV3Model().apply {
                        productCode = it.code
                        amount = it.quantity
                    })
                }
                viewModel.kiemKeKho(kiemKeRequestModel)
            }
        }
    }

    private fun onChooseShop(view: View) {
        var doc = DialogList()
        var mArrayList = ArrayList<DialogListModel>()
        listKho.forEach {
            mArrayList.add(DialogListModel(it.code, "${it.code} - ${it.name}", it.type.toString()))
        }

        doc.show(
            activity, mArrayList,
            "Trạm",
            getString(R.string.enter_text_search)
        ) { item ->
            shopCode = item.id
//            staffCode = null
//            if (item.other == "1") {
//                shopCode = item.id
//            } else {
//                staffCode = item.id
//            }
            edtTram.setText(item.name)
        }
    }

    override fun onItemSLChanged(position: Int, count: Int?) {
        mNewList[position].quantity = count?.toFloat()
    }
}