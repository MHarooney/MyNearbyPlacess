package com.example.chamatestapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chamatestapp.Model.Place
import com.facebook.drawee.view.SimpleDraweeView
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
    }


}