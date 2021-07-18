package vn.gas.thq.ui.nhapkhonguon

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_init_kho_nguon.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.customview.CustomArrayAdapter
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.nhapvo.BienXeModel
import vn.gas.thq.ui.vitri.ShopModel
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.CallBackChange
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.NumberTextWatcher
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.hongha.ga.R
import java.util.*

class NhapKhoNguonFragment : BaseFragment() {
    private lateinit var viewModel: NhapKhoNguonViewModel
    private lateinit var suggestAdapter: CustomArrayAdapter
    private var alertDialog: AlertDialog? = null
    private var listShop = mutableListOf<ShopModel>()
    private var listBienXe = mutableListOf<BienXeModel>()

    private var shopID: Int = 0
    private var licensePlateID = 0
    private var khoiLuong = 0

    companion object {
        @JvmStatic
        fun newInstance() = NhapKhoNguonFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_init_kho_nguon
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
                .get(NhapKhoNguonViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Nhập kho nguồn"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun initData() {
        viewModel.getListShop("status==1")
        viewModel.getBienXe("status==1")

        val userDetail = AppPreferencesHelper(context).userModel
        edtTramOfStaff.setText(userDetail.shopName)

        edtKhoNguon.setOnClickListener(this::onChooseKhoNguon)
        edtBienXe.setOnClickListener(this::onChooseBienXe)
        suggestAdapter = CustomArrayAdapter(context, android.R.layout.simple_list_item_1)
        edtKhoiLuong.addTextChangedListener(
            NumberTextWatcher(
                edtKhoiLuong,
                suggestAdapter,
                object : CallBackChange {
                    override fun afterEditTextChange(it: Editable?) {
                        khoiLuong = getRealNumberV2(it)
                    }
                })
        )
        btnThemMoi.setOnClickListener(this::themMoiNhapGasNguon)
    }

    @SuppressLint("SetTextI18n")
    override fun initObserver() {
        viewModel.callbackListShop.observe(viewLifecycleOwner, {
            listShop.clear()
            listShop.addAll(it)

            edtKhoNguon.setText(listShop.firstOrNull { it2 -> it2.shopId == 76 }?.name)
            shopID = 76
        })

        viewModel.callbackListBienXe.observe(viewLifecycleOwner, {
            listBienXe.clear()
            listBienXe.addAll(it)
        })

        viewModel.callbackInitNhapGasNguonSuccess.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(
                activity,
                "Thông báo",
                "Bạn đã thực hiện nhập gas thành công"
            ) {

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

    private fun onChooseKhoNguon(view: View) {
        val doc = DialogList()
        val mArrayList = ArrayList<DialogListModel>()
        listShop.forEach {
            mArrayList.add(
                DialogListModel(
                    it.shopId.toString(),
                    it.name
                )
            )
        }
        doc.show(
            activity, mArrayList,
            "Kho nguồn",
            getString(R.string.enter_text_search)
        ) { item ->
//            if (AppConstants.NOT_SELECT == item.id) {
//                return@show
//            }
            shopID = item.id.toInt()
            edtKhoNguon.setText(item.name)
        }
    }

    private fun onChooseBienXe(view: View) {
        val doc = DialogList()
        val mArrayList = ArrayList<DialogListModel>()
        listBienXe.forEach {
            mArrayList.add(
                DialogListModel(
                    it.licensePlateId.toString(),
                    it.plateNo
                )
            )
        }
        doc.show(
            activity, mArrayList,
            "Biển số xe",
            getString(R.string.enter_text_search)
        ) { item ->
//            if (AppConstants.NOT_SELECT == item.id) {
//                return@show
//            }
            licensePlateID = item.id.toInt()
            edtBienXe.setText(item.name)
        }
    }

    private fun themMoiNhapGasNguon(view: View) {
//        if (TextUtils.isEmpty(edtUseKHL.text) || TextUtils.isEmpty(edtAvailableKHL.text)) return
//        if (edtUseKHL.text.toString().isEmpty() && edtUseGasDu.text.toString().isEmpty() && edtUseGasKiemKe.text.toString().isEmpty()) {
//            showMess("Chưa nhập thông tin gas hoặc khí sử dụng")
//            return
//        }
//
//        if (TextUtils.isEmpty(edtAmount12.text) && TextUtils.isEmpty(edtAmount45.text)) {
//            showMess("Bạn chưa nhập số lượng thành phẩm sau khi sang chiết được")
//            return
//        }
        if (edtKhoNguon.text.toString().isEmpty()) {
            showMess("Bạn chưa chọn kho nguồn")
            return
        }

        if (edtBienXe.text.toString().isEmpty()) {
            showMess("Bạn chưa chọn biển số xe")
            return
        }

        if (edtPhieuXuatKhoNguon.text.toString().isEmpty()) {
            showMess("Bạn chưa nhập Phiếu xuất kho nguồn")
            return
        }

        if (edtKhoiLuong.text.toString().isEmpty()) {
            showMess("Bạn chưa nhập Khối lượng")
            return
        }

        val obj = InitNhapGasNguon().apply {
            this.shopId = shopID
            this.licensePlateId = licensePlateID
            this.invoiceNumber = edtPhieuXuatKhoNguon.text.toString()
            this.amount = khoiLuong
        }

        CommonUtils.showConfirmDiglog2Button(
            activity,
            "Xác nhận",
            "Bạn có chắc chắn muốn nhập kho với khối lượng khí gas hóa lỏng $khoiLuong kg?",
            getString(
                R.string.biometric_negative_button_text
            ),
            getString(R.string.text_ok)
        ) {
            if (it == AppConstants.YES) {
                viewModel.initNhapGasNguon(obj)
//                viewModel.initSangChiet(obj)
//                viewModel.checkTransfer()
            }
        }
    }

    private fun getRealNumberV2(view: Editable?): Int {
        return if (TextUtils.isEmpty(
                view.toString().trim()
            )
        ) 0 else CommonUtils.getIntFromStringDecimal(
            view.toString()
                .trim()
        )
    }
}