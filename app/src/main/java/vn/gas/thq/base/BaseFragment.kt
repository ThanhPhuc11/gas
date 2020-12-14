package vn.gas.thq.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import vn.hongha.ga.R

abstract class BaseFragment : Fragment() {
    //    lateinit var viewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        initView()
        initObserver()
        initData()
//        hideTopBar()
    }

    abstract fun initView()
    abstract fun getLayoutId(): Int
    abstract fun initObserver()
    abstract fun initData()
}