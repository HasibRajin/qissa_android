package com.example.qissa.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.qissa.models.DataXXXXXXXXXXXXXXX
import com.example.qissa.network.ApiException
import com.example.qissa.repositories.QuestionRepositories
import com.example.qissa.utils.SharedPreference
import retrofit2.HttpException
import java.io.IOException

class QuestionPagingSource(
    private val sharedPreference: SharedPreference,
    private val repository: QuestionRepositories,
) : PagingSource<Int, DataXXXXXXXXXXXXXXX>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataXXXXXXXXXXXXXXX> {
        val pagePosition = params.key ?: 1

        return try {

            val response = repository.callGetQuestionApi(
                pagePosition, sharedPreference.getUser()?.id
            )
            val result = response.data.data
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
    override fun getRefreshKey(state: PagingState<Int, DataXXXXXXXXXXXXXXX>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
