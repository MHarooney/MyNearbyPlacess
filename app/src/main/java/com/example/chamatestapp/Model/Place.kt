package com.example.chamatestapp.Model

class Place {
    lateinit var types: Array<String>
    var business_status: String? = null
    var icon: String? = null
    var rating: String? = null
    lateinit var photos: Array<Photos>
    var reference: String? = null
    var user_ratings_total: String? = null
    var price_level: String? = null
    var scope: String? = null
    var name: String? = null
    var opening_hours: Opening_hours? = null
    var geometry: Geometry? = null
    var vicinity: String? = null
    var id: String? = null
    var plus_code: Plus_code? = null
    var place_id: String? = null

    override fun toString(): String {
        return "ClassPojo [types = $types, business_status = $business_status, icon = $icon, rating = $rating, photos = $photos, reference = $reference, user_ratings_total = $user_ratings_total, price_level = $price_level, scope = $scope, name = $name, opening_hours = $opening_hours, geometry = $geometry, vicinity = $vicinity, id = $id, plus_code = $plus_code, place_id = $place_id]"
    }
}