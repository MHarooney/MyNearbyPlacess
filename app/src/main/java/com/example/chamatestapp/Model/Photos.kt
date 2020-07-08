package com.example.chamatestapp.Model

class Photos {
    var photo_reference: String? = null
    var width: String? = null
    lateinit var html_attributions: Array<String>
    var height: String? = null

    override fun toString(): String {
        return "ClassPojo [photo_reference = $photo_reference, width = $width, html_attributions = $html_attributions, height = $height]"
    }
}