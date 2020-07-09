package com.example.chamatestapp.Model

class Address_components {
    lateinit var types: Array<String>
    var short_name: String? = null
    var long_name: String? = null

    override fun toString(): String {
        return "ClassPojo [types = $types, short_name = $short_name, long_name = $long_name]"
    }
}