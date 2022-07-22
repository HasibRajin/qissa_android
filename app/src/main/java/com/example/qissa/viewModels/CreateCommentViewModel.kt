package com.example.qissa.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qissa.models.CreateCommentResponse
import com.example.qissa.network.ApiException
import com.example.qissa.repositories.CommentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateCommentViewModel @Inject constructor(
    private val commentRepository: CommentRepository
) : ViewModel() {

    private val _eventShowMessage: MutableLiveData<String?> by lazy {
        MutableLiveData<String?>()
    }

    val eventShowMessage: LiveData<String?>
        get() = _eventShowMessage

    // ----------------------------------------------------------------

    private val _eventShowLoading: MutableLiveData<Boolean?> by lazy {
        MutableLiveData<Boolean?>()
    }

    val eventShowLoading: LiveData<Boolean?>
        get() = _eventShowLoading

    // ----------------------------------------------------------------

    private val _Items: MutableLiveData<CreateCommentResponse> by lazy {
        MutableLiveData<CreateCommentResponse>()
    }

    val items: LiveData<CreateCommentResponse?>
        get() = _Items

    fun createComment(token: String, commentText: String, postId: Int) = viewModelScope.launch {

        _eventShowLoading.value = true

        try {

            _Items.value = commentRepository.callCreateCommentApi(token, commentText, postId)
//            Log.d(ContentValues.TAG, "findComments: getting comments done ${_Items.value }")
            items.value?.let {
                Timber.tag("TEST").d(it.message)
            }

            _eventShowLoading.value = false
        } catch (e: ApiException) {

            Timber.tag("TEST").d(e.message.toString())
            _eventShowMessage.value = e.message
            _eventShowLoading.value = false
        }
    }
}
