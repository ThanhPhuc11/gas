package vn.gas.thq

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import vn.gas.thq.base.BaseActivity
import vn.gas.thq.model.ExpriteEventModel
import vn.gas.thq.ui.downloadApk.ApkDialog
import vn.gas.thq.ui.downloadApk.NeedUpgradeApkEvent
import vn.gas.thq.ui.login.LoginFragment
import vn.gas.thq.ui.main.IntentShareViewModel
import vn.gas.thq.ui.main.MainFragment
import vn.gas.thq.util.AppConstants
import vn.gas.thq.util.ScreenId
import vn.gas.thq.util.ViewController
import vn.hongha.ga.R


open class MainActivity : BaseActivity() {
    override lateinit var viewController: ViewController
    private lateinit var intentShareViewModel: IntentShareViewModel
    private var doubleBackToExitPressedOnce = false
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
        viewController.pushFragment(ScreenId.SCREEN_LOGIN, LoginFragment.newInstance())
        intentShareViewModel = ViewModelProviders.of(this).get(IntentShareViewModel::class.java)

        checkPermissions()
        intentShareViewModel.callbackFirebaseType.value =
            intent?.extras?.getString(AppConstants.NOTIFI_TYPE)

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intentShareViewModel.callbackFirebaseType.value =
            intent?.extras?.getString(AppConstants.NOTIFI_TYPE)
    }

    var permissions = arrayOf<String>(
        Manifest.permission.INTERNET,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private fun checkPermissions(): Boolean {
        var result: Int
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(this, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), 100)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 100) {
            if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {

            } else {
                //PERMISSION_DENIED
                // do something
                Toast.makeText(this, "Vui lòng cấp quyền để sử dụng ứng dụng!", Toast.LENGTH_LONG)
                    .show()
                finish()
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    var apkD: ApkDialog? = null;

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onNeedUpgradeApEvent(event: NeedUpgradeApkEvent?) {
        if (event!!.mType == NeedUpgradeApkEvent.MOVE_LOGIN_SCREEN) {
            viewController.onNeedUpgradeApk()
        } else {
            if (apkD != null) {
                try {
                    apkD!!.dismiss()
                } catch (e: Exception) {
                }
            }
            var vName = "";
            try {
                var s = event.url
                val f = s.lastIndexOf("v")
                val e = s.lastIndexOf("apk") - 1
                vName = s.substring(f, e)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            apkD = ApkDialog(
                this,
                "Đã có phiên bản mới " + vName + " \n\nVui lòng thực thực hiện nâng cấp phiên bản mới!",
                event.url
            )
            apkD!!.show()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onNeedUpgradeApEvent(event: ExpriteEventModel?) {
        viewController.popAllFragment()
        viewController.pushFragment(ScreenId.SCREEN_LOGIN, LoginFragment.newInstance())
    }

    override fun onBackPressed() {
        if (viewController.currentFragment is MainFragment || viewController.currentFragment is LoginFragment) {
            if (doubleBackToExitPressedOnce) {
                finish()
                return
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Nhấn một lần nữa để thoát", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else {
            super.onBackPressed()
        }
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
        if (currentFocus == null) return
        currentFocus.let {
            val v: View = this.currentFocus!!
            if ((v is EditText || v is TextInputEditText) && !v.javaClass.name.startsWith("android.webkit.")
            ) {
                val sourceCoordinates = IntArray(2)
                v.getLocationOnScreen(sourceCoordinates)
                val x: Float = event.rawX + v.left - sourceCoordinates[0]
                val y: Float = event.rawY + v.top - sourceCoordinates[1]
                if (x < v.left || x > v.right || y < v.top || y > v.bottom) {
                    hideKeyboard()
                    v.clearFocus()
                }
            }
        }
    }
}