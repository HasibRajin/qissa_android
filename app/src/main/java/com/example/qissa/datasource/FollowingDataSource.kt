package com.example.qissa.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.qissa.models.DataXXXXXXXXXXX
import com.example.qissa.network.ApiException
import com.example.qissa.repositories.UserRelationRepository
import retrofit2.HttpException

class FollowingDataSource(
    private val token: String,
    private val repository: UserRelationRepository,
    private val relatableId: Int,
    private val relatableType: String
) : PagingSource<Int, DataXXXXXXXXXXX>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataXXXXXXXXXXX> {
        val pagePosition = params.key ?: 1

        return try {

            val response =
                repository.callGetFollowing(
                    token, pagePosition, relatableId, relatableType
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
        } catch (exception: java.io.IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        } catch (exception: ApiException) {
            return LoadResult.Error(exception)
        }
    }

    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, DataXXXXXXXXXXX>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
