package com.example.chamatestapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aradis.online.Models.Place
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var sortListViewAdapter: SortAdapter
    private lateinit var sortList : List<String>
    private lateinit var sortListsItems : HashMap<String, List<String>>

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BtnlocMapTxt.setOnClickListener {
            val intent = Intent(this,MapsActivity3::class.java)
            startActivity(intent)
        }

        showList()

        sortListViewAdapter = SortAdapter(this, sortList, sortListsItems)
        sortLv.setAdapter(sortListViewAdapter)


        val recyclerView = findViewById(R.id.recList) as RecyclerView


        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        val places = ArrayList<Place>()

        places.add(Place("Macdolands"))
        places.add(Place("Dominos"))
        places.add(Place("Kentaky"))
        places.add(Place("Todays"))
        places.add(Place("Chicky"))

        val adapter = CustomAdapter(places)

        recyclerView.adapter = adapter
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
}