package vn.gas.thq.ui.nhapkho

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_init_import.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.model.ProductModel
import vn.gas.thq.model.UserModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.hongha.ga.R
import java.util.*

class NhapKhoFragment : BaseFragment(), ProductImportAdapter.ItemClickListener {
    private lateinit var viewModel: NhapKhoViewModel
    private var mListStaff = mutableListOf<UserModel>()
    private var requestNhapKho = RequestNhapKho()
    private var mList = mutableListOf<ProductModel>()
    private var staffCode: String? = null
    private var gasPrice: Int = 0
    private lateinit var productAdapter: ProductImportAdapter
    private var alertDialog: AlertDialog? = null

    companion object {
        @JvmStatic
        fun newInstance(): NhapKhoFragment {
            val args = Bundle()

            val fragment = NhapKhoFragment()
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
                .get(NhapKhoViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Nhập kho từ LXBH"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_init_import
    }

    override fun initObserver() {
        viewModel.mListStaffData.observe(viewLifecycleOwner, {
            mListStaff.clear()
            mListStaff.addAll(it)
        })

        viewModel.gasPriceData.observe(viewLifecycleOwner, {
            gasPrice = it
        })

        viewModel.mListDataProduct.observe(viewLifecycleOwner, {
            mList.clear()
            it.forEach {
                mList.add(
                    ProductModel(
                        it.productName,
                        it.productCode,
                        "",
                        "",
                        it.currentQuantity,
                        it.unit
                    )
                )
            }
            productAdapter.notifyDataSetChanged()
        })

        viewModel.callbackNhapKhoSuccess.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(activity, "Thông báo", "Nhập kho thành công") {
                alertDialog?.dismiss()
//                view?.let { it1 -> onSearchData(it1) }
                viewModel.getDataFromStaff(staffCode)
            }
        })

        //TODO: chung
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
        viewModel.getListStaff()
        initRecyclerView()
        edtLXBH.setOnClickListener(this::onChooseLXBH)
        btnSubmit.setOnClickListener(this::submitNhapKho)
    }

    private fun initRecyclerView() {
        productAdapter = ProductImportAdapter(mList)
        productAdapter.setClickListener(this)
        val linearLayoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
        rvRequestItem.layoutManager = linearLayoutManager
        rvRequestItem.adapter = productAdapter
    }

    private fun onChooseLXBH(view: View) {
        val doc = DialogList()
        var mArrayList = ArrayList<DialogListModel>()
        mListStaff.forEach {
            mArrayList.add(DialogListModel(it.staffCode, it.name, it.saleLineName))
        }
        doc.show(
            activity, mArrayList,
            getString(R.string.lxbh),
            getString(R.string.enter_text_search)
        ) { item ->
//            if (AppConstants.NOT_SELECT == item.id) {
//                return@show
//            }
            staffCode = item.id
            edtLXBH.setText(item.name)
            showInfo(item)
            viewModel.gasRemainPrice(staffCode!!)
        }
    }

    private fun showInfo(item: DialogListModel) {
        llInfo.visibility = View.VISIBLE
        tvName.text = item.name ?: "Không có thông tin"
        tvTuyen.text = item.other ?: "Không có thông tin"
        viewModel.getDataFromStaff(item.id)
    }

    private fun submitNhapKho(view: View) {
        mList.forEach {
            requestNhapKho.item.add(ProductNhapKhoModel().apply {
                productCode = it.code
                amount = it.quantity
            })
        }
        requestNhapKho.staffCode = staffCode
        requestNhapKho.gasPrice = gasPrice
        CommonUtils.showConfirmDiglog2Button(
            activity, "Xác nhận", "Bạn có chắc chắn muốn nhập kho?", getString(
                R.string.biometric_negative_button_text
            ), getString(R.string.text_ok)
        ) {
            if (it == AppConstants.YES) {
                viewModel.nhapKho(requestNhapKho)
            }
        }
    }

    override fun onItemSLChanged(position: Int, count: Int) {
        mList[position].quantity = count
    }

    override fun onItemSLChangedFloat(position: Int, count: Float) {
        Log.e("Float", "$position. $count. ${count.toInt()}")
        mList[position].quantity = count.toInt()
    }
}