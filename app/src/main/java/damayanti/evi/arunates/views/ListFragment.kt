package damayanti.evi.arunates.views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import damayanti.evi.arunates.R
import damayanti.evi.arunates.adapter.ListTitleAdapter
import damayanti.evi.arunates.commons.LoadingAlert
import damayanti.evi.arunates.commons.RxBaseFragment
import damayanti.evi.arunates.commons.Utils
import damayanti.evi.arunates.database.ContentRoomDatabase
import damayanti.evi.arunates.models.DataContent
import damayanti.evi.arunates.services.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content.view.*
import kotlinx.android.synthetic.main.dialog_no_internet.view.*
import kotlinx.android.synthetic.main.dialog_no_internet.view.title
import kotlinx.android.synthetic.main.fragment_list.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Suppress("DEPRECATION", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ListFragment : RxBaseFragment() , SwipeRefreshLayout.OnRefreshListener{

    private var loading: Dialog? = null
    private lateinit var db: ContentRoomDatabase
    private var data: MutableList<DataContent> = mutableListOf()
    private lateinit var searchView : SearchView
    private lateinit var queryTextListener: SearchView.OnQueryTextListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading = LoadingAlert.progressDialog(this.context!!, this.activity!!)
        db = ContentRoomDatabase.getDatabase(this.requireContext())
        refresh.setOnRefreshListener(this)

        if(db.contentDao().getAllData().isEmpty()){
            initData()
        }
        else{
            data = db.contentDao().getAllData()
            viewData(data)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
        queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("onQueryTextChange", newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                data.clear()

                data = db.contentDao().findWithTitle("%$query%")
                viewData(data)

                if(data.isEmpty()){
                    val mDialogView = LayoutInflater.from(context).inflate(
                        R.layout.dialog_warning,
                        null
                    )
                    val mBuilder = AlertDialog.Builder(context)
                        .setView(mDialogView)

                    val mAlertDialog = mBuilder.setCancelable(false).show()

                    mDialogView.bt_close.setOnClickListener {
                        mAlertDialog.dismiss()
                    }
                }
                Log.d("onQueryTextChange", "$data $query")

                return true
            }
        }
        searchView.setOnQueryTextListener(queryTextListener)
    }

    private fun viewData(data: MutableList<DataContent>){
        list_data.layoutManager = LinearLayoutManager(this.context)
        list_data.adapter = ListTitleAdapter(this.context!!, data) {

            val mDialogView = LayoutInflater.from(context).inflate(
                R.layout.content,
                null
            )
            val mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)

            val mAlertDialog = mBuilder.setCancelable(false).show()
            mDialogView.title.text = it.title
            mDialogView.body.text = it.body

            mDialogView.imgClose.setOnClickListener {
                mAlertDialog.dismiss()
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun initData(){
        loading?.show()

        subscriptions.add(provideService()
            .getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ resp ->
                loading?.dismiss()
                for (item in resp) {
                    db.contentDao().insert(item)
                }

                data = db.contentDao().getAllData()
                viewData(data)

            }) { err ->
                loading?.dismiss()
                if (err.localizedMessage.contains("resolve host")) {
                    val mDialogView = LayoutInflater.from(context).inflate(
                        R.layout.dialog_no_internet,
                        null
                    )
                    val mBuilder = AlertDialog.Builder(context)
                        .setView(mDialogView)

                    val mAlertDialog = mBuilder.setCancelable(false).show()

                    mDialogView.bt_close.setOnClickListener {
                        mAlertDialog.dismiss()
                        initData()
                    }

                } else {

                    val mDialogView = LayoutInflater.from(context).inflate(
                        R.layout.dialog_warning,
                        null
                    )
                    val mBuilder = AlertDialog.Builder(context)
                        .setView(mDialogView)

                    val mAlertDialog = mBuilder.setCancelable(false).show()

                    mDialogView.bt_close.setOnClickListener {
                        mAlertDialog.dismiss()
                        initData()
                    }

                    mDialogView.title.text = "FAILED TO GET DATA! "

                    mDialogView.content.text = err.localizedMessage

                }
            }
        )
    }


    private fun provideService(): Service {
        val clientBuilder: OkHttpClient.Builder = Utils.buildClient()
        val retrofit = Retrofit.Builder()
            .baseUrl(Utils.ENDPOINT)
            .client(
                clientBuilder
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
        return retrofit.create(Service::class.java)
    }

    override fun onRefresh() {
        refresh.isRefreshing = false
        data = db.contentDao().getAllData()
        viewData(data)
    }
}