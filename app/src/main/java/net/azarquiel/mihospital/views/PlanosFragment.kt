package net.azarquiel.mihospital.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.github.chrisbanes.photoview.PhotoView
import net.azarquiel.mihospital.R

class PlanosFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var ivzoom: ImageView
    private lateinit var pvzoom: PhotoView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_planos, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinner: Spinner = view.findViewById(R.id.spinner)
        spinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
            activity!!.application,
            R.array.planos,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        ivzoom = view.findViewById(R.id.ivzoom)
        pvzoom = view.findViewById(R.id.pvzoom)
        ivzoom.setOnClickListener {
            pvzoom.visibility = View.VISIBLE
            ivzoom.visibility = View.GONE
        }
        pvzoom.setOnClickListener {
            pvzoom.visibility = View.GONE
            ivzoom.visibility = View.VISIBLE
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> {
                ivzoom.setImageResource(R.drawable.planogeneral)
                pvzoom.setImageResource(R.drawable.planogeneral)
            }
            1 -> {
                ivzoom.setImageResource(R.drawable.plantabaja)
                pvzoom.setImageResource(R.drawable.plantabaja)
            }
            2 -> {
                ivzoom.setImageResource(R.drawable.plantaprimera)
                pvzoom.setImageResource(R.drawable.plantaprimera)
            }
            3 -> {
                ivzoom.setImageResource(R.drawable.plantasegunda)
                pvzoom.setImageResource(R.drawable.plantasegunda)
            }
        }
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}
