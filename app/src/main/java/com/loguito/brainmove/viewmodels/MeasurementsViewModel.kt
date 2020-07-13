package com.loguito.brainmove.viewmodels

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.loguito.brainmove.R
import com.loguito.brainmove.models.remote.Measure


class MeasurementsViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var _userName = MutableLiveData<String>()
    private var _measures = MutableLiveData<Measure?>()
    private var _measuresError = MutableLiveData<Int>()
    private var _loadingVisibility = MutableLiveData<Pair<Boolean, Int?>>()
    private var _profileImageUrl = MutableLiveData<String>()

    private lateinit var userId: String

    val userName: LiveData<String>
        get() = _userName
    val measures: LiveData<Measure?>
        get() = _measures
    val measuresError: LiveData<Int>
        get() = _measuresError
    val loadingVisibility: LiveData<Pair<Boolean, Int?>>
        get() = _loadingVisibility
    val profileImageUrl: LiveData<String>
        get() = _profileImageUrl

    init {
        auth.currentUser?.let { user ->
            userId = user.uid
            getUserImage(user.photoUrl.toString())
            _loadingVisibility.postValue(Pair(true, R.string.loading_measures_message))
            _userName.postValue(user.displayName ?: "")
            db.collection("measures")
                .limit(1)
                .orderBy("date", Query.Direction.DESCENDING)
                .whereEqualTo("user_id", user.uid)
                .get()
                .addOnSuccessListener { result ->
                    _loadingVisibility.postValue(Pair(false, null))
                    if (result.isEmpty) {
                        _measures.postValue(null)
                    } else {
                        for (document in result) {
                            _measures.postValue(document.toObject(Measure::class.java))
                        }
                    }
                }
                .addOnFailureListener {
                    _loadingVisibility.postValue(Pair(false, null))
                    _measuresError.postValue(R.string.retrieve_measures_error)
                }
        }
    }

    fun uploadUserImage(uri: Uri) {
        MediaManager
            .get()
            .upload(uri)
            .callback(object : UploadCallback {
                override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                    resultData?.get("secure_url")?.let {
                        _loadingVisibility.postValue(Pair(false, null))
                        auth.currentUser?.let { user ->
                            val builder = UserProfileChangeRequest.Builder()
                                .setPhotoUri(it.toString().toUri()).build()
                            user.updateProfile(builder).addOnCompleteListener { response ->
                                if (response.isSuccessful) {
                                    getUserImage(it.toString())
                                }
                            }
                        }
                    }
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    _loadingVisibility.postValue(Pair(false, null))
                }

                override fun onStart(requestId: String?) {
                    _loadingVisibility.postValue(Pair(true, R.string.loading_upload_photo_message))
                }
            })
            .option("invalidate", true)
            .option("overwrite", true)
            .option("public_id", userId).dispatch()
    }

    private fun getUserImage(imageUrl: String) {
        if (imageUrl.isNotEmpty()) {
            _profileImageUrl.postValue(imageUrl)
        }
    }
}