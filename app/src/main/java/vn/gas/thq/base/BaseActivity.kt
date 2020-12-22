package vn.gas.thq.base

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
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

    open fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onBackPressed() {
        viewController.popFragment()
    }
}