package vn.gas.thq.ui.trano.nhaptrano

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_init_tra_no.*
import kotlinx.android.synthetic.main.fragment_retail.*
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.customview.CustomArrayAdapter
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.retail.Customer
import vn.gas.thq.util.*
import vn.gas.thq.util.dialog.DialogList
import vn.gas.thq.util.dialog.DialogListModel
import vn.gas.thq.util.dialog.GetListDataDemo
import vn.hongha.ga.R
import java.util.*

class NhapTraNoFragment : BaseFragment() {
    private lateinit var viewModel: NhapTraNoViewModel
    private lateinit var suggestAdapter: CustomArrayAdapter
    private var alertDialog: AlertDialog? = null
    private var mListCustomer = mutableListOf<Customer>()

    companion object {
        @JvmStatic
        fun newInstance() = NhapTraNoFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_init_tra_no
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
                .get(NhapTraNoViewModel::class.java)
    }

    override fun initView() {

    }

    override fun initData() {
        val user = AppPreferencesHelper(context).userModel
        val queryKH = "shopId==${user.shopId};status==1"
        viewModel.onGetListCustomer(queryKH, 0)
        edtKH.setOnClickListener(this::onChooseCustomer)

        edtLoaiCongNo.setOnClickListener(this::onChooseLoaiCongNo)
        btnThemMoi.setOnClickListener(this::themMoiSangChiet)
        suggestAdapter = CustomArrayAdapter(context, android.R.layout.simple_list_item_1)
//        edtUseKHL.addTextChangedListener(
//            NumberTextWatcher(
//                edtUseKHL,
//                suggestAdapter,
//                object : CallBackChange {
//                    override fun afterEditTextChange(it: Editable?) {
////                        useKHL = getRealNumberV2(it)
//                    }
//                })
//        )
    }

    @SuppressLint("SetTextI18n")
    override fun initObserver() {
        viewModel.callbackListKH.observe(viewLifecycleOwner, {
            mListCustomer.clear()
            mListCustomer.addAll(it)
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

    private fun onChooseCustomer(view: View) {
        val doc = DialogList()
        val mArrayList = ArrayList<DialogListModel>()
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
//            status = item.id
            edtKH.setText(item.name)
        }
    }

    private fun onChooseLoaiCongNo(view: View) {
        val doc = DialogList()
        val mArrayList = GetListDataDemo.getListLoaiCongNo()
        doc.show(
            activity, mArrayList,
            "Loại công nợ",
            getString(R.string.enter_text_search)
        ) { item ->
            if (AppConstants.NOT_SELECT == item.id) {
                return@show
            }
//            status = item.id
            edtLoaiCongNo.setText(item.name)
            showInputCongNoTien(item.id == "3" || item.id == "4")
        }
    }

    private fun showInputCongNoTien(isCongNoTien: Boolean) {
        if (isCongNoTien) {
            llWrapCongNoTien.visibility = View.VISIBLE
            lblKHTra.visibility = View.GONE
            edtKHTra.visibility = View.GONE
        } else {
            llWrapCongNoTien.visibility = View.GONE
            lblKHTra.visibility = View.VISIBLE
            edtKHTra.visibility = View.VISIBLE
        }
    }

    private fun themMoiSangChiet(view: View) {

//        if (TextUtils.isEmpty(edtAmount12.text) && TextUtils.isEmpty(edtAmount45.text)) {
//            showMess("Bạn chưa nhập số lượng thành phẩm sau khi sang chiết được")
//            return
//        }

        CommonUtils.showConfirmDiglog2Button(
            activity, "Xác nhận", "Bạn có chắc chắn muốn nhập thông tin sang chiết?", getString(
                R.string.biometric_negative_button_text
            ), getString(R.string.text_ok)
        ) {
            if (it == AppConstants.YES) {
//                viewModel.initSangChiet(obj)
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