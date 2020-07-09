package com.example.chamatestapp.Model

class PlaceDetail {
    var result: Result? = null
    var html_attributions: Array<String>
    var status: String? = null

    override fun toString(): String {
        return "ClassPojo [result = $result, html_attributions = $html_attributions, status = $status]"
    }
}