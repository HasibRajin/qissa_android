package com.example.qissa.ui.writepost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qissa.models.CreatePostResponse
import com.example.qissa.network.ApiException
import com.example.qissa.repositories.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val postRepository: PostRepository
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

    private val _Items: MutableLiveData<CreatePostResponse> by lazy {
        MutableLiveData<CreatePostResponse>()
    }

    val items: LiveData<CreatePostResponse?>
        get() = _Items

    fun createPost(
        token: String,
        title: String? = null,
        details: String,
        image: String? = null,
        topicId: Int
    ) = viewModelScope.launch {

        _eventShowLoading.value = true

        try {

            _Items.value = postRepository.callDoPostApi(token, title, details, image, topicId)
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

    fun deletePost(token: String, postId: Int) = viewModelScope.launch {

        _eventShowLoading.value = true

        try {

            _Items.value = postRepository.callDeletePostApi(token, postId)
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
