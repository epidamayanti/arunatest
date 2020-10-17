package damayanti.evi.arunates

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import damayanti.evi.arunates.commons.BaseActivity
import damayanti.evi.arunates.commons.LoadingAlert
import damayanti.evi.arunates.commons.Utils
import damayanti.evi.arunates.services.Service
import damayanti.evi.arunates.views.ListFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity  : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        changeFragment(ListFragment(), false, Utils.LIST_FRAGMENT)

    }





}