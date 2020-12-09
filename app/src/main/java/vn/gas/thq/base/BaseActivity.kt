package vn.gas.thq.base

import androidx.appcompat.app.AppCompatActivity
import vn.gas.thq.util.ViewController

abstract class BaseActivity : AppCompatActivity() {

    abstract var viewController: ViewController
    override fun onBackPressed() {
        super.onBackPressed()
        viewController.popFragment()
    }
}