package net.azarquiel.mihospital.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rowcitas.view.*
import net.azarquiel.mihospital.model.Citas

class CitasAdapter (val context: Context,
                    val layout: Int
) : RecyclerView.Adapter<CitasAdapter.ViewHolder>() {

    private var dataList: List<Citas> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewlayout = layoutInflater.inflate(layout, parent, false)
        return ViewHolder(viewlayout, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    internal fun setCita(citas: List<Citas>) {
        this.dataList = citas
        notifyDataSetChanged()
    }


    class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {
        fun bind(dataItem: Citas){
            itemView.tvnombrerowcita.text = dataItem.respuestas[3]
            itemView.tvnumsegsocialrowcita.text = dataItem.respuestas[0]
            itemView.tag = dataItem
        }

    }
}