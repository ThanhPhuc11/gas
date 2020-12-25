package vn.gas.thq.customview

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.layout_step_toolbar.view.*
import vn.hongha.ga.R

class StepView : LinearLayout {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_step_toolbar, this, true)
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.StepView)
        try {
            setStepDone(a.getString(R.styleable.StepView_stepDone))
        } finally {
            a.recycle()
        }
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    fun setStepDone(step: String?) {
        if (step == "1") {
            viewLine.setBackgroundColor(resources.getColor(R.color.blue_14AFB4))
        } else if (step == "2") {
            viewLine.setBackgroundColor(resources.getColor(R.color.blue_14AFB4))
            tvStep2.background = context.resources.getDrawable(R.drawable.ic_step)
        }
    }
}