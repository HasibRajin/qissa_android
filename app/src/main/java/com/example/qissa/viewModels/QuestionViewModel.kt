package com.example.qissa.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.qissa.datasource.QuestionPagingSource
import com.example.qissa.models.DataXXXXXXXXXXXXXXX
import com.example.qissa.models.QuestionResponse
import com.example.qissa.repositories.QuestionRepositories
import com.example.qissa.utils.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val questionRepositories: QuestionRepositories,
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

    private val _Items: MutableLiveData<QuestionResponse> by lazy {
        MutableLiveData<QuestionResponse>()
    }

    private val items: LiveData<QuestionResponse?>
        get() = _Items

    fun getQuestionPaged(): Flow<PagingData<DataXXXXXXXXXXXXXXX>> {

        return Pager(
            PagingConfig(pageSize = 20)
        ) {
            QuestionPagingSource(sharedPreference, questionRepositories)
        }
            .flow
            .cachedIn(viewModelScope)
    }
}
