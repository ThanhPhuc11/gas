package vn.gas.thq.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import vn.gas.thq.util.ViewController

abstract class BaseActivity : AppCompatActivity() {

    abstract var viewController: ViewController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        viewController = ViewController(getFragmentContainerId(), supportFragmentManager)
        initData()
    }

//    @JvmName("getViewController1")
//    fun getViewController(): ViewController {
//        return viewController
//    }

    abstract fun getLayoutId(): Int
    abstract fun getFragmentContainerId(): Int

    abstract fun initData()

    override fun onBackPressed() {
//        super.onBackPressed()
        viewController.popFragment()
    }
}