package vn.gas.thq.ui.qlyeucauduyetgia

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.fragment_approval_requests.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.hongha.ga.R

class QuanLyYeuCauDuyetGiaFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance(): QuanLyYeuCauDuyetGiaFragment {
            val args = Bundle()

            val fragment = QuanLyYeuCauDuyetGiaFragment()
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
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_approval_requests
    }

    override fun initObserver() {
    }

    override fun initData() {
        tvTitle.text = "Quản lý yêu cầu duyệt giá"
        wrapClick1.setOnClickListener {
            showDiglog1Button()
        }
    }

    fun showDiglog1Button(
//        title: String,
//        message: String,
//        callback: ConfirmDialogCallback?
    ) {
        val builder = context?.let { AlertDialog.Builder(it, R.style.AlertDialogNoBG) }
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.layout_dialog_item_request, null)
        builder?.setView(dialogView)
//        val tv_header_dialog = dialogView.findViewById<TextView>(R.id.tv_header_dialog)
//        val tv_content_dialog = dialogView.findViewById<TextView>(R.id.tv_content_dialog)
//        tv_header_dialog.text = "" + title
//        tv_content_dialog.text = "" + message
        val alertDialog = builder?.create()
        alertDialog?.window?.setLayout(500, 200)
        alertDialog?.show()
    }
}