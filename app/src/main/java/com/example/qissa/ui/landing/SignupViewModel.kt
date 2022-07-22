package com.example.qissa.ui.landing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qissa.models.SignupResponse
import com.example.qissa.network.ApiException
import com.example.qissa.repositories.SignupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupRepository: SignupRepository
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

    private val _Items: MutableLiveData<SignupResponse> by lazy {
        MutableLiveData<SignupResponse>()
    }

    val items: LiveData<SignupResponse?>
        get() = _Items

    fun doSignup(name: String, email: String, password: String, confirm_password: String) =
        viewModelScope.launch {

            _eventShowLoading.value = true

            try {

                _Items.value =
                    signupRepository.callSignupApi(name, email, password, confirm_password)

                items.value?.let {
                    Timber.tag("SIGNUP").d(it.message)
                }

                _eventShowLoading.value = false
                _Items.value = null
            } catch (e: ApiException) {
                Timber.tag("TEST").d(e.message.toString())
                _eventShowMessage.value = e.message
                _eventShowLoading.value = false
            }
        }
}
