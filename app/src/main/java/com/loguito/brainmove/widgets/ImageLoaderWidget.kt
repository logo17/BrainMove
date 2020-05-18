package com.loguito.brainmove.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding3.widget.textChanges
import com.loguito.brainmove.R
import com.loguito.brainmove.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.image_loader_widget.view.*
import java.util.concurrent.TimeUnit

class ImageLoaderWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.image_loader_widget, this, true)
        setBackgroundColor(ContextCompat.getColor(context, R.color.white))

        imageUrlEditText.textChanges()
            .skipInitialValue()
            .filter { it.isNotEmpty() }
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                Glide.with(context).load(it.toString()).into(loaderImageView)
            }
            .subscribe()
    }
}