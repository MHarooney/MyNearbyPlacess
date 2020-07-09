package com.example.chamatestapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.chamatestapp.MapsActivity3
import com.example.chamatestapp.Model.MyPlaces
import com.example.chamatestapp.Remote.IGoogleAPIService
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity3 : FragmentActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private var latitude = 0.0
    private var longitude = 0.0
    private var mLastLocation: Location? = null
    private var mMarker: Marker? = null
    var mservice: IGoogleAPIService? = null
    var currentPlace: MyPlaces? = null

    //New Location
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var locationCallback: LocationCallback? = null
    private var mLocationRequest: LocationRequest? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps2)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        //Init Sevivice
        mservice = Common.getGoogleAPIService()

        //Request Runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
        }
        val bottomNavigationView =
            findViewById<View>(R.id.bnv) as BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item -> //Code late
            when (item.itemId) {
                R.id.action_cafes -> nearByPlace("cafe")
                R.id.action_bars -> nearByPlace("bar")
                R.id.action_restaurant -> nearByPlace("restaurant")
                else -> {
                }
            }
            true
        }

        //Init location
        buildLocationCallBack()
        buildLocationRequest()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }
    //Ctrl + O
    /**
     * Dispatch onStop() to all fragments.
     */
    override fun onStop() {
        fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
        super.onStop()
    }

    private fun buildLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 1000
        mLocationRequest!!.fastestInterval = 1000
        mLocationRequest!!.smallestDisplacement = 10f
        mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    private fun buildLocationCallBack() {
        locationCallback = object : LocationCallback() {
            //Ctrl + O
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                mLastLocation = locationResult.lastLocation
                if (mMarker != null) mMarker!!.remove()
                latitude = mLastLocation.getLatitude()
                longitude = mLastLocation.getLongitude()
                val latLng = LatLng(latitude, longitude)
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title("Your Position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                mMarker = mMap!!.addMarker(markerOptions)

                //Move Camera
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))
            }
        }
    }

    private fun nearByPlace(placeType: String) {
        mMap!!.clear()
        val url = getUrl(latitude, longitude, placeType)
        mservice!!.getNearByPlaces(url)
            .enqueue(object : Callback<MyPlaces> {
                override fun onResponse(
                    call: Call<MyPlaces>,
                    response: Response<MyPlaces>
                ) {
                    currentPlace = response.body() //Remember assign value for currentPlace
                    if (response.isSuccessful) {
                        for (i in 0 until response.body()!!.results.length) {
                            val markerOptions = MarkerOptions()
                            val googlePlace = response.body()!!.results[i]
                            val lat = googlePlace.geometry!!.location!!.lat!!.toDouble()
                            val lng = googlePlace.geometry!!.location!!.lng!!.toDouble()
                            val placeName = googlePlace.name
                            val vicinity = googlePlace.vicinity
                            val latLng = LatLng(lat, lng)
                            markerOptions.position(latLng)
                            markerOptions.title(placeName)
                            if (placeType == "cafe") markerOptions.icon(
                                BitmapDescriptorFactory.fromResource(
                                    R.drawable.cfe_amp_mrkr
                                )
                            ) else if (placeType == "bar") markerOptions.icon(
                                BitmapDescriptorFactory.fromResource(R.drawable.cups_mrkr)
                            ) else if (placeType == "restaurant") markerOptions.icon(
                                BitmapDescriptorFactory.fromResource(R.drawable.rest_mrkr_map)
                            ) else markerOptions.icon(
                                BitmapDescriptorFactory.defaultMarker(
                                    BitmapDescriptorFactory.HUE_RED
                                )
                            )
                            markerOptions.snippet(i.toString()) //Assign index for marker

                            //Add to map
                            mMap!!.addMarker(markerOptions)

                            //Move camera
                            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))
                        }
                    }
                }

                override fun onFailure(
                    call: Call<MyPlaces>,
                    t: Throwable
                ) {
                }
            })
    }

    private fun getUrl(
        latitude: Double,
        longitude: Double,
        placeType: String
    ): String {
        val googlePlacesUrl =
            StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
        googlePlacesUrl.append("location=$latitude,$longitude")
        googlePlacesUrl.append("&radius=" + 1500)
        googlePlacesUrl.append("&type=$placeType")
        //        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + resources.getString(R.string.browser_key))
        Log.d("getUrl", googlePlacesUrl.toString())
        return googlePlacesUrl.toString()
    }

    private fun checkLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), MY_PERMISSION_CODE
            ) else ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), MY_PERMISSION_CODE
            )
            false
        } else true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        mMap!!.isMyLocationEnabled = true
                        //Init location
                        buildLocationCallBack()
                        buildLocationRequest()
                        fusedLocationProviderClient =
                            LocationServices.getFusedLocationProviderClient(this)
                        fusedLocationProviderClient.requestLocationUpdates(
                            mLocationRequest,
                            locationCallback,
                            Looper.myLooper()
                        )
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Init Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            );
            run { mMap!!.isMyLocationEnabled = true }
        } else {
            mMap!!.isMyLocationEnabled = true
        }

        //Make event click on Marker
        mMap!!.setOnMarkerClickListener { marker ->
            if (marker.snippet != null) {
                //When user select marker ,just get Result of Place and assign to static variable
                Common.currentResult =
                    currentPlace!!.results[marker.snippet.toInt()]
                //Start new Activity
                startActivity(
                    Intent(
                        this@MapsActivity3,
                        DetailsActivity::class.java
                    )
                )
            }
            true
        }

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    companion object {
        private const val MY_PERMISSION_CODE = 1000
    }
}