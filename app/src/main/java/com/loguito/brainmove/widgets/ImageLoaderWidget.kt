package com.loguito.brainmove.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding3.widget.itemSelections
import com.loguito.brainmove.R
import com.loguito.brainmove.models.remote.BlockImage
import com.loguito.brainmove.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.image_loader_widget.view.*

class ImageLoaderWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val _handleImageSelected = SingleLiveEvent<String>()
    val handleImageSelected: LiveData<String>
        get() = _handleImageSelected

    var blockImages: List<BlockImage> = ArrayList()
        set(value) {
            field = value
            val adapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item, value
            )
            blockImageDropdown.adapter = adapter
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.image_loader_widget, this, true)
        setBackgroundColor(ContextCompat.getColor(context, R.color.white))

        blockImageDropdown.itemSelections()
            .filter { blockImages.isNotEmpty() }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                val imageUrl = blockImages[it].imageUrl
                _handleImageSelected.postValue(imageUrl)
                Glide.with(context).load(imageUrl).into(loaderImageView)
            }
            .subscribe()
    }
}