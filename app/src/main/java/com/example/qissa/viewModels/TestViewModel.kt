package com.example.qissa.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qissa.models.TestResponse
import com.example.qissa.network.ApiException
import com.example.qissa.repositories.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TestViewModel  @Inject constructor(
    private val testRepository: TestRepository
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

    private val _testItems: MutableLiveData<List<TestResponse>> by lazy {
        MutableLiveData<List<TestResponse>>()
    }

    val testItems: LiveData<List<TestResponse>?>
        get() = _testItems

    // ----------------------------------------------------------------

    fun getTestData() = viewModelScope.launch {
        _eventShowLoading.value = true
        try {

            val response = testRepository.getTest()
            if (response.test != null) {
                Timber.tag("TEST").d(response.test)
//                _testItems.value = response.test
            } else {
                Timber.tag("TEST").d("Exception")
                throw ApiException("CODE:${response.test}")
            }
        } catch (e: ApiException) {
            _eventShowMessage.value = e.message
            Timber.tag("TEST").d("Exception2")
        }

        _eventShowLoading.value = false
    }

    }