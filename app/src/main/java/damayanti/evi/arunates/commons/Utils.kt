package damayanti.evi.arunates.commons

import damayanti.evi.arunates.models.DataContent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class Utils {

    companion object {
        //view
        val LIST_FRAGMENT = "list"

        //endpoint API
        val ENDPOINT = "https://jsonplaceholder.typicode.com/"
        const val DATA_ENDPOINT = "posts"

        var data: MutableList<DataContent> = mutableListOf()


        //retrofit
        fun buildClient(): OkHttpClient.Builder {
            val clientBuilder = OkHttpClient.Builder()
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            clientBuilder.addInterceptor(loggingInterceptor)
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)

            return clientBuilder
        }
    }
}