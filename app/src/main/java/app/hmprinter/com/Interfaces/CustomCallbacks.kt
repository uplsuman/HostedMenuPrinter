package app.hmprinter.com.Interfaces

import app.hmprinter.com.Models.RestaurantResponse

interface CustomCallbacks {
    fun onSuccess(value: RestaurantResponse)
    fun onFailure(error: Throwable)
}
