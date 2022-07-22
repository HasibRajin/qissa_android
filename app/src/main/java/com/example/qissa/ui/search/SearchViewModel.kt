package com.example.qissa.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qissa.models.SearchResponse
import com.example.qissa.network.ApiException
import com.example.qissa.repositories.SearchRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepositories
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

    private val _Items: MutableLiveData<SearchResponse> by lazy {
        MutableLiveData<SearchResponse>()
    }

    val items: LiveData<SearchResponse?>
        get() = _Items

    fun getSearchResponse(requestData: String, likerId: Int?) = viewModelScope.launch {

        _eventShowLoading.value = true

        try {

            _Items.value = searchRepository.callSearchApi(requestData, likerId)
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
