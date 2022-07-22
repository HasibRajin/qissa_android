package com.example.qissa.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qissa.models.UpdateProfileResponse
import com.example.qissa.network.ApiException
import com.example.qissa.repositories.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
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

    private val _Items: MutableLiveData<UpdateProfileResponse> by lazy {
        MutableLiveData<UpdateProfileResponse>()
    }

    val items: LiveData<UpdateProfileResponse?>
        get() = _Items

    fun updateUserInfo(
        token: String,
        name: String,
        email: String,
        phone: String,
    ) = viewModelScope.launch {

        _eventShowLoading.value = true

        try {

            _Items.value = profileRepository.callUpdateUserInfoApi(
                token,
                name,
                email,
                phone,
            )
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

    fun updateProfileInfo(
        token: String,
        dateOfBirth: String,
        gender: String,
        education: String,
        address: String
    ) = viewModelScope.launch {

        _eventShowLoading.value = true

        try {

            _Items.value = profileRepository.callUpdateProfileInfoApi(
                token,
                dateOfBirth,
                gender,
                education,
                address
            )
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

    fun updateProfileImage(
        token: String,
        profilePic: String,

    ) = viewModelScope.launch {

        _eventShowLoading.value = true

        try {

            _Items.value = profileRepository.callUpdateProfileImageApi(
                token,
                profilePic,

            )
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
