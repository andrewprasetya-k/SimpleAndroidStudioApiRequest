package com.example.simpleapirequest

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/users")
    suspend fun getUsers(): UserResponse

    @GET("/users/{id}")
    suspend fun getUser(@Path("id") userId: Int): User

    @GET("/products")
    suspend fun getProducts(): ProdukResponse
}