package vn.gas.thq.ui.nhapvo

import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_nhap_tank.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.vitri.ShopModel
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.hongha.ga.R
import java.util.*
import kotlin.text.StringBuilder

class NhapVoFragment : BaseFragment() {
    lateinit var viewModel: NhapVoViewModel

    private lateinit var adapter: TankAdapter
    private var listShop = mutableListOf<ShopModel>()
    private var listBienXe = mutableListOf<BienXeModel>()
    private var listTank = mutableListOf<VoModel>()

    private var shopId: Int = 0
    private var licensePlateId = 0

    companion object {
        @JvmStatic
        fun newInstance() = NhapVoFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_nhap_tank
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
                .get(NhapVoViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Xuất nhập vỏ"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun initData() {
        initRecyclerView()
        viewModel.getListShop("status==1")
        viewModel.getBienXe("")
        viewModel.getVo()

        val user = AppPreferencesHelper(requireContext()).userModel
        tvDonViThucHien.text = user.shopName
        tvNguoiThucHien.text = user.name

        edtDonViNhan.setOnClickListener(this::onChooseDonViNhan)
        edtBienXe.setOnClickListener(this::onChooseBienXe)
        btnSearch.setOnClickListener(this::onSubmit)
    }

    override fun initObserver() {
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

        viewModel.callbackListShop.observe(viewLifecycleOwner, {
            listShop.clear()
            listShop.addAll(it)
        })

        viewModel.callbackListBienXe.observe(viewLifecycleOwner, {
            listBienXe.clear()
            listBienXe.addAll(it)
        })

        viewModel.callbackListTank.observe(viewLifecycleOwner, {
            listTank.clear()
            listTank.addAll(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.callbackNhapXuatVo.observe(viewLifecycleOwner, {
            CommonUtils.showDiglog1Button(
                activity,
                "Thông báo",
                "Thành công"
            ) {
                viewController?.popFragment()
            }
        })
    }

    private fun checkDonvinhan(): Boolean {
        return TextUtils.isEmpty(edtDonViNhan.text)
    }

    private fun checkXevanchuyen(): Boolean {
        return TextUtils.isEmpty(edtBienXe.text)
    }

    private fun validate(): Boolean {
        return listTank.firstOrNull { (it.slXuat != null && it.slXuat!! > 0) || (it.slNhap != null && it.slNhap!! > 0) } != null
    }

    private fun onSubmit(view: View) {
        if (checkDonvinhan()) {
            CommonUtils.showDiglog1Button(
                activity,
                "Thông báo",
                "Bạn chưa nhập Đơn vị nhận",
                null
            )
            return
        }
        if (checkXevanchuyen()) {
            CommonUtils.showDiglog1Button(
                activity,
                "Thông báo",
                "Bạn chưa nhập Xe vận chuyển",
                null
            )
            return
        }
        if (!validate()) {
            CommonUtils.showDiglog1Button(
                activity,
                "Thông báo",
                "Bạn chưa nhập số lượng vỏ cần xuất/nhập",
                null
            )
            return
        }
        val xuat = StringBuilder()
        val nhap = StringBuilder()
        val listTransfer = mutableListOf<VoModel>()
        listTank.forEach {
            if (it.slXuat != null && it.slXuat != 0) {
                listTransfer.add(VoModel().apply {
                    productOfferingId = it.productOfferingId
                    transferType = "1"
                    amount = it.slXuat
                })
                xuat.append(", ${it.name}: ${it.slXuat}")
            }
            if (it.slNhap != null && it.slNhap != 0) {
                listTransfer.add(VoModel().apply {
                    productOfferingId = it.productOfferingId
                    transferType = "2"
                    amount = it.slNhap
                })
                nhap.append(", ${it.name}: ${it.slNhap}")
            }
        }
        val mess =
            "Bạn có chắc chắn thực hiện giao dịch xuất, nhập cho biển số xe ${edtBienXe.text} với thông tin số lượng như dưới không?\n" +
                    "- Xuất: ${if (xuat.length > 2) xuat.substring(2, xuat.length) else "0"}\n" +
                    "- Nhập: ${if (nhap.length > 2) nhap.substring(2, nhap.length) else "0"}"
        // Vỏ HH 12kg: 10, Vỏ khác 45kg: 20
        CommonUtils.showConfirmDiglog2ButtonStartGravity(
            activity, "Xác nhận", mess, getString(
                R.string.biometric_negative_button_text
            ), getString(R.string.text_ok)
        ) {
            if (it == AppConstants.YES) {
                viewModel.xuatnhapVo(shopId, licensePlateId, listTransfer)
            }
        }
    }

    private fun initRecyclerView() {
        adapter = TankAdapter(listTank)
//        adapter.setClickListener(this)

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvTank.layoutManager = linearLayoutManager
        rvTank.adapter = adapter

        adapter.onItemXuatChange = { productId, sl ->
            listTank.firstOrNull { it.productOfferingId == productId }?.apply {
                slXuat = sl ?: 0
            }
        }
        adapter.onItemNhapChange = { productId, sl ->
            listTank.firstOrNull { it.productOfferingId == productId }?.slNhap = sl ?: 0
        }
    }

    private fun onChooseDonViNhan(view: View) {
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
            "Đơn vị nhận",
            getString(R.string.enter_text_search)
        ) { item ->
//            if (AppConstants.NOT_SELECT == item.id) {
//                return@show
//            }
            shopId = item.id.toInt()
            edtDonViNhan.setText(item.name)
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
            "Xe vận chuyển",
            getString(R.string.enter_text_search)
        ) { item ->
//            if (AppConstants.NOT_SELECT == item.id) {
//                return@show
//            }
            licensePlateId = item.id.toInt()
            edtBienXe.setText(item.name)
        }
    }
}