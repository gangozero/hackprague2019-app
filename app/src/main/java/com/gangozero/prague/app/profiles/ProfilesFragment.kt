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
import com.gangozero.prague.app.core.Schedulers
import com.gangozero.prague.app.profiles.usecases.GetProfiles
import com.gangozero.prague.app.profiles.usecases.SubmitGrade
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView


class ProfilesFragment : Fragment() {

    lateinit var mapView: MapView
    var map: GoogleMap? = null

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

        val profilesService = ProfilesService()
        val viewModel = ProfilesViewModel(GetProfiles(profilesService), SubmitGrade(profilesService), Schedulers())
        viewModel.profiles().subscribe {

            if (it is State.Loading) {
                view.findViewById<View>(R.id.data_view).visibility = View.GONE
                view.findViewById<View>(R.id.progress_view).visibility = View.VISIBLE
            } else if (it is State.Error) {
                view.findViewById<View>(R.id.data_view).visibility = View.GONE
                view.findViewById<View>(R.id.progress_view).visibility = View.GONE
            } else if (it is State.Success) {

                view.findViewById<View>(R.id.data_view).visibility = View.VISIBLE
                view.findViewById<View>(R.id.progress_view).visibility = View.GONE

                val viewPager = view.findViewById<ViewPager>(R.id.view_pager)

                viewPager.adapter = ProfilesPagerAdapter(it.profiles) { id, like ->
                    viewModel.submitGrade(id, like)
                }

                mapView.getMapAsync {
                    map = it
                    enableMyLocationIfPermitted()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
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
        if (ContextCompat.checkSelfPermission(context!!,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                    1000)
        } else if (map != null) {
            map!!.isMyLocationEnabled = true
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfilesFragment()
    }
}
