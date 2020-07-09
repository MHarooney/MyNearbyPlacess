package com.example.chamatestapp.Model

class MyPlaces(
    var next_page_token: String? = null,
    var html_attributions: Array<String>,
    var results: Array<Place>,
    var status: String? = null
) {


    override fun toString(): String {
        return "ClassPojo [next_page_token = $next_page_token, html_attributions = $html_attributions, results = $results, status = $status]"
    }
}