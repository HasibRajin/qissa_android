package com.example.qissa.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qissa.models.TopicResponse
import com.example.qissa.network.ApiException
import com.example.qissa.repositories.TopicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TopicViewModel @Inject constructor(
    private val topicRepository: TopicRepository
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

    private val _Items: MutableLiveData<TopicResponse> by lazy {
        MutableLiveData<TopicResponse>()
    }

    val items: LiveData<TopicResponse?>
        get() = _Items

    fun getTopics() = viewModelScope.launch {

        _eventShowLoading.value = true

        try {

            _Items.value = topicRepository.callGetTopicApi()
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
