package com.example.qissa.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qissa.models.ReactionResponse
import com.example.qissa.network.ApiException
import com.example.qissa.repositories.ReactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReactionViewModel @Inject constructor(
    private val reactionRepository: ReactionRepository
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

    private val _Items: MutableLiveData<ReactionResponse> by lazy {
        MutableLiveData<ReactionResponse>()
    }

    val items: LiveData<ReactionResponse?>
        get() = _Items

    fun doReaction(token: String, postId: Int, reactionType: String) = viewModelScope.launch {

        _eventShowLoading.value = true

        try {

            _Items.value = reactionRepository.callCreateReactionApi(token, postId, reactionType)
            Log.d(TAG, "doReaction: like done wefewlgfnwignipkngwp")
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

    fun deleteReaction(reactionId: Int, token: String) = viewModelScope.launch {

        _eventShowLoading.value = true

        try {
            _Items.value = reactionRepository.callDeleteReactionApi(reactionId, token)
            Log.d(TAG, "deleteReaction: like deleted wefewlgfnwignipkngwp")

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
