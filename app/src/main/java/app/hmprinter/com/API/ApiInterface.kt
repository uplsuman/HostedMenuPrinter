package app.hmprinter.com.API

import app.hmprinter.com.Constants.ApiConstant
import app.hmprinter.com.Models.RestaurantResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET(ApiConstant.getByStoreCode)
    fun getRestaurant(@Query("storeCode") storeCode: String,
        @Query("restaurantEmail",encoded = true) restaurantEmail: String
    ): Call<RestaurantResponse>

}