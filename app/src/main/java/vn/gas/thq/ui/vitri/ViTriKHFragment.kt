package vn.gas.thq.ui.vitri

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_cap_nhat_vi_tri.*
import kotlinx.android.synthetic.main.fragment_cap_nhat_vi_tri.btnSearch
import kotlinx.android.synthetic.main.fragment_qlyc_ca_nhan.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.retail.Customer
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.EndlessPageRecyclerViewScrollListener
import vn.gas.thq.util.EndlessRecyclerViewScrollListener
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.hongha.ga.R
import java.util.*

class ViTriKHFragment : BaseFragment(), CustomerAdapter.ItemClickListener {
    private lateinit var viewModel: ViTriKHViewModel
    private lateinit var adapter: CustomerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var listTram = mutableListOf<ShopModel>()
    private var listSaleLine = mutableListOf<SaleLineModel>()
    private var mListCustomer = mutableListOf<Customer>()
    private var alertDialog: AlertDialog? = null
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    private var shopId: String? = null
    private var saleLineId: String? = null
    private var isReload: Boolean = false

    private var queryKH = ""

    private var PERMISSION_ALL = 1
    private var PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    companion object {
        @JvmStatic
        fun newInstance() = ViTriKHFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_cap_nhat_vi_tri
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
                .get(ViTriKHViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Cập nhật vị trí khách hàng"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun initData() {
        viewModel.getAllShop()
//        viewModel.getSaleLine()
        initRecyclerView()
        edtTram.setOnClickListener(this::onChooseShop)
        edtTuyenXe.setOnClickListener(this::onChooseTuyen)
        btnSearch.setOnClickListener(this::onSearch)
    }

    override fun initObserver() {
        viewModel.callbackListShop.observe(viewLifecycleOwner, {
            listTram.clear()
            listTram.addAll(it)
            if (listTram.size == 1) {
                shopId = listTram[0].shopId.toString()
                edtTram.setText(listTram[0].name)
                viewModel.getSaleLine(shopId!!)
            }
        })

        viewModel.callbackListSaleLine.observe(viewLifecycleOwner, {
            listSaleLine.clear()
            listSaleLine.addAll(it)
        })

        viewModel.mLiveDataCustomer.observe(viewLifecycleOwner, {
            if (isReload) {
                mListCustomer.clear()
            }
            mListCustomer.addAll(it)
            adapter.notifyDataSetChanged()
            isReload = false
        })

        viewModel.callbackDetailKH.observe(viewLifecycleOwner, {
            showDialogDetail(it)
        })

        viewModel.callbackUpdateSuccess.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(activity, "Thông báo", "Cập nhật toạ độ KH thành công") {
                alertDialog?.dismiss()
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
        adapter = CustomerAdapter(mListCustomer)
        adapter.setClickListener(this)

        linearLayoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
        rvCust.layoutManager = linearLayoutManager
        rvCust.adapter = adapter
    }

    private fun onSearch(view: View) {
        setEndLessScrollListener()
        isReload = true
        queryKH = ""
        if (!TextUtils.isEmpty(edtMaKH.text.toString())) {
            queryKH += ";custId==${edtMaKH.text.toString()}"
        }
        if (!TextUtils.isEmpty(edtTenKH.text.toString())) {
            queryKH += ";name=ik='${edtTenKH.text.toString()}'"
        }
        if (shopId != null) {
            queryKH += ";shopId==$shopId"
        }
        if (saleLineId != null) {
            queryKH += ";saleLineId==$saleLineId"
        }
        if (queryKH == "") {
            viewModel.onGetListCustomer(queryKH, 0)
            return
        }
        viewModel.onGetListCustomer(queryKH.substring(1, queryKH.length), 0)
    }

    private fun setEndLessScrollListener() {
        rvCust.clearOnScrollListeners()
        rvCust.addOnScrollListener(object :
            EndlessPageRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if (queryKH == "") {
                    viewModel.onGetListCustomer(queryKH, page)
                    return
                }
                queryKH = queryKH.substring(1, queryKH.length)
                viewModel.onGetListCustomer(queryKH, page)
            }
        })
    }

    private fun onChooseShop(view: View) {
        val doc = DialogList()
        val mArrayList = ArrayList<DialogListModel>()
        listTram.forEach {
            mArrayList.add(DialogListModel(it.shopId.toString(), it.name))
        }

        doc.show(
            activity, mArrayList,
            "Trạm",
            getString(R.string.enter_text_search)
        ) { item ->
            if (AppConstants.NOT_SELECT == item.id) {
                shopId = null
                edtTram.setText("")
                return@show
            }
            shopId = item.id
            edtTram.setText(item.name)
        }
//        doc.displayNotSelect()
    }

