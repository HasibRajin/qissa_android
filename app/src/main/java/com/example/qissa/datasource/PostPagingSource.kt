package com.example.qissa.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.qissa.models.DataX
import com.example.qissa.network.ApiException
import com.example.qissa.repositories.PostRepository
import com.example.qissa.utils.SharedPreference
import retrofit2.HttpException
import java.io.IOException

class PostPagingSource(
    private val sharedPreference: SharedPreference,
    private val repository: PostRepository,
    val item: DataX?,
    val position: Int,
    val list: ArrayList<DataX>?
) : PagingSource<Int, DataX>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataX> {
        val pagePosition = params.key ?: 1

//        if(position>0){
//        }
//        fun updateData(){
//            val result = list
////            }
//
//            //                result.addAll(response.data.data as ArrayList)
////            } else {
////                result.remove(item)
////            }
//
//            run {
//                val nextKey = if (result?.isEmpty() == true) {
//                    null
//                } else {
//                    pagePosition
//                }
//
//                LoadResult.Page(
//                    data = result  as ArrayList<DataX>,
//                    prevKey = if (pagePosition == 1) null else pagePosition - 1,
//                    nextKey = nextKey
//                )
//            }
//        }

        return try {
//            if (position < 0) {
//
//            val result = if (position > 0) {
//                Timber.tag(ContentValues.TAG)
//                    .d("jwoifjwofiwoeifey: getPostsPaged  cALLED" + list?.size.toString())
//                val listy = list?.removeAt(position) as List<DataX>
//                Timber.tag(ContentValues.TAG)
//                    .d("jwoifjwofiwoeifey: getPostsPaged  cALLED" + listy?.size.toString())
//                listy
//            } else {
            val response = repository.callGetPostApi(
                pagePosition, sharedPreference.getUser()?.id
            )
            val result = response.data.data
//            }

            //                result.addAll(response.data.data as ArrayList)
//            } else {
//                result.remove(item)
//            }

            run {
                val nextKey = if (result.isEmpty()) {
                    null
                } else {
                    pagePosition + 1
                }

                LoadResult.Page(
                    data = result,
                    prevKey = if (pagePosition == 1) null else pagePosition - 1,
                    nextKey = nextKey
                )
            }
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        } catch (exception: ApiException) {
            return LoadResult.Error(exception)
        }
    }

    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, DataX>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
//        var result: ArrayList<DataX> = ArrayList<DataX>()
    }
}
