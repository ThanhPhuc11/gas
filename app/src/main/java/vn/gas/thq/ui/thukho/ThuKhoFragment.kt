package vn.gas.thq.ui.thukho

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.fragment_thu_kho_qlyc_xuat_kho.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.util.CommonUtils
import vn.hongha.ga.R

class ThuKhoFragment : BaseFragment() {
    var alertDialog: AlertDialog? = null
    companion object {
        @JvmStatic
        fun newInstance(): ThuKhoFragment {
            val args = Bundle()

            val fragment = ThuKhoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setViewController() {
        viewController = (activity as MainActivity).viewController
    }

    override fun setupViewModel() {

    }

    override fun initView() {
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
        edtStartDate.setOnClickListener {
            CommonUtils.showCalendarDialog(
                context,
                edtStartDate.text.toString()
            ) { strDate -> edtStartDate.setText(strDate) }
        }

        edtEndDate.setOnClickListener {
            CommonUtils.showCalendarDialog(
                context,
                edtEndDate.text.toString()
            ) { strDate -> edtEndDate.setText(strDate) }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_thu_kho_qlyc_xuat_kho
    }

    override fun initObserver() {

    }

    override fun initData() {
        btnItem1.setOnClickListener {
            showDiglog1Button()
        }
        btnItem2.setOnClickListener {
            showDiglog1Button()
        }
        btnItem3.setOnClickListener {
            showDiglog1Button()
        }
    }

    private fun showDiglog1Button(
//        title: String,
//        message: String,
//        callback: ConfirmDialogCallback?
    ) {
        val builder = context?.let { AlertDialog.Builder(it, R.style.AlertDialogNoBG) }
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.layout_dialog_item_thu_kho, null)
        builder?.setView(dialogView)
//        val tv_header_dialog = dialogView.findViewById<TextView>(R.id.tv_header_dialog)
//        val tv_content_dialog = dialogView.findViewById<TextView>(R.id.tv_content_dialog)
//        tv_header_dialog.text = "" + title
//        tv_content_dialog.text = "" + message
        val imgClose: ImageView = dialogView.findViewById(R.id.imgClose)
        alertDialog = builder?.create()
        alertDialog?.window?.setLayout(500, 200)
        alertDialog?.show()
        imgClose.setOnClickListener {
            alertDialog?.dismiss()
        }
    }
}