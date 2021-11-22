package app.hmprinter.com.Repository

import android.content.Context
import android.util.Log
import app.hmprinter.com.API.ApiImplementation
import app.hmprinter.com.Helpers.DataStoreManager
import app.hmprinter.com.Interfaces.CustomCallbacks
import app.hmprinter.com.Models.RestaurantResponse


class LoginRepository(private val context: Context) {
    private val TAG = LoginRepository::class.java.name
    private var instance: LoginRepository? = null
    private var dataStoreManager: DataStoreManager = DataStoreManager(context)
    fun getInstance(): LoginRepository? {

        if (instance == null) {
            instance = LoginRepository(context)
        }
        return instance
    }

     fun login(storeCode: String?, restaurantEmail: String?, callbacks: CustomCallbacks) {
        Log.d(TAG, "----Login----")
        if (storeCode == null && restaurantEmail == null) {
         //Check if isLoggedIn from data store & return restaurant data

            return
        }

        ApiImplementation.getRestaurant(
            context,
            storeCode!!,
            restaurantEmail!!,
            object : CustomCallbacks {
                override fun onSuccess(value: RestaurantResponse) {
                    callbacks.onSuccess(value)

                }

                override fun onFailure(error: Throwable) {
                    callbacks.onFailure(error)
                }


            })

    }

}