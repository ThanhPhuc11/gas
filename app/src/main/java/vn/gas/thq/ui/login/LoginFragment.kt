package vn.gas.thq.ui.login

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_login.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.base.ViewModelFactory
import vn.gas.thq.network.ApiService
import vn.gas.thq.network.LoginRetrofitBuilder
import vn.gas.thq.ui.main.MainFragment
import vn.gas.thq.util.ScreenId
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

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun initData() {
        btnLogin.setOnClickListener {
            showLoading()
            viewModel.doLogin(edtuserName.text.toString(), edtPassword.text.toString())
        }
    }

    override fun initObserver() {
        viewModel.getStatusAccessToken().observe(this, {
            if (it) {
                viewController?.replaceByFragment(ScreenId.SCREEN_LOGIN, MainFragment.newInstance())
                hideLoading()
            } else {
                hideLoading()
            }
        })
    }
}