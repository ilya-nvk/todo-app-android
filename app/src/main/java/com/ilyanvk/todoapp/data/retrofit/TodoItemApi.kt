package com.ilyanvk.todoapp.data.retrofit

import com.ilyanvk.todoapp.data.retrofit.models.TodoItemApiRequest
import com.ilyanvk.todoapp.data.retrofit.models.TodoItemApiRequestList
import com.ilyanvk.todoapp.data.retrofit.models.TodoItemApiResponse
import com.ilyanvk.todoapp.data.retrofit.models.TodoItemApiResponseList
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
        @Header(HEADER) revision: Int, @Body body: TodoItemApiRequestList
    ): Response<TodoItemApiResponseList>

    @POST("list")
    suspend fun addTodoItem(
        @Header(HEADER) revision: Int, @Body body: TodoItemApiRequest
    ): Response<TodoItemApiResponse>

    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Header(HEADER) revision: Int,
        @Path("id") id: String,
        @Body body: TodoItemApiRequest
    ): Response<TodoItemApiResponse>

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Header(HEADER) revision: Int, @Path("id") id: String
    ): Response<TodoItemApiResponse>

    companion object {
        const val HEADER = "X-Last-Known-Revision"
    }
}
