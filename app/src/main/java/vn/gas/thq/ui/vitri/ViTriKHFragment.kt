package vn.gas.thq.ui.vitri

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
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
import kotlinx.android.synthetic.main.fragment_cap_nhat_vi_tri.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.retail.Customer
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.hongha.ga.R
import java.util.*

class ViTriKHFragment : BaseFragment(), CustomerAdapter.ItemClickListener {
    private lateinit var viewModel: ViTriKHViewModel
    private lateinit var adapter: CustomerAdapter
    private var listTram = mutableListOf<ShopModel>()
    private var listSaleLine = mutableListOf<SaleLineModel>()
    private var mListCustomer = mutableListOf<Customer>()
    private var alertDialog: AlertDialog? = null
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    private var shopId: String? = null
    private var saleLineId: String? = null

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
        viewModel.getSaleLine()
        initRecyclerView()
        edtTram.setOnClickListener(this::onChooseShop)
        edtTuyenXe.setOnClickListener(this::onChooseTuyen)
        btnSearch.setOnClickListener(this::onSearch)
    }

    override fun initObserver() {
        viewModel.callbackListShop.observe(viewLifecycleOwner, {
            listTram.clear()
            listTram.addAll(it)
        })

        viewModel.callbackListSaleLine.observe(viewLifecycleOwner, {
            listSaleLine.clear()
            listSaleLine.addAll(it)
        })

        viewModel.mLiveDataCustomer.observe(viewLifecycleOwner, {
            mListCustomer.addAll(it)
            adapter.notifyDataSetChanged()
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

        val linearLayoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
        rvCust.layoutManager = linearLayoutManager
        rvCust.adapter = adapter
    }

    private fun onSearch(view: View) {
        viewModel.onGetListCustomer("0", "0")
    }

    private fun onChooseShop(view: View) {
        var doc = DialogList()
        var mArrayList = ArrayList<DialogListModel>()
        listTram.forEach {
            mArrayList.add(DialogListModel(it.shopId.toString(), it.name))
        }

        doc.show(
            activity, mArrayList,
            "Trạm",
            getString(R.string.enter_text_search)
        ) { item ->
            shopId = item.id
            edtTram.setText(item.name)
        }
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
            saleLineId = item.id
            edtTuyenXe.setText(item.name)
        }
    }

    private fun showDialogDetail(custId: String) {
        val builder = context?.let { AlertDialog.Builder(it, R.style.AlertDialogNoBG) }
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.layout_dialog_vi_tri, null)
        builder?.setView(dialogView)

        val imgClose = dialogView.findViewById<ImageView>(R.id.imgClose)
        val btnHuy = dialogView.findViewById<Button>(R.id.btnHuy)
        val btnCapNhat = dialogView.findViewById<Button>(R.id.btnCapNhat)

        val tvAddress = dialogView.findViewById<TextView>(R.id.tvAddress)

        tvAddress.text = "${String.format("%.2f", latitude)} : ${String.format("%.2f", longitude)}"

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
                    viewModel.capNhatToaDoKH(custId, ToaDoModel().apply {
                        lat = latitude.toInt()
                        lng = longitude.toInt()
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
        showMess("${mListCustomer[position].customerId}")
        showDialogDetail("${mListCustomer[position].customerId}")
    }
}