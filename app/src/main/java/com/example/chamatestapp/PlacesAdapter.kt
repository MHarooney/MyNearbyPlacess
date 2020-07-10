package com.example.chamatestapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.chamatestapp.Model.Place
import com.facebook.drawee.view.SimpleDraweeView
import com.mikhaellopez.circularimageview.CircularImageView
import java.util.*

class PlacesAdapter : RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    var placeList: MutableList<Place> = ArrayList()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place: Place = placeList[position]

        holder.textViewName.text = place.name
        place.photos?.get(0)?.photo_reference?.let {
            val url =
                "https://maps.googleapis.com/maps/api/place/photo?photoreference=${place.photos!![0].photo_reference}&maxwidth=600&key=AIzaSyAEHwUHw-U0Rod4biLUMdW9STgQarz4oL0"
            holder.placeImage.setImageURI(url)

        }

        holder.itemView.setOnClickListener {
            //When user select marker ,just get Result of Place and assign to static variable
            Common.currentResult = place
            //Start new Activity
            //Start new Activity
            holder.itemView.context.startActivity(Intent(holder.itemView.context, DetailsActivity::class.java))
            Toast.makeText(holder.itemView.context,"هذة ضغطة جميلة من هذا الايتم الذى اسمه "+place.name,Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return placeList.size

    }

    fun insertPlaces(body: Array<Place>) {
        placeList.clear()
        body.let {
            placeList.addAll(it)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName = itemView.findViewById(R.id.titleTxt) as TextView
        val placeImage = itemView.findViewById(R.id.placeImage) as SimpleDraweeView
        val place_logo = itemView.findViewById(R.id.place_logo) as CircularImageView

//        init {
//            itemView.setOnClickListener {
//                if (marker.getSnippet() != null) {
//                    //When user select marker ,just get Result of Place and assign to static variable
//                    Common.currentResult =
//                        currentPlace.results.get(marker.getSnippet().toInt())
//                    //Start new Activity
//                    startActivity(
//                        Intent(
//                            this,
//                            DetailsActivity::class.javaSSS
//                        )
//                    )
//                }
//            }
//        }
    }


}