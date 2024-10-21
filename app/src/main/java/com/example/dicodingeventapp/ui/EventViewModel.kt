package com.example.dicodingeventapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingeventapp.data.response.EventResponse
import com.example.dicodingeventapp.data.response.ListEventsItem
import com.example.dicodingeventapp.data.retrofit.APIConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EventViewModel : ViewModel() {

    private val _listUpComingEvent = MutableLiveData<List<ListEventsItem>>()
    val upComingEvent : LiveData<List<ListEventsItem>> get() = _listUpComingEvent

    private val _listPastEvent = MutableLiveData<List<ListEventsItem>>()
    val finishedEvent : LiveData<List<ListEventsItem>> get() = _listPastEvent

    private val _eventDetail = MutableLiveData<ListEventsItem?>()
    val eventDetail : LiveData<ListEventsItem?> get() = _eventDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> get() = _isLoading

    companion object {
        const val TAG = "EventViewModel"
    }

    fun upComingEvents(){
        _isLoading.value = true

        val client = APIConfig.getAPIService().getActiveEvents(1)
        client.enqueue(object: Callback<EventResponse> {
            override fun onResponse(
                call : Call<EventResponse>,
                response : Response<EventResponse>
            ){
                _isLoading.value = false
                if(response.isSuccessful){
                    val eventResponse = response.body()
                    if(eventResponse != null){
                        _listUpComingEvent.value = eventResponse.listEvents
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun pastEvents(){
        _isLoading.value = true

        val client = APIConfig.getAPIService().getActiveEvents(0)
        client.enqueue(object: Callback<EventResponse> {
            override fun onResponse(
                call : Call<EventResponse>,
                response : Response<EventResponse>
            ){
                _isLoading.value = false
                if(response.isSuccessful){
                    val eventResponse = response.body()
                    if(eventResponse != null){
                        _listPastEvent.value = eventResponse.listEvents
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun eventsDetail(eventDetail: ListEventsItem?) {
        _isLoading.value = true
        val client = APIConfig.getAPIService().getDetailEvent(eventDetail.toString())
        client.enqueue(object : Callback<ListEventsItem> {
            override fun onResponse(call: Call<ListEventsItem>, response: Response<ListEventsItem>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val eventDetail = response.body()
                    if (eventDetail != null) {
                        _eventDetail.value = eventDetail
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ListEventsItem>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "Network Failure: ${t.message}")
            }
        })
    }
}

