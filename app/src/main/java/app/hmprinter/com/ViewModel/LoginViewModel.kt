package app.hmprinter.com.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import app.hmprinter.com.Interfaces.CustomCallbacks
import app.hmprinter.com.Models.RestaurantResponse
import app.hmprinter.com.Repository.LoginRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = LoginViewModel::class.java.name
    var restaurantData: MutableLiveData<RestaurantResponse> =
        MutableLiveData<RestaurantResponse>()
    val defaultRestaurantData: RestaurantResponse = RestaurantResponse()
    private var mRepository: LoginRepository =
        LoginRepository(application.applicationContext).getInstance()!!

    fun login(storeCode: String?, restaurantEmail: String?) {
        mRepository.login(storeCode, restaurantEmail, object : CustomCallbacks {
            override fun onSuccess(value: RestaurantResponse) {
                Log.d(TAG, "onSuccess - $value")
                restaurantData.postValue(value)
            }

            override fun onFailure(error: Throwable) {
                Log.d(TAG, "onFailure - ${error.message.toString()}")
                restaurantData.postValue(defaultRestaurantData)
            }
        })

    }


}