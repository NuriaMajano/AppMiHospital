package net.azarquiel.mihospital.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rowmaterialsanitario.view.*
import net.azarquiel.mihospital.R
import net.azarquiel.mihospital.model.InterfazAdapter
import net.azarquiel.mihospital.model.MaterialSanitario

class MaterialSanitarioAdapter (val context: Context,
                                val layout: Int,
                                val interfazAdapter: InterfazAdapter
) : RecyclerView.Adapter<MaterialSanitarioAdapter.ViewHolder>() {

    private var dataList: List<MaterialSanitario> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewlayout = layoutInflater.inflate(layout, parent, false)
        return ViewHolder(viewlayout, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
        holder.itemView.btnmas.setOnClickListener { interfazAdapter.onClickMas(item) }
        holder.itemView.btnmenos.setOnClickListener { interfazAdapter.onClickMenos(item) }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    internal fun setMaterialSanitario(materialesSanitarios: List<MaterialSanitario>) {
        this.dataList = materialesSanitarios
        notifyDataSetChanged()
    }

    class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {
        fun bind(dataItem: MaterialSanitario){
            itemView.tvnombrematerialrowmaterialsanitario.text = dataItem.nombre
            itemView.tvcantidadrowmaterial.text = dataItem.cantidad.toString()
            itemView.tag = dataItem
        }
    }

}