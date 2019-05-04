package com.gangozero.prague.app.profiles

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gangozero.prague.app.R
import com.gangozero.prague.app.profiles.dummy.DummyContent

class ProfilesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_theme_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = ProfilesAdapter(DummyContent.ITEMS)
            }
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfilesFragment()
    }
}
