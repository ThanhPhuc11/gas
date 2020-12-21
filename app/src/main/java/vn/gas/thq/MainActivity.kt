package vn.gas.thq

import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import vn.gas.thq.base.BaseActivity
import vn.gas.thq.ui.login.LoginFragment
import vn.gas.thq.util.ViewController
import vn.hongha.ga.R

open class MainActivity : BaseActivity() {
    override lateinit var viewController: ViewController

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        PrefsUtil(this).setString("diep", "value1");
//        System.out.println("@@@@ " + PrefsUtil(this).getString("diep"))
//
//
//    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getFragmentContainerId(): Int {
        return R.id.flContainer
    }

    override fun initData() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        viewController = ViewController(getFragmentContainerId(), supportFragmentManager)
//        ActivityUtils.pushFragment(
//            "Login", LoginFragment.newInstance(), R.id.flContainer, supportFragmentManager
//        )
        viewController.pushFragment("login", LoginFragment.newInstance())
    }
}