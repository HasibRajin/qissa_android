package com.example.qissa.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qissa.models.AnswerResponse
import com.example.qissa.network.ApiException
import com.example.qissa.repositories.AnswerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AnswerViewModel @Inject constructor(
    private val answerRepository: AnswerRepository
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

    private val _Items: MutableLiveData<AnswerResponse> by lazy {
        MutableLiveData<AnswerResponse>()
    }

    var items: LiveData<AnswerResponse?>? = null
        get() = _Items

    fun getAnswer(QuestionId: Int, likerId: Int?) = viewModelScope.launch {

        _eventShowLoading.value = true

        try {

            _Items.value = answerRepository.callGetAnswerApi(QuestionId, likerId)
//            Log.d(ContentValues.TAG, "findComments: getting comments done ${_Items.value }")
            items?.value?.let {
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
