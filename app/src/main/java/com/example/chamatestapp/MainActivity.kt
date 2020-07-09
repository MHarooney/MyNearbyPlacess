package com.example.chamatestapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chamatestapp.Model.MyPlaces
import com.example.chamatestapp.Remote.IGoogleAPIService
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var placesAdapter: PlacesAdapter
    private lateinit var sortListViewAdapter: SortAdapter
    private lateinit var sortList: List<String>
    private lateinit var sortListsItems: HashMap<String, List<String>>

    //New Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback
    lateinit var mLocationRequest: LocationRequest

    lateinit var context: Context

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Init Sevivice
        mservice = Common.getGoogleAPIService()

        BtnlocMapTxt.setOnClickListener {
            val intent = Intent(this, MapsActivity3::class.java)
            startActivity(intent)
        }

        showList()

        sortListViewAdapter = SortAdapter(this, sortList, sortListsItems)
        sortLv.setAdapter(sortListViewAdapter)

        placesAdapter = PlacesAdapter()
        placesListRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            adapter = placesAdapter
        }

        //Init location

        //Init location
        buildLocationCallBack()
        buildLocationRequest()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        fusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest,
            locationCallback,
            Looper.myLooper()
        )

        progressBar.visibility = View.VISIBLE
    }

    private fun showList() {
        sortList = ArrayList()
        sortListsItems = HashMap()

        (sortList as ArrayList<String>).add("Sort")

        val sortLists1: MutableList<String> = ArrayList()
        sortLists1.add("Sort By Top Rated")
        sortLists1.add("Sort By Low Rated")

        sortListsItems[sortList[0]] = sortLists1
    }

    private fun buildLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.smallestDisplacement = 10f
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }


    private var latitude = 0.0
    private var longitude = 0.0
    lateinit var mLastLocation: Location

    private fun buildLocationCallBack() {
        locationCallback = object : LocationCallback() {
            //Ctrl + O
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                mLastLocation = locationResult.lastLocation
                nearByPlace("cafe")
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                Log.d(TAG, "onLocationResult: ")
            }
        }
    }

    private val TAG = "MainActivity"

    lateinit var mservice: IGoogleAPIService

    private fun nearByPlace(placeType: String) {

        val url: String = getUrl(placeType)
        Log.d(TAG, "nearByPlace: " + url)
        mservice.getNearByPlaces(url).enqueue(object : Callback<MyPlaces> {
            override fun onResponse(
                call: Call<MyPlaces>,
                response: Response<MyPlaces>
            ) {
                if (response.isSuccessful) {
                    Log.d("eslamfaisal", "good")
                    response.body()?.let {
                        placesAdapter.insertPlaces(it.results)
                    }

                } else
                    Log.d("eslamfaisal", "" + response.errorBody().toString())

                progressBar.visibility = View.GONE
            }

            override fun onFailure(
                call: Call<MyPlaces>,
                t: Throwable
            ) {

                Log.d("eslamfaisal", "" + t.message)
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun getUrl(
        placeType: String
    ): String {
        val googlePlacesUrl =
            StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
        googlePlacesUrl.append("location=${mLastLocation.latitude},${mLastLocation.longitude}")
        googlePlacesUrl.append("&radius=" + 1500)
        googlePlacesUrl.append("&type=$placeType")
        //        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + resources.getString(R.string.browser_key))
        Log.d("getUrl", googlePlacesUrl.toString())
        return googlePlacesUrl.toString()
    }
    //Ctrl + O
    /**
     * Dispatch onStop() to all fragments.
     */
    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()
    }

    fun getCafe(view: View) {
        progressBar.visibility = View.VISIBLE
        nearByPlace("cafe")
    }

    fun getBars(view: View) {
        progressBar.visibility = View.VISIBLE
        nearByPlace("bar")
    }

    fun getRest(view: View) {
        progressBar.visibility = View.VISIBLE
        nearByPlace("restaurant")
    }

}