package com.hazz.aipick.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ResetService {
    @GET
    fun get(@Url url: String, @QueryMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST
    fun post(@Url url: String, @FieldMap params: Map<String, String>): Call<ResponseBody>

    @Streaming
    @GET
    fun down(@Url url: String): Call<ResponseBody>
}