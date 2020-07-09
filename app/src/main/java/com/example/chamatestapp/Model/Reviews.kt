package com.example.chamatestapp.Model

class Reviews {
    var author_name: String? = null
    var profile_photo_url: String? = null
    var author_url: String? = null
    var rating: String? = null
    var language: String? = null
    var text: String? = null
    var time: String? = null
    var relative_time_description: String? = null

    override fun toString(): String {
        return "ClassPojo [author_name = $author_name, profile_photo_url = $profile_photo_url, author_url = $author_url, rating = $rating, language = $language, text = $text, time = $time, relative_time_description = $relative_time_description]"
    }
}