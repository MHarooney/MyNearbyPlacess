package com.example.chamatestapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.chamatestapp.Model.PlaceDetail
import com.example.chamatestapp.Remote.IGoogleAPIService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : AppCompatActivity() {
    var photo: ImageView? = null
    var ratingBar: RatingBar? = null
    var openining_hours: TextView? = null
    var place_address: TextView? = null
    var place_name: TextView? = null
    var btnViewOnMap: Button? = null
    var btnViewDirections: Button? = null
    var mService: IGoogleAPIService? = null
    var mPlace: PlaceDetail? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_activity)
        val toolbar =
            findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        mService = Common.getGoogleAPIService()
        photo =
            findViewById<View>(R.id.photo) as ImageView
        ratingBar =
            findViewById<View>(R.id.ratingBar) as RatingBar
        place_address =
            findViewById<View>(R.id.place_addess) as TextView
        place_name =
            findViewById<View>(R.id.place_name) as TextView
        openining_hours =
            findViewById<View>(R.id.place_open_hour) as TextView
        btnViewOnMap =
            findViewById<View>(R.id.btn_show_map) as Button
        btnViewDirections =
            findViewById<View>(R.id.btn_view_directions) as Button

        //Empty all view
        place_name!!.text = ""
        place_address!!.text = ""
        openining_hours!!.text = ""
        btnViewOnMap!!.setOnClickListener {
            val mapIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(mPlace!!.result!!.url)
            )
            startActivity(mapIntent)
        }
        btnViewDirections!!.setOnClickListener {
            val mapIntent =
                Intent(this@DetailsActivity, ViewDirections::class.java)
            startActivity(mapIntent)
        }

        //Photo
        if (Common.currentResult.photos != null && Common.currentResult.photos.length > 0) {
            Picasso.with(this)
                .load(
                    getPhotoOfPlace(
                        Common.currentResult.photos[0].photo_reference,
                        1000
                    )
                )
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_error_24)
                .into(photo)
        }
        //Rating
        if (Common.currentResult.rating != null && !TextUtils.isEmpty(
                Common.currentResult.rating
            )
        ) {
            ratingBar!!.rating = Common.currentResult.rating!!.toFloat()
        } else {
            ratingBar!!.visibility = View.GONE
        }
        //Opening hours
        if (Common.currentResult.opening_hours != null) {
            openining_hours!!.text = "Open now: " + Common.currentResult.opening_hours!!.open_now
        } else {
            openining_hours!!.visibility = View.GONE
        }
        //User Service to fetch Address and Name
        mService.getDetailPlace(getPlaceDetailUrl(Common.currentResult.place_id))
            .enqueue(object : Callback<PlaceDetail?> {
                override fun onResponse(
                    call: Call<PlaceDetail?>,
                    response: Response<PlaceDetail?>
                ) {
                    mPlace = response.body()
                    place_address!!.text = mPlace!!.result!!.formatted_address
                    place_name!!.text = mPlace!!.result!!.name
                }

                override fun onFailure(
                    call: Call<PlaceDetail?>,
                    t: Throwable
                ) {
                }
            })
    }

    private fun getPlaceDetailUrl(place_id: String?): String {
        val url =
            StringBuilder("https://maps.googleapis.com/maps/api/place/details/json")
        url.append("?place_id=$place_id")
        url.append("&key=" + resources.getString(R.string.browser_key))
        return url.toString()
    }

    private fun getPhotoOfPlace(photo_reference: String?, maxWidth: Int): String {
        val url =
            StringBuilder("https://maps.googleapis.com/maps/api/place/photo")
        url.append("?maxwidth=$maxWidth")
        url.append("&photoreference=$photo_reference")
        url.append("&key=" + resources.getString(R.string.browser_key))
        return url.toString()
    }
}