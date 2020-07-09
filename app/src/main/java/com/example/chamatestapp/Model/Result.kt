package com.example.chamatestapp.Model

class Result {
    var utc_offset: String? = null
    var formatted_address: String? = null
    lateinit var types: Array<String>
    var website: String? = null
    var icon: String? = null
    var rating: String? = null
    lateinit var address_components: Array<Address_components>
    var url: String? = null
    var reference: String? = null
    lateinit var reviews: Array<Reviews>
    var name: String? = null
    var geometry: Geometry? = null
    var vicinity: String? = null
    var id: String? = null
    var adr_address: String? = null
    var formatted_phone_number: String? = null
    var international_phone_number: String? = null
    var place_id: String? = null

    override fun toString(): String {
        return "ClassPojo [utc_offset = $utc_offset, formatted_address = $formatted_address, types = $types, website = $website, icon = $icon, rating = $rating, address_components = $address_components, url = $url, reference = $reference, reviews = $reviews, name = $name, geometry = $geometry, vicinity = $vicinity, id = $id, adr_address = $adr_address, formatted_phone_number = $formatted_phone_number, international_phone_number = $international_phone_number, place_id = $place_id]"
    }
}