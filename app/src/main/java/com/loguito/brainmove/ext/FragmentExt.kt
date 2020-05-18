package com.loguito.brainmove.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.loguito.brainmove.R
import com.loguito.brainmove.utils.Constants.Companion.PICKFILE_RESULT_CODE
import com.loguito.brainmove.utils.Constants.Companion.PICKIMAGE_RESULT_CODE
import com.loguito.brainmove.widgets.OnDialogButtonClicked


fun showSnackbar(view: View, message: String) {
    Snackbar.make(
        view,
        message,
        Snackbar.LENGTH_LONG
    ).show()
}

fun buildDateRangePicker(): MaterialDatePicker<Pair<Long, Long>> {
    val builder: MaterialDatePicker.Builder<Pair<Long, Long>> =
        MaterialDatePicker.Builder.dateRangePicker()
    return builder.build()
}

fun Fragment.showFileChooser() {
    var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
    chooseFile.type = "text/csv"
    chooseFile = Intent.createChooser(chooseFile, "Choose a file")
    startActivityForResult(chooseFile, PICKFILE_RESULT_CODE)
}

fun Fragment.showImageChooser() {
    var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
    chooseFile.type = "image/*"
    chooseFile =
        Intent.createChooser(chooseFile, requireContext().getString(R.string.choose_image_title))
    startActivityForResult(chooseFile, PICKIMAGE_RESULT_CODE)
}

fun Fragment.dismissKeyboard() {
    val imm: InputMethodManager =
        requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    // To get the correct window token, lets first get the currently focused view
    var view = requireActivity().currentFocus
    // To get the window token when there is no currently focused view, we have a to create a view
    if (view == null) {
        view = View(activity)
    }
    // hide the keyboard
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.navigateBack() {
    dismissKeyboard()
    findNavController().popBackStack()
}

fun Fragment.dpToPx(context: Context, valueInDp: Float): Float {
    val displayMetrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, displayMetrics)
}

fun Fragment.showDialog(
    descriptionText: String,
    mainButtonText: Int? = null,
    secondaryButtonText: Int? = null,
    listener: OnDialogButtonClicked? = null
) {
    val builder = AlertDialog.Builder(requireContext())
    builder.setTitle(requireContext().getString(R.string.app_name))
    builder.setMessage(descriptionText)

    mainButtonText?.let {
        builder.setPositiveButton(it) { _, _ ->
            listener?.onPositiveButtonClicked()
        }
    }

    secondaryButtonText?.let {
        builder.setNegativeButton(it) { _, _ ->
            listener?.onNegativeButtonClicked()
        }
    }

    builder.show()
}