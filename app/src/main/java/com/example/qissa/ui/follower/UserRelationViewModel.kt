package com.example.qissa.ui.follower

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.qissa.datasource.FollowerPagingSource
import com.example.qissa.datasource.FollowingDataSource
import com.example.qissa.models.DataXXXXXXXXX
import com.example.qissa.models.DataXXXXXXXXXXX
import com.example.qissa.models.UserRelationRespose
import com.example.qissa.network.ApiException
import com.example.qissa.repositories.UserRelationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserRelationViewModel @Inject constructor(
    private val relationRepository: UserRelationRepository
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

    private val _Items: MutableLiveData<UserRelationRespose> by lazy {
        MutableLiveData<UserRelationRespose>()
    }

    val items: LiveData<UserRelationRespose?>
        get() = _Items

    fun doRelation(token: String, userId: Int, reactionType: String) = viewModelScope.launch {

        _eventShowLoading.value = true

        try {

            _Items.value = relationRepository.callCreateRelationApi(token, userId, reactionType)
            Log.d(ContentValues.TAG, "doRelation: ${items.value}")
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

    fun deleteRelation(userId: Int, token: String) = viewModelScope.launch {

        _eventShowLoading.value = true

        try {
            _Items.value = relationRepository.callDeleteRelationApi(userId, token)
            Log.d(ContentValues.TAG, "deleteRelation:  ${items.value}")

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

    fun getUserFollower(
        token: String,
        relatableID: Int,
        relatableType: String
    ): Flow<PagingData<DataXXXXXXXXX>> {

        return Pager(
            PagingConfig(pageSize = 20)
        ) {
            Timber.tag(ContentValues.TAG).d("paging data: getUserRelationPaged  cALLED")
            FollowerPagingSource(token, relationRepository, relatableID, relatableType)
        }
            .flow
            .cachedIn(viewModelScope)
    }

    fun getUserFollowing(
        token: String,
        relatableID: Int,
        relatableType: String
    ): Flow<PagingData<DataXXXXXXXXXXX>> {

        return Pager(
            PagingConfig(pageSize = 20)
        ) {
            Timber.tag(ContentValues.TAG).d("paging data: getUserRelationPaged  cALLED")
            FollowingDataSource(token, relationRepository, relatableID, relatableType)
        }
            .flow
            .cachedIn(viewModelScope)
    }
}
