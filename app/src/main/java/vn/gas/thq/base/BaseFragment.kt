package vn.gas.thq.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vn.gas.thq.util.ViewController

abstract class BaseFragment : Fragment() {
    //    lateinit var viewModel
//    var viewModel = null
    private var mActivity: BaseActivity? = null
    var viewController: ViewController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
//        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass())
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
        initObserver()
        initData()
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
}