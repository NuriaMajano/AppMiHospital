package net.azarquiel.mihospital.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rowagenda.view.*
import kotlinx.android.synthetic.main.rowpaciente.view.*
import net.azarquiel.mihospital.R
import net.azarquiel.mihospital.model.Agenda
import net.azarquiel.mihospital.model.Paciente

class AgendaAdapter (val context: Context,
                     val layout: Int
) : RecyclerView.Adapter<AgendaAdapter.ViewHolder>() {

    private var dataList: List<Agenda> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewlayout = layoutInflater.inflate(layout, parent, false)
        return ViewHolder(viewlayout, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    internal fun setAgenda(agendas: List<Agenda>) {
        this.dataList = agendas
        notifyDataSetChanged()
    }

    class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {
        fun bind(dataItem: Agenda, impar: Int){

            if (impar%2 != 0) {
                itemView.llv1.setBackgroundResource(R.color.colorFondoImpar1)
                itemView.llv2.setBackgroundResource(R.color.colorFondoImpar2)
            } else {
                itemView.llv1.setBackgroundResource(R.color.colorFondoPar1)
                itemView.llv2.setBackgroundResource(R.color.colorFondoPar2)
            }
            itemView.tvnombrerowagenda.text = dataItem.nombre
            itemView.tvdescripcionrowagenda.text = dataItem.descripcion
            itemView.tvhorarowagenda.text = dataItem.hora
            itemView.tag = dataItem
        }

    }
}