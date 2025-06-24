package com.example.simpleapirequest

object RetrofitInstance {
    private const val BASE_URL = "https://dummyjson.com"
    val apiService: ApiService by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}