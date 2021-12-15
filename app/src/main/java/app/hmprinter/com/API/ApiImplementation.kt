package app.hmprinter.com.API

import android.content.Context
import android.util.Log
import app.hmprinter.com.Constants.AppConstant
import app.hmprinter.com.Interfaces.CustomCallbacks
import app.hmprinter.com.Models.RestaurantResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object ApiImplementation {
    private val TAG = ApiImplementation::class.java.name

    fun getRestaurant(
        context: Context,
        storeCode: String,
        restaurantEmail: String,
        customCallback: CustomCallbacks
    ) {
        val apiService: ApiInterface =
            ApiClient.getHMAppClient(context)!!.create(ApiInterface::class.java)

        val call: Call<RestaurantResponse> =
            apiService.getRestaurant(storeCode, restaurantEmail)
        call.enqueue(object : Callback<RestaurantResponse?> {
            override fun onResponse(
                call: Call<RestaurantResponse?>?,
                response: Response<RestaurantResponse?>
            ) {
                if (response.body() != null) {
                    val restaurantResponse: RestaurantResponse = response.body()!!
                    customCallback.onSuccess(restaurantResponse)
                } else {
                    customCallback.onFailure(Throwable(AppConstant.INVALID_INPUT))
                }
            }

            override fun onFailure(call: Call<RestaurantResponse?>?, t: Throwable) {
                customCallback.onFailure(t)

            }
        })
    }
}