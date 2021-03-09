package vn.gas.thq.ui.login

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_login.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.model.NickPassModel
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.LoginRetrofitBuilder
import vn.gas.thq.network.RetrofitBuilder
import vn.gas.thq.ui.main.MainFragment
import vn.gas.thq.ui.sharedviewmodel.TokenSharedViewModel
import vn.gas.thq.util.ScreenId
import vn.hongha.ga.BuildConfig
import vn.hongha.ga.R


class LoginFragment : BaseFragment() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var viewModelToken: TokenSharedViewModel
    private var check1: Boolean = false

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun setViewController() {
        viewController = (activity as MainActivity).viewController
    }

    override fun setupViewModel() {
        viewModel =
            ViewModelProviders.of(this,
                context?.let {
                    LoginRetrofitBuilder.getInstance(it)?.create(ApiService::class.java)
                        ?.let { apiService ->
                            ViewModelFactory(apiService, context)
                        }
                })
                .get(LoginViewModel::class.java)
        viewModelToken = ViewModelProviders.of(this).get(TokenSharedViewModel::class.java)
    }

    override fun initView() {
        tvVersion.text = "version: ${BuildConfig.VERSION_NAME}"
    }

    override fun initData() {
        cbRemember.isChecked = AppPreferencesHelper(context).remember
        if (cbRemember.isChecked) {
            val nickPassModel = AppPreferencesHelper(context).nickPass
            edtuserName.setText(nickPassModel?.userName)
            edtPassword.setText(nickPassModel?.passWord)
        }
        btnLogin.setOnClickListener {
            viewModel.doLogin(edtuserName.text.toString(), edtPassword.text.toString())
        }
        tvSwapDev.setOnClickListener {
            check1 = true
            Handler().postDelayed({
                check1 = false
            }, 2000)
        }
        imgLogo.setOnClickListener {
            if (check1) {
                showMess("Chức năng ẩn")
            }
        }
    }

    override fun initObserver() {
//        viewModel.getStatusAccessToken().observe(this, {
//            if (it) {
//                viewController?.replaceByFragment(ScreenId.SCREEN_LOGIN, MainFragment.newInstance())
//                hideLoading()
//            } else {
//                hideLoading()
//            }
//        })
        viewModel.getSuccessToken().observe(viewLifecycleOwner, {
            if (cbRemember.isChecked) {
                AppPreferencesHelper(context).nickPass =
                    NickPassModel().apply {
                        userName = edtuserName.text.toString().trim()
                        passWord = edtPassword.text.toString().trim()
                    }
            } else {
                AppPreferencesHelper(context).nickPass = null
            }
            AppPreferencesHelper(context).remember = cbRemember.isChecked
            viewModelToken.sharedToken.value = AppPreferencesHelper(context).tokenModel.accessToken
            if (this.tag == "SCREEN_LOGIN") {
                viewController?.replaceByFragment(ScreenId.SCREEN_MAIN, MainFragment.newInstance())
            } else {
                viewController?.popFragment()

            }
            hideLoading()
        })
//
//        viewModel.getFailToken().observe(viewLifecycleOwner, {
//            hideLoading()
//        })

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
}