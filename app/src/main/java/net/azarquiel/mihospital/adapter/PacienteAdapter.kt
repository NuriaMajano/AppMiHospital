package net.azarquiel.mihospital.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rowpaciente.view.*
import net.azarquiel.mihospital.model.Paciente

class PacienteAdapter (val context: Context,
                       val layout: Int
) : RecyclerView.Adapter<PacienteAdapter.ViewHolder>() {

    private var dataList: List<Paciente> = emptyList()

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

    internal fun setPaciente(pacientes: List<Paciente>) {
        this.dataList = pacientes
        notifyDataSetChanged()
    }


    class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {
        fun bind(dataItem: Paciente){
            // itemview es el item de diseño
            // al que hay que poner los datos del objeto dataItem
            itemView.tvnombrerowpaciente.text = dataItem.nombre
            itemView.tvnumsegsocialrowpaciente.text = dataItem.numsegsocial
            itemView.tvcodhabitacionrowpaciente.text = dataItem.codhab
            itemView.tvestadorowpaciente.text = dataItem.estadopaciente
            itemView.tag = dataItem
        }

    }
}