package com.loguito.brainmove.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.loguito.brainmove.R
import kotlinx.android.synthetic.main.empty_list_widget.view.*

class EmptyListWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.empty_list_widget, this, true)
        attrs?.let {
            val typedArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.EmptyListWidget,
                defStyleAttr,
                defStyleRes
            )

            descriptionTextView.text =
                typedArray.getString(R.styleable.EmptyListWidget_description)

            typedArray.recycle()
        }
    }
}