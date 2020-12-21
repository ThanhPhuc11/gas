package vn.gas.thq.ui.login

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_login.*
import vn.gas.thq.MainActivity
import vn.gas.thq.base.BaseFragment
import vn.gas.thq.ui.main.MainFragment
import vn.gas.thq.ui.retail.RetailFragment
import vn.hongha.ga.R

class LoginFragment : BaseFragment() {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
////            param1 = it.getString(ARG_PARAM1)
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_login, container, false)
//    }

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

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        btnLogin.setOnClickListener {
//            ActivityUtils.pushFragment("main", RetailFragment.newInstance(), R.id.flContainer, fragmentManager)
//        }
//    }

    override fun setViewController() {
        viewController = (activity as MainActivity).viewController
    }

    override fun initView() {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun initObserver() {

    }

    override fun initData() {
        btnLogin.setOnClickListener {
            viewController?.pushFragment("Login", MainFragment.newInstance())
        }
    }
}