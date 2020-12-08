package vn.gas.thq

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.marcotejeda.mvp_retrofit_kotlin.data.datasourse.prefrerences.PrefsUtil
import kotlinx.android.synthetic.main.activity_main.*
import vn.gas.thq.ui.login.LoginFragment
import vn.gas.thq.util.ActivityUtils
import vn.hongha.ga.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PrefsUtil(this).setString("diep", "value1");
        System.out.println("@@@@ " + PrefsUtil(this).getString("diep"))

        ActivityUtils.pushFragment(
            "Login", LoginFragment.newInstance(), R.id.flContainer, supportFragmentManager
        )
    }
}