    private fun onChooseTuyen(view: View) {
        val doc = DialogList()
        val mArrayList = ArrayList<DialogListModel>()
        listSaleLine.forEach {
            mArrayList.add(DialogListModel(it.saleLineId.toString(), it.name))
        }

        doc.show(
            activity, mArrayList,
            "Tuyến xe",
            getString(R.string.enter_text_search)
        ) { item ->
            if (AppConstants.NOT_SELECT == item.id) {
                saleLineId = null
                edtTuyenXe.setText("")
                return@show
            }
            saleLineId = item.id
            edtTuyenXe.setText(item.name)
        }
        doc.displayNotSelect()
    }

    private fun showDialogDetail(customer: Customer) {
        val builder = context?.let { AlertDialog.Builder(it, R.style.AlertDialogNoBG) }
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.layout_dialog_vi_tri, null)
        builder?.setView(dialogView)

        val imgClose = dialogView.findViewById<ImageView>(R.id.imgClose)
        val btnHuy = dialogView.findViewById<Button>(R.id.btnHuy)
        val btnCapNhat = dialogView.findViewById<Button>(R.id.btnCapNhat)

        val tvCustId = dialogView.findViewById<TextView>(R.id.tvCustId)
        val tvCustName = dialogView.findViewById<TextView>(R.id.tvCustName)
        val tvAddress = dialogView.findViewById<TextView>(R.id.tvAddress)
        val tvVungThiTruong = dialogView.findViewById<TextView>(R.id.tvVungThiTruong)
        val tvTuyenBH = dialogView.findViewById<TextView>(R.id.tvTuyenBH)
        val tvNVKD = dialogView.findViewById<TextView>(R.id.tvNVKD)

        tvCustId.text = customer.customerId
        tvCustName.text = customer.name
        tvTuyenBH.text = customer.saleLineName
        tvNVKD.text = customer.staffName
        tvAddress.text = customer.address
        tvVungThiTruong.text = customer.shopArea
//        tvAddress.text = "${String.format("%.2f", latitude)} : ${String.format("%.2f", longitude)}"

        imgClose.setOnClickListener {
            alertDialog?.dismiss()
        }

        btnHuy.setOnClickListener {
            alertDialog?.dismiss()
        }
        btnCapNhat.setOnClickListener {
            CommonUtils.showConfirmDiglog2Button(
                activity, "Xác nhận", "Bạn có chắc chắn muốn cập nhật?", getString(
                    R.string.biometric_negative_button_text
                ), getString(R.string.text_ok)
            ) {
                if (it == AppConstants.YES) {
                    viewModel.capNhatToaDoKH(customer.customerId, ToaDoModel().apply {
                        lat = latitude.toFloat()
                        lng = longitude.toFloat()
                    })
//                        viewModel.onCancelRequest(orderId)
                }
            }
        }

        alertDialog = builder?.create()
        alertDialog?.window?.setLayout(500, 200)
        alertDialog?.show()
    }

    private fun hasPermissions(context: Context?, vararg permissions: String?): Boolean {
        if (context != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    private fun check() {
        val location = getLastKnownLocation()
        longitude = location?.longitude ?: 0.0
        latitude = location?.latitude ?: 0.0
        Log.e("PHUC", "$longitude : $latitude")
    }

    private fun getLastKnownLocation(): Location? {
        val mLocationManager: LocationManager = context?.getSystemService(
            AppCompatActivity.LOCATION_SERVICE
        ) as LocationManager
        val providers: List<String> = mLocationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            if (ActivityCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                break
            }
            val l: Location = mLocationManager.getLastKnownLocation(provider)
                ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                // Found best last known location: %s", l);
                bestLocation = l
            }
        }
        return bestLocation
    }

    override fun onItemClick(view: View?, position: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(context, *PERMISSIONS)) {
                requestPermissions(PERMISSIONS, PERMISSION_ALL)
                return
            }
            check()
        }
//        showMess("${mListCustomer[position].customerId}")
        viewModel.onGetDetailCustomer(mListCustomer[position].customerId.toString())
//        showDialogDetail("${mListCustomer[position].customerId}")
    }
}