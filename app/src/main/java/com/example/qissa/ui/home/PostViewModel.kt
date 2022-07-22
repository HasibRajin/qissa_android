package com.example.qissa.ui.home

import android.content.ContentValues
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.qissa.datasource.PostPagingSource
import com.example.qissa.datasource.SingleUserPostPagingSource
import com.example.qissa.datasource.TopicPostPagingSource
import com.example.qissa.models.DataX
import com.example.qissa.models.PostResponse
import com.example.qissa.network.ApiException
import com.example.qissa.repositories.PostRepository
import com.example.qissa.utils.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val sharedPreference: SharedPreference
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

    private val _Items: MutableLiveData<PostResponse> by lazy {
        MutableLiveData<PostResponse>()
    }

    private val items: LiveData<PostResponse?>
        get() = _Items

    fun getPostsPaged(
        item: DataX?,
        position: Int,
        list: ArrayList<DataX>?
    ): Flow<PagingData<DataX>> {

        return Pager(
            PagingConfig(pageSize = 20)
        ) {
            Timber.tag(ContentValues.TAG)
                .d("jwoifjwofiwoeifey: getPostsPaged  cALLED" + list?.size.toString())
            PostPagingSource(sharedPreference, postRepository, item, position, list)
        }
            .flow
            .cachedIn(viewModelScope)
    }

    fun getSingleUserPosts(userID: Int): Flow<PagingData<DataX>> {

        return Pager(
            PagingConfig(pageSize = 20)
        ) {
            Timber.tag(ContentValues.TAG).d("paging data: getSinglePostsPaged  cALLED")
            SingleUserPostPagingSource(sharedPreference, postRepository, userID)
        }
            .flow
            .cachedIn(viewModelScope)
    }

    fun getTopicUserPosts(topicId: Int, userID: Int?): Flow<PagingData<DataX>> {

        return Pager(
            PagingConfig(pageSize = 20)
        ) {
            Timber.tag(ContentValues.TAG).d("paging data: getSinglePostsPaged  cALLED")
            TopicPostPagingSource(postRepository, topicId, userID)
        }
            .flow
            .cachedIn(viewModelScope)
    }

    fun getPost(current_page: Int) = viewModelScope.launch {

        _eventShowLoading.value = true

        try {

//            _Items.value = postRepository.callGetPostApi(current_page)

            items.value?.let {
//                Timber.tag("TEST").d(items.value.toString())
            }

            _eventShowLoading.value = false
        } catch (e: ApiException) {

            Timber.tag("TEST").d(e.message.toString())
            _eventShowMessage.value = e.message
            _eventShowLoading.value = false
        }
    }
}
