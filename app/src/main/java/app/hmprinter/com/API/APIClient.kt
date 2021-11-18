package app.hmprinter.com.API


import android.content.Context

import app.hmprinter.com.Utility.NetworkConnectionInterceptor
import app.hmprinter.com.BuildConfig
import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiClient {
    val BASE_URL: String = BuildConfig.HM_PRINTER_BASE_URL

    private var retrofit: Retrofit? = null
    fun getHMAppClient(context: Context?): Retrofit? {
        if (retrofit == null) {
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(NetworkConnectionInterceptor(context!!))
            httpClient.readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
        }
        return retrofit
    }

}