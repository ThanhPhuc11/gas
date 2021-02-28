package vn.gas.thq.ui.login

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_login.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.LoginRetrofitBuilder
import vn.gas.thq.ui.main.MainFragment
import vn.gas.thq.util.ScreenId
import vn.hongha.ga.BuildConfig
import vn.hongha.ga.R


class LoginFragment : BaseFragment() {
    private lateinit var viewModel: LoginViewModel

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
    }

    override fun initView() {
        tvVersion.text = "version: ${BuildConfig.VERSION_NAME}"
    }

    override fun initData() {
        btnLogin.setOnClickListener {
            viewModel.doLogin(edtuserName.text.toString(), edtPassword.text.toString())
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
            viewController?.replaceByFragment(ScreenId.SCREEN_LOGIN, MainFragment.newInstance())
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