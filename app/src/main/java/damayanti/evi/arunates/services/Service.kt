package damayanti.evi.arunates.services

import damayanti.evi.arunates.commons.Utils
import damayanti.evi.arunates.models.DataContent
import io.reactivex.Observable
import retrofit2.http.*

interface Service {
    @GET(Utils.DATA_ENDPOINT)
    fun getData(): Observable<MutableList<DataContent>>

}