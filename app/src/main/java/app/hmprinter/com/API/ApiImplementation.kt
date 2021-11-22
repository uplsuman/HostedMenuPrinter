package app.hmprinter.com.API

import android.content.Context
import app.hmprinter.com.Models.RestaurantResponse
import app.hmprinter.com.Utility.NoConnectivityException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ApiImplementation {
    private val TAG = ApiImplementation::class.java.name

    fun getRestaurant(context: Context) {
        val apiService: ApiInterface =
            ApiClient.getHMAppClient(context)!!.create(ApiInterface::class.java)
        // TODO: get restaurant
        val call: Call<RestaurantResponse> =
            apiService.getRestaurant("", "")
        call.enqueue(object : Callback<RestaurantResponse?> {
            override fun onResponse(
                call: Call<RestaurantResponse?>?,
                response: Response<RestaurantResponse?>
            ) {
                if (response.body() != null) {
                    val restaurantResponse: RestaurantResponse = response.body()!!

                }
            }

            override fun onFailure(call: Call<RestaurantResponse?>?, t: Throwable) {
                if (t is NoConnectivityException) {

                } else {

                }

            }
        })
    }
}