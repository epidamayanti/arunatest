package damayanti.evi.arunates.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import damayanti.evi.arunates.R.layout.item_title
import damayanti.evi.arunates.models.DataContent
import kotlinx.android.synthetic.main.item_title.view.*

class ListTitleAdapter(private val context: Context, private val items: List<DataContent>, private val listener: (DataContent) -> Unit)
    : RecyclerView.Adapter<ListTitleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(context).inflate(item_title, parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    override fun getItemCount(): Int = items.size


    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindItem(items: DataContent, listener: (DataContent) -> Unit) {
            containerView.item_title.text = items.title
            containerView.setOnClickListener { listener(items) }
        }
    }

}

