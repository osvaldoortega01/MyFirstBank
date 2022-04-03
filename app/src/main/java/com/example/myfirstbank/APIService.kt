package com.example.myfirstbank

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

//Esta interfaz contine el método por el cual se accede al API
interface APIService {
    @GET
    suspend fun getLatestValues(@Url url: String): Response<ResponseMXN>
}