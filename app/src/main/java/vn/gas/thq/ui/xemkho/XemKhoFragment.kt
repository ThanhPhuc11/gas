package vn.gas.thq.ui.xemkho

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_xem_kho.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.model.ProductModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.hongha.ga.R

class XemKhoFragment : BaseFragment() {
    private lateinit var viewModel: XemKhoViewModel
    private lateinit var productAdapter: KhoItemAdapter
    private var alertDialog: AlertDialog? = null
    var mList = mutableListOf<ProductModel>()

    companion object {
        @JvmStatic
        fun newInstance(): XemKhoFragment {
            val args = Bundle()

            val fragment = XemKhoFragment()
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
                .get(XemKhoViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Xem kho"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_xem_kho
    }

    override fun initObserver() {
        viewModel.mLiveData.observe(viewLifecycleOwner, {
            mList.clear()
            it.forEach {
                mList.add(ProductModel(it.productName, it.productCode, "", "", it.currentQuantity, it.unit))
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
        initRecyclerView()
        btnSearch.setOnClickListener(this::onSearch)
    }

    private fun initRecyclerView() {
        productAdapter = context?.let { KhoItemAdapter(mList, it) }!!

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

    private fun onSearch(view: View) {
        viewModel.getDataFromShop()
    }
}