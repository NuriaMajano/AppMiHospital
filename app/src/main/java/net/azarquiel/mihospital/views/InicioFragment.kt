package net.azarquiel.mihospital.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

import net.azarquiel.mihospital.R
import net.azarquiel.mihospital.adapter.ViewPageAdapter

class InicioFragment : Fragment() {

    private val icons = arrayOf(R.drawable.event ,R.drawable.face)
    private lateinit var vp: ViewPager
    private lateinit var tabs: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_inicio, container, false)
        vp = rootView.findViewById(R.id.view_pager) as ViewPager
        tabs = rootView.findViewById(R.id.tabs) as TabLayout
        setupViewPager(vp)
        setupTabs()
        return rootView

    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPageAdapter(activity!!.baseContext, childFragmentManager)
        adapter.addFragment(AgendaFragment(), getString(R.string.tituloagenda))
        adapter.addFragment(SanitariosFragment(), getString(R.string.titulosanitarios))
        viewPager.adapter = adapter
    }

    private fun setupTabs() {
        tabs.setupWithViewPager(vp)
        for ((i, icon) in icons.withIndex()) {
            tabs.getTabAt(i)!!.icon = ContextCompat.getDrawable(activity!!.baseContext, icon)
        }
    }

}
