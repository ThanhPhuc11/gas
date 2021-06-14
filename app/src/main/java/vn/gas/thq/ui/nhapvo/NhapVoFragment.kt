package vn.gas.thq.ui.nhapvo

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_nhap_tank.*
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.vitri.ShopModel
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.hongha.ga.R
import java.util.*

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
            showMess("Success")
        })
    }

    private fun onSubmit(view: View) {
        val listTransfer = mutableListOf<VoModel>()
        listTank.forEach {
            if (it.slXuat != null && it.slXuat != 0)
                listTransfer.add(VoModel().apply {
                    productOfferingId = it.productOfferingId
                    transferType = "1"
                    amount = it.slXuat
                })
            if (it.slNhap != null && it.slNhap != 0)
                listTransfer.add(VoModel().apply {
                    productOfferingId = it.productOfferingId
                    transferType = "2"
                    amount = it.slNhap
                })
        }
        viewModel.xuatnhapVo(shopId, licensePlateId, listTransfer)
    }

    private fun initRecyclerView() {
        adapter = TankAdapter(listTank)
//        adapter.setClickListener(this)

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvTank.layoutManager = linearLayoutManager
        rvTank.adapter = adapter

        adapter.onItemXuatChange = { productId, sl ->
            listTank.firstOrNull { it.productOfferingId == productId }?.apply {
                slXuat = sl
            }
        }
        adapter.onItemNhapChange = { productId, sl ->
            listTank.firstOrNull { it.productOfferingId == productId }?.slNhap = sl
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
            getString(R.string.lxbh),
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
            getString(R.string.lxbh),
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