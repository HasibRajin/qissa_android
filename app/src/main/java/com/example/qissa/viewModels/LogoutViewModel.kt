package com.example.qissa.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qissa.dialogue.LoginBottomDialogue
import com.example.qissa.models.LoginResponse
import com.example.qissa.network.ApiException
import com.example.qissa.repositories.LogoutRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val loginRepository: LogoutRepositories
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

    fun doLogout(token: String) = viewModelScope.launch {

        _eventShowLoading.value = true

        try {
            Log.d(
                LoginBottomDialogue.TAG,
                "doSignInWDFEWRHTRYJTUKYILUYKUTJYWDDEFWGRTRHYJUKILKUYJEFWDW: Signing clicked"
            )

            _Items.value = loginRepository.callLogoutApi(token)

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
