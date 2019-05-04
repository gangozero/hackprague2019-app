package com.gangozero.prague.app.core

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gangozero.prague.app.R
import com.gangozero.prague.app.profiles.ProfilesFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction().add(
                    R.id.root_container,
                    ProfilesFragment.newInstance()
            ).commit()
        }
    }
}
