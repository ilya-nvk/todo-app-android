package com.ilyanvk.todoapp.data.retrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoItemApi {

    @GET("list")
    suspend fun getList(): Response<TodoItemApiResponseList>

    @PATCH("list")
    suspend fun updateTodoItemsList(
        @Header("X-Last-Known-Revision") revision: Int, @Body body: TodoItemApiRequestList
    ): Response<TodoItemApiResponseList>

    @GET("list/{id}")
    suspend fun getTodoItem(@Path("id") id: String): Response<TodoItemApiResponse>

    @POST("list/{id}")
    suspend fun addTodoItem(
        @Header("X-Last-Known-Revision") revision: Int, @Body body: TodoItemApiRequest
    ): Response<TodoItemApiResponse>

    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String,
        @Body body: TodoItemApiRequest
    ): Response<TodoItemApiResponse>

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Header("X-Last-Known-Revision") revision: Int, @Path("id") id: String
    ): Response<TodoItemApiResponse>
}
