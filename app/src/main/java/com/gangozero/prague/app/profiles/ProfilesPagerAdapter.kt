package com.gangozero.prague.app.profiles

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gangozero.prague.app.R


class ProfilesPagerAdapter(profiles: List<Profile>) : PagerAdapter() {

    val profiles = profiles.toMutableList()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val profile = profiles[position]

        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val page = LayoutInflater.from(container.context).inflate(R.layout.page_profile, container, false)


        val textProfileName = page.findViewById<TextView>(R.id.text_profile_name)

        textProfileName.text = profile.name

        container.addView(page, params)

        return page
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun getCount(): Int {
        return profiles.size
    }
}