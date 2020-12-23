package vn.gas.thq

import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
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

    @Override
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_UP) {
           // EventBus.getDefault().post(CheckKeyboardEvent(ev))
            onCheckKeyboardEvent(ev)
        }
        return super.dispatchTouchEvent(ev)
    }

    open fun onCheckKeyboardEvent(event: MotionEvent) {
        if(currentFocus == null) return
        currentFocus.let {
            val v: View = this.currentFocus!!
            if (v != null && (v is EditText || v is TextInputEditText) &&
                !v.javaClass.name.startsWith("android.webkit.")
            ) {
                val sourceCoordinates = IntArray(2)
                v.getLocationOnScreen(sourceCoordinates)
                val x: Float = event.getRawX() + v.left - sourceCoordinates[0]
                val y: Float = event.getRawY() + v.top - sourceCoordinates[1]
                if (x < v.left || x > v.right || y < v.top || y > v.bottom) {
                    hideKeyboard()
                }
            }
        }
    }
}