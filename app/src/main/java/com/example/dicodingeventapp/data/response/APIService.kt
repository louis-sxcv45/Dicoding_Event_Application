package com.example.dicodingeventapp.data.response

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface  APIService {
    @GET ("events")
    fun getActiveEvents(
        @Query("active") active : Int
    ): Call<EventResponse>

    @GET ("events/{id}")
    fun getDetailEvent(
        @Path("id") id: String
    ): Call<ListEventsItem>
}