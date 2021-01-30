package vn.gas.thq.base

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import vn.gas.thq.util.CommonUtils
import vn.gas.thq.util.ViewController

abstract class BaseFragment : Fragment() {
    private var mProgressDialog: ProgressDialog? = null

    private var mActivity: BaseActivity? = null
    var viewController: ViewController? = null
    var childViewController: ViewController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        if (arguments != null) {
//            parseArgs(arguments)
//        }
//        showTopBar(false)
//        viewController = ViewController(getFragmentContainerId(), supportFragmentManager)
        setViewController()
        initView()
        initData()
        initObserver()
//        hideTopBar()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            this.mActivity = context
//            context.onFragmentAttached()
        }
    }

    abstract fun setViewController()
    abstract fun setupViewModel()
    abstract fun initView()
    abstract fun getLayoutId(): Int
    abstract fun initObserver()
    abstract fun initData()

    open fun hideKeyboard() {
        if (mActivity != null) {
            mActivity!!.hideKeyboard()
        }
    }

    open fun showLoading() {
        hideLoading()
        mProgressDialog = CommonUtils.showLoadingDialog(this.context)
    }

    open fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.cancel()
        }
    }

    open fun showMess(str: String?) {
        Toast.makeText(context, str ?: "", Toast.LENGTH_LONG).show()
    }
}