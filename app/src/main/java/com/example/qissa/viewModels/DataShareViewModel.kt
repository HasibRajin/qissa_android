package com.example.qissa.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qissa.models.Data
import com.example.qissa.models.DataXXXXXXXXXXXXX

class DataShareViewModel : ViewModel() {
    private val _userInfo = MutableLiveData<Data>()
    val userInfo: LiveData<Data>
        get() = _userInfo

    fun setUserInfo(data: Data) {
        _userInfo.value = data
    }

    private val _searchData = MutableLiveData<DataXXXXXXXXXXXXX>()

    val searchData: LiveData<DataXXXXXXXXXXXXX>
        get() = _searchData

    fun setSearchData(data: DataXXXXXXXXXXXXX) {
        _searchData.value = data
        Log.d(TAG, "setSignInUser: $data")
    }
//    private fun getUserInfo(): List<Data> {
//        val options = mutableListOf<String>()
//    }
}
