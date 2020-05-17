package com.loguito.brainmove.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.loguito.brainmove.R
import kotlinx.android.synthetic.main.item_single_detail.view.*

class SingleDetailWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var detailText: String = String()
        set(value) {
            field = value
            detailTextView.text = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.item_single_detail, this, true)
        attrs?.let {
            val typedArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.SingleDetailWidget,
                defStyleAttr,
                defStyleRes
            )

            titleTextView.text = typedArray.getString(R.styleable.SingleDetailWidget_title)
            detailTextView.text = typedArray.getString(R.styleable.SingleDetailWidget_detail)
            typedArray.recycle()
        }
    }
}