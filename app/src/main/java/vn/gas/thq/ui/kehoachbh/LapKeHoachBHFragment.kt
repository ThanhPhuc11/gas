package vn.gas.thq.ui.kehoachbh

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_init_ke_hoach.*
import kotlinx.android.synthetic.main.fragment_init_ke_hoach.btnSubmit
import kotlinx.android.synthetic.main.fragment_retail.*
import kotlinx.android.synthetic.main.layout_dialog_confirm_plan.view.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.nhapkho.ProductNhapKhoModel
import vn.gas.thq.ui.retail.Customer
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.hongha.ga.R
import java.util.*

class LapKeHoachBHFragment : BaseFragment(), DSKeHoachAdapter.ItemClickListener {
    private lateinit var adapter: DSKeHoachAdapter
    private var listKHBH = mutableListOf<KeHoachModel>()
    private var alertDialog: AlertDialog? = null
    private lateinit var viewModel: LapKeHoachBHViewModel
    private var mListCustomer = mutableListOf<Customer>()

    companion object {
        @JvmStatic
        fun newInstance() = LapKeHoachBHFragment().apply {
            arguments = Bundle()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_init_ke_hoach
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
                .get(LapKeHoachBHViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Kế hoạch bán hàng"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun initData() {
        val user = AppPreferencesHelper(context).userModel
        tvStaff.text = user.name
        tvPlanTime.text = AppDateUtils.getCurrentDate()
        viewModel.onGetListCustomer("21", "105")
        initRecyclerView()
        addKeHoach(view)
        tvAddKeHoach.setOnClickListener(this::addKeHoach)
        btnSubmit.setOnClickListener(this::submitData)
    }

    private fun submitData(view: View) {
        showDiglogConfirmPlan()
    }

    private fun addKeHoach(view: View?) {
        var keHoachModel = KeHoachModel()
        keHoachModel.item.add(ProductNhapKhoModel().apply {
            productCode = "GAS12"
            amount = 0
            price = 0
        })
        keHoachModel.item.add(ProductNhapKhoModel().apply {
            productCode = "GAS45"
            amount = 0
            price = 0
        })
        keHoachModel.item.add(ProductNhapKhoModel().apply {
            productCode = "TANK12"
            amount = 0
            price = 0
        })
        keHoachModel.item.add(ProductNhapKhoModel().apply {
            productCode = "TANK45"
            amount = 0
            price = 0
        })
        listKHBH.add(keHoachModel)
        adapter.notifyDataSetChanged()
        rvKeHoach.scrollToPosition(listKHBH.size - 1)
    }

    private fun initRecyclerView() {
        adapter = DSKeHoachAdapter(listKHBH)
        adapter.setClickListener(this)
        val linearLayoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
        rvKeHoach.layoutManager = linearLayoutManager
        rvKeHoach.adapter = adapter
    }

    override fun initObserver() {
        viewModel.mLiveDataCustomer.observe(viewLifecycleOwner, {
            mListCustomer.addAll(it)
        })

        viewModel.callbackKHBH.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(activity, "Thông báo", "Hoàn thành") {
                alertDialog?.dismiss()
                listKHBH.clear()
                adapter.notifyDataSetChanged()
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

    private fun showDiglogConfirmPlan() {
        val builder = context?.let { AlertDialog.Builder(it, R.style.AlertDialogNoBG) }
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.layout_dialog_confirm_plan, null)
        builder?.setView(dialogView)
        var tvGas12 = dialogView.findViewById<TextView>(R.id.tvGas12)
        var tvGas45 = dialogView.findViewById<TextView>(R.id.tvGas45)
        var tvTank12 = dialogView.findViewById<TextView>(R.id.tvTank12)
        var tvTank45 = dialogView.findViewById<TextView>(R.id.tvTank45)

        var gas12 = 0
        var gas45 = 0
        var tank12 = 0
        var tank45 = 0
        dialogView.apply {
            imgClose.setOnClickListener {
                alertDialog?.dismiss()
            }


//            btnHuyYC.setOnClickListener {
//                CommonUtils.showConfirmDiglog2Button(
//                    activity, "Xác nhận", "Bạn có chắc chắn muốn huỷ yêu cầu?", getString(
//                        R.string.biometric_negative_button_text
//                    ), getString(R.string.text_ok)
//                ) {
//                    if (it == AppConstants.YES) {
//                        viewModel.onCancelRequest(orderId)
//                    }
//                }
//            }
            btnDongY.setOnClickListener {
//                viewModelThuKho.acceptOrNotRequest(orderId, true)
                viewModel.lapKeHoachBH(RequestKeHoachModel().apply { detail = listKHBH })
            }
        }
        for (item: KeHoachModel in listKHBH) {
            gas12 += item.item[0].amount!!
            gas45 += item.item[1].amount!!
            tank12 += item.item[2].amount!!
            tank45 += item.item[3].amount!!
        }
        tvGas12.text = gas12.toString()
        tvGas45.text = gas45.toString()
        tvTank12.text = tank12.toString()
        tvTank45.text = tank45.toString()

        alertDialog = builder?.create()
        alertDialog?.window?.setLayout(500, 200)
        alertDialog?.show()
    }

    override fun onItemClick(position: Int) {
        chooseCustomer(position)
    }

    override fun onItemChange(position: Int, count: Int) {
        listKHBH[position].daysPerVisit = count
    }

    override fun onItemCodeQuantity(position: Int, code: String, quantity: Int) {
        listKHBH[position].item.first { it.productCode == code }.amount = quantity
    }

    override fun onItemCodePrice(position: Int, code: String, price: Int) {
        listKHBH[position].item.first { it.productCode == code }.price = price
    }

    private fun chooseCustomer(position: Int) {
        var doc = DialogList()
        var mArrayList = ArrayList<DialogListModel>()
        mListCustomer.forEach {
            mArrayList.add(DialogListModel(it.customerId ?: "", it.name ?: ""))
        }
        doc.show(
            activity, mArrayList,
            getString(R.string.customer),
            getString(R.string.enter_text_search)
        ) { item ->
            if (AppConstants.NOT_SELECT == item.id) {
                return@show
            }
            listKHBH[position].customerId = item.id.toInt()
            listKHBH[position].custName = item.name
            adapter.notifyItemChanged(position)
        }
    }
}