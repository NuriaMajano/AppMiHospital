package net.azarquiel.mihospital.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rowhabitacionlibre.view.*
import net.azarquiel.mihospital.model.Habitacion
class HabitacionLibreAdapter (val context: Context,
                              val layout: Int
) : RecyclerView.Adapter<HabitacionLibreAdapter.ViewHolder>() {

//    private lateinit var item: Habitacion
    private var dataList: List<Habitacion> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewlayout = layoutInflater.inflate(layout, parent, false)
        return ViewHolder(viewlayout, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = dataList[position]
//        if (!item.ocupada) holder.bind(item)

//        if (!item.ocupada) item  = dataList[position]
//        holder.bind(item)

//        OFICIAL
        val item = dataList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    internal fun setHabitacion(habitacioneslibres: List<Habitacion>) {
        this.dataList = habitacioneslibres
        notifyDataSetChanged()
    }


    class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {
        fun bind(dataItem: Habitacion){
            if (!dataItem.ocupada){
                itemView.tvcodhabrowhabitacionlibre.text = dataItem.nombrehabitacion
                itemView.tag = dataItem
            }
        }

    }
}