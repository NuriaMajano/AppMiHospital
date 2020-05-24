package net.azarquiel.mihospital.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rowsanitarios.view.*
import net.azarquiel.mihospital.model.Sanitario

class SanitariosAdapter (val context: Context,
                         val layout: Int
) : RecyclerView.Adapter<SanitariosAdapter.ViewHolder>() {

    private var dataList: List<Sanitario> = emptyList()

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

    internal fun setSanitario(sanitarios: List<Sanitario>) {
        this.dataList = sanitarios
        notifyDataSetChanged()
    }


    class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {
        fun bind(dataItem: Sanitario){
            // itemview es el item de diseño
            // al que hay que poner los datos del objeto dataItem

                itemView.tvnombresanitariorowsanitario.text = dataItem.nombre
                itemView.tag = dataItem


        }

    }
}