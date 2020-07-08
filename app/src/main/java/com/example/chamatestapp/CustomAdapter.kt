package com.example.chamatestapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aradis.online.Models.Place

class CustomAdapter(val placeList: ArrayList<Place>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place: Place = placeList[position]

        holder?.textViewName.text = place.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val textViewName = itemView.findViewById(R.id.titleTxt) as TextView
    }

}