package vn.gas.thq.ui.qlyeucauduyetkehoach.chitiet

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_detail_ke_hoach.*
import kotlinx.android.synthetic.main.fragment_detail_ke_hoach.rvKeHoach
import kotlinx.android.synthetic.main.fragment_detail_ke_hoach.tvPlanTime
import kotlinx.android.synthetic.main.fragment_detail_ke_hoach.tvStaff
import kotlinx.android.synthetic.main.fragment_detail_ke_hoach.tvTram
import kotlinx.android.synthetic.main.fragment_detail_ke_hoach.tvTuyenBH
import kotlinx.android.synthetic.main.fragment_init_ke_hoach.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.greenrobot.eventbus.EventBus
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.customview.CustomArrayAdapter
import vn.gas.thq.event.UpdateEvent
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.kehoachbh.DSKeHoachAdapter
import vn.gas.thq.ui.kehoachbh.KeHoachModel
import vn.gas.thq.ui.pheduyetgia.DuyetGiaModel
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.AppDateUtils
import vn.gas.thq.util.CommonUtils
import vn.hongha.ga.R

class DetailKeHoachFragment : BaseFragment() {
    private lateinit var viewModel: DetailKeHoachViewModel
    private lateinit var adapter: DSDetailKeHoachAdapter
    private var listKHBH = mutableListOf<KeHoachModel>()
    private var alertDialog: AlertDialog? = null
    private var id = ""
    private var option = 0

    companion object {
        private const val ID = "ID"
        private const val OPTION = "OPTION"

        @JvmStatic
        fun newInstance(id: String, option: Int) = DetailKeHoachFragment().apply {
            arguments = Bundle().apply {
                putString(ID, id)
                putInt(OPTION, option)
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_detail_ke_hoach
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
                .get(DetailKeHoachViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Quản lý yêu cầu KH bán hàng"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun initData() {
        id = arguments?.getString(ID).toString()
        option = arguments?.getInt(OPTION) ?: 0
        viewModel.getKeHoachBH(id)
        initRecyclerView()
        if (option == AppConstants.LEVEL_DUYET_GIA) {
            btnDuyet.text = "Duyệt giá"
        } else if (option == AppConstants.LEVEL_DUYET_SO_LUONG) {
            btnDuyet.text = "Duyệt số lượng"
        } else {
            btnDuyet.visibility = View.GONE
            btnTuChoi.visibility = View.GONE
        }
        btnDuyet.setOnClickListener(this::duyet)
        btnTuChoi.setOnClickListener(this::showDiaLogReason)
    }

    override fun initObserver() {
        viewModel.callbackDetailKHBH.observe(viewLifecycleOwner, {
            setViewData(it)
        })

        viewModel.callbackDuyetSuccessKHBH.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(activity, "Thông báo", "Phê duyệt yêu cầu thành công") {
                alertDialog?.dismiss()
                EventBus.getDefault().post(UpdateEvent())
                viewController?.popFragment()
            }
        })

        viewModel.callbackTuChoiSuccessKHBH.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(activity, "Thông báo", "Từ chối yêu cầu thành công") {
                alertDialog?.dismiss()
                EventBus.getDefault().post(UpdateEvent())
                viewController?.popFragment()
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

    private fun duyet(view: View) {
        CommonUtils.showConfirmDiglog2Button(
            activity, "Xác nhận", "Bạn có chắc chắn muốn phê duyệt yêu cầu?", getString(
                R.string.biometric_negative_button_text
            ), getString(R.string.text_ok)
        ) {
            if (it == AppConstants.YES) {
                if (option == AppConstants.LEVEL_DUYET_GIA) {
                    viewModel.duyetKeHoachBH(id, DetailTypeKHBHModel().apply { approveType = "2" })
                } else if (option == AppConstants.LEVEL_DUYET_SO_LUONG) {
                    viewModel.duyetKeHoachBH(id, DetailTypeKHBHModel().apply { approveType = "1" })
                }
            }
        }
    }

    private fun tuChoi(strReason: String?) {
        CommonUtils.showConfirmDiglog2Button(
            activity, "Xác nhận", "Bạn có chắc chắn muốn từ chối yêu cầu?", getString(
                R.string.biometric_negative_button_text
            ), getString(R.string.text_ok)
        ) {
            if (it == AppConstants.YES) {
                if (option == AppConstants.LEVEL_DUYET_GIA) {
                    viewModel.tuChoiKeHoachBH(id, DetailTypeKHBHModel().apply {
                        approveType = "2"
                        reason = strReason
                    })
                } else if (option == AppConstants.LEVEL_DUYET_SO_LUONG) {
                    viewModel.tuChoiKeHoachBH(id, DetailTypeKHBHModel().apply {
                        approveType = "1"
                        reason = strReason
                    })
                }
            }
        }
    }

    private fun showDiaLogReason(view: View) {
        val builder = context?.let { AlertDialog.Builder(it, R.style.AlertDialogNoBG) }
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.layout_dialog_reject_reason, null)
        builder?.setView(dialogView)

        val imgClose: ImageView = dialogView.findViewById(R.id.imgClose)
        val edtReason: EditText = dialogView.findViewById(R.id.edtReason)
        val btnHuy: Button = dialogView.findViewById(R.id.btnHuy)
        val btnDongY: Button = dialogView.findViewById(R.id.btnDongY)

        imgClose.setOnClickListener {
            alertDialog?.dismiss()
        }

        btnHuy.setOnClickListener {
            alertDialog?.dismiss()
        }

        btnDongY.setOnClickListener {
            if (TextUtils.isEmpty(edtReason.text.toString())) {
                CommonUtils.showDiglog1Button(
                    activity,
                    "Thông báo",
                    "Vui lòng nhập lý do từ chối"
                ) {

                }
            } else {
                tuChoi(edtReason.text.toString())
                alertDialog?.dismiss()
            }
        }

        alertDialog = builder?.create()
        alertDialog?.window?.setLayout(500, 200)
        alertDialog?.show()
    }


    private fun initRecyclerView() {
        adapter = DSDetailKeHoachAdapter(listKHBH)
//        adapter.setClickListener(this)
        val linearLayoutManager = LinearLayoutManager(context, GridLayoutManager.VERTICAL, false)
        rvKeHoach.layoutManager = linearLayoutManager
        rvKeHoach.adapter = adapter
    }

    private fun setViewData(obj: DetailKHBHModel) {
        tvTram.text = obj.shopName
        tvTuyenBH.text = obj.saleLineName
        tvStaff.text = obj.staffName
        tvPlanTime.text = AppDateUtils.changeDateFormat(
            AppDateUtils.FORMAT_6,
            AppDateUtils.FORMAT_1,
            obj.effectDate
        )

        listKHBH.clear()
        obj.detail?.let { listKHBH.addAll(it) }
        adapter.notifyDataSetChanged()
    }
}