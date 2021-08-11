package vn.gas.thq.ui.changepassword

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_change_pass_word.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.model.NickPassModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.LoginRetrofitBuilder
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.login.LoginFragment
import vn.gas.thq.ui.main.MainFragment
import vn.gas.thq.ui.sharedviewmodel.TokenSharedViewModel
import vn.gas.thq.util.ScreenId
import vn.hongha.ga.R


class ChangePasswordFragment : BaseFragment() {
    private lateinit var viewModel: ChangePasswordViewModel
    private lateinit var viewModelToken: TokenSharedViewModel
    private var check1: Boolean = false

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ChangePasswordFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_change_pass_word
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
                .get(ChangePasswordViewModel::class.java)
        viewModelToken = ViewModelProviders.of(this).get(TokenSharedViewModel::class.java)
    }

    override fun initView() {
        tvTitle.text = "Thay đổi mật khẩu"
        imgBack.setOnClickListener {
            viewController?.popFragment()
        }
    }

    override fun initData() {
        btnChange.setOnClickListener {
            validate()
            viewModel.changePassword(NewPasswordModel().apply {
                oldPassword = edtOldPassword.text.toString()
                newPassword = edtNewPassword.text.toString()
            })
        }
    }

    override fun initObserver() {
        viewModel.changeSuccess.observe(viewLifecycleOwner, {
            showMess("Thay đổi mật khẩu thành công")
            viewController?.popAllFragment()
            viewController?.pushFragment(ScreenId.SCREEN_LOGIN, LoginFragment.newInstance())
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

    private fun validate() {
        if (TextUtils.isEmpty(edtOldPassword.text.toString().trim())) {
            showMess("Vui lòng nhập mật khẩu cũ")
            return
        }
        if (TextUtils.isEmpty(edtNewPassword.text.toString().trim())) {
            showMess("Vui lòng nhập mật khẩu mới")
            return
        }
        if (TextUtils.isEmpty(edtConfirmPassword.text.toString().trim())) {
            showMess("Vui lòng nhập lại mật khẩu mới")
            return
        }
        if (edtNewPassword.text.toString() != edtConfirmPassword.text.toString()) {
            showMess("Nhập lại mật khẩu không chính xác")
            return
        }
    }
}