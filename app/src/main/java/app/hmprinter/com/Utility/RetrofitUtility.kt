package app.hmprinter.com.Utility

import android.content.Context
import app.hmprinter.com.API.ApiClient
import app.hmprinter.com.API.ApiInterface
import app.hmprinter.com.Models.RestaurantResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RetrofitUtility {
    private val TAG = RetrofitUtility::class.java.name

    fun getSurvey(context: Context, serialNum: String?) {

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