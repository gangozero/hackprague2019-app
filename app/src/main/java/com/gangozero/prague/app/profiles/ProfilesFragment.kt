package com.gangozero.prague.app.profiles

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gangozero.prague.app.R
import com.gangozero.prague.app.core.App
import com.gangozero.prague.app.core.Schedulers
import com.gangozero.prague.app.profiles.usecases.GetProfiles
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView


class ProfilesFragment : Fragment() {

    lateinit var mapView: MapView
    var map: GoogleMap? = null

    lateinit var viewModel: ProfilesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profiles, container, false)

        return view
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onStart() {
        mapView.onStart()
        super.onStart()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        mapView.onDestroy()
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView = view.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)

        val profilesService = (context!!.applicationContext as App).profilesService
        viewModel = ProfilesViewModel(
                GetProfiles(profilesService),
                (context!!.applicationContext as App).createSubmitGrade(),
                Schedulers()
        )

        view.findViewById<View>(R.id.btn_retry).setOnClickListener { loadProfiles(view) }

        loadProfiles(view)
    }

    private fun loadProfiles(view: View) {

        val app = context!!.applicationContext as App

        viewModel.profiles().subscribe {

            when (it) {
                is State.Loading -> {
                    view.findViewById<View>(R.id.data_view).visibility = View.GONE
                    view.findViewById<View>(R.id.error_container).visibility = View.GONE
                    view.findViewById<View>(R.id.progress_view).visibility = View.VISIBLE
                }
                is State.Error -> {
                    view.findViewById<View>(R.id.data_view).visibility = View.GONE
                    view.findViewById<View>(R.id.error_container).visibility = View.VISIBLE
                    view.findViewById<View>(R.id.progress_view).visibility = View.GONE
                }
                is State.Success -> {


                    app.selectedProfile = it.profiles[0]

                    view.findViewById<View>(R.id.error_container).visibility = View.GONE
                    view.findViewById<View>(R.id.data_view).visibility = View.VISIBLE
                    view.findViewById<View>(R.id.progress_view).visibility = View.GONE

                    val viewPager = view.findViewById<ViewPager>(R.id.view_pager)

                    viewPager.adapter = ProfilesPagerAdapter(it.profiles) { id, like ->

                        val locManager = app.locationManager

                        val lastLocation = locManager.lastLocation
                        if (lastLocation != null) {
                            viewModel.submitGrade(like)
                        }

                    }

                    viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                        override fun onPageScrollStateChanged(p0: Int) {

                        }

                        override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

                        }

                        override fun onPageSelected(page: Int) {
                            app.selectedProfile = it.profiles[page]
                        }

                    })

                    mapView.getMapAsync {
                        map = it
                        enableMyLocationIfPermitted()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted()
                } else {
                    //showDefaultLocation()
                }
                return
            }
        }
    }


    private fun enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                    1000)
        } else if (map != null) {
            (context!!.applicationContext as App).locationManager.init()
            map!!.isMyLocationEnabled = true
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfilesFragment()
    }
}
