package com.example.qissa.ui.landing

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qissa.dialogue.LoginBottomDialogue
import com.example.qissa.models.LoginResponse
import com.example.qissa.network.ApiException
import com.example.qissa.repositories.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
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

    private val _Items: MutableLiveData<LoginResponse> by lazy {
        MutableLiveData<LoginResponse>()
    }

    val items: LiveData<LoginResponse?>
        get() = _Items

    fun doLogin(email: String, password: String) = viewModelScope.launch {

        _eventShowLoading.value = true

        try {
            Log.d(
                LoginBottomDialogue.TAG,
                "doSignInWDFEWRHTRYJTUKYILUYKUTJYWDDEFWGRTRHYJUKILKUYJEFWDW: Signing clicked"
            )

            _Items.value = loginRepository.callLoginApi(email, password)

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

    fun doSocialLogin(name: String, email: String, profilePic: String) = viewModelScope.launch {

        _eventShowLoading.value = true

        try {
            Log.d(
                LoginBottomDialogue.TAG,
                "doSignInWDFEWRHTRYJTUKYILUYKUTJYWDDEFWGRTRHYJUKILKUYJEFWDW: Signing clicked"
            )

            _Items.value = loginRepository.callSocialLoginApi(name, email, profilePic)

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
