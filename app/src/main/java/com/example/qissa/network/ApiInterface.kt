package com.example.qissa.network

import com.example.qissa.models.* // ktlint-disable no-wildcard-imports
import retrofit2.Response
import retrofit2.http.* // ktlint-disable no-wildcard-imports

interface ApiInterface {
    @GET("test")
    suspend fun getTestData(): Response<TestResponse>

    @POST("api/login")
    @FormUrlEncoded
    suspend fun doLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @POST("api/socialLogin")
    @FormUrlEncoded
    suspend fun doSocialLogin(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("profile_pic") profilePic: String
    ): Response<LoginResponse>

    @POST("api/signup")
    @FormUrlEncoded
    suspend fun doSignup(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirm_password") confirm_password: String,
    ): Response<SignupResponse>

    @POST("api/logout")
    suspend fun doLogout(
        @Header("Authorization") token: String,
    ): Response<LoginResponse>

    @PUT("api/user/info")
    @FormUrlEncoded
    suspend fun doUpdateUserInfo(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
    ): Response<UpdateProfileResponse>

    @PUT("api/profile/info")
    @FormUrlEncoded
    suspend fun doUpdateProfileInfo(
        @Header("Authorization") token: String,
        @Field("date_of_birth") dateOfBirth: String,
        @Field("gender") gender: String,
        @Field("education") education: String,
        @Field("address") address: String,
    ): Response<UpdateProfileResponse>

    @PUT("api/profile/image")
    @FormUrlEncoded
    suspend fun doUpdateProfileImage(
        @Header("Authorization") token: String,
        @Field("profile_pic") profilePic: String,

    ): Response<UpdateProfileResponse>

    @GET("api/post")
    suspend fun getPost(
        @Query("current_page") currentPage: Int,
        @Query("liker_id") likerId: Int?
    ): Response<PostResponse>

    @GET("api/post")
    suspend fun getPagedPost(
        @Query("current_page") currentPage: Int,
    ): Response<PostResponse>

    @GET("api/post/{id}")
    suspend fun getSingleUserPost(
        @Path("id") id: Int,
        @Query("current_page") currentPage: Int,
        @Query("liker_id") likerId: Int?
    ): Response<PostResponse>

    @GET("api/post/{id}")
    suspend fun getTopicUserPost(
        @Path("id") id: Int,
        @Query("current_page") currentPage: Int,
        @Query("liker_id") likerId: Int?,
        @Query("user_id") userId: Int?,

    ): Response<PostResponse>

    @POST("api/post")
    @FormUrlEncoded
    suspend fun doPost(
        @Header("Authorization") token: String,
        @Field("title") title: String?,
        @Field("details") details: String,
        @Field("image") image: String?,
        @Field("topic_id") topicId: Int,
    ): Response<CreatePostResponse>

    @DELETE("api/post/{id}")
    suspend fun deletePost(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
    ): Response<CreatePostResponse>

    @POST("api/reaction")
    @FormUrlEncoded
    suspend fun doLike(
        @Header("Authorization") token: String,
        @Field("post_id") postId: Int,
        @Field("reaction_type") reactionType: String,
    ): Response<ReactionResponse>

    @DELETE("api/reaction/{id}")
    suspend fun deleteLike(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
    ): Response<ReactionResponse>

    @GET("api/comment/{id}")
    suspend fun getComment(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
    ): Response<CommentResponse>

    @POST("api/comment")
    @FormUrlEncoded
    suspend fun doComment(
        @Header("Authorization") token: String,
        @Field("comment_text") commentText: String,
        @Field("post_id") postId: Int,
    ): Response<CreateCommentResponse>

    @GET("api/user/{id}")
    suspend fun getUserData(
        @Path("id") id: Int,
        @Query("follower_id") followerId: Int?
    ): Response<SingleUserResponse>

    @POST("api/relation")
    @FormUrlEncoded
    suspend fun doFollow(
        @Header("Authorization") token: String,
        @Field("user_id") userId: Int,
        @Field("relatable_type") relatableType: String,
    ): Response<UserRelationRespose>

    @DELETE("api/relation/{id}")
    suspend fun deleteRelation(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
    ): Response<UserRelationRespose>

    @GET("api/relation")
    suspend fun getRelation(
        @Header("Authorization") token: String,
        @Query("current_page") currentPage: Int,
        @Query("relatable_id") relatableId: Int,
        @Query("relatable_type") relatableType: String,
    ): Response<FollowerResponse>

    @GET("api/relation")
    suspend fun getFollowing(
        @Header("Authorization") token: String,
        @Query("current_page") currentPage: Int,
        @Query("relatable_id") relatableId: Int,
        @Query("relatable_type") relatableType: String,
    ): Response<FollowingResponse>

    @GET("api/topics")
    suspend fun getTopic(): Response<TopicResponse>

    @GET("api/search")
    suspend fun getSearchData(
        @Query("request_data") requestData: String,
        @Query("liker_id") likerId: Int?
    ): Response<SearchResponse>
}
