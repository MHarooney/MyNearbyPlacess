package com.example.chamatestapp.Helper

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class DirectionJSONParser {
    // ---------------------------------------------------------------------------------------------
    // Method that takes a JSONObject with information about the route, and returns a complex List -
    // that contains all required information for plotting the route on the map, and not only. -----
    fun parseFeed(jObject: JSONObject): List<List<HashMap<String, String>>> {
        val routes: MutableList<List<HashMap<String, String>>> =
            ArrayList()
        val routesJSONArray: JSONArray
        var legsJSONArray: JSONArray
        var stepsJSONArray: JSONArray
        var distanceJSONObject: JSONObject
        var durationJSONObject: JSONObject
        try {
            routesJSONArray = jObject.getJSONArray("routes")
            for (i in 0 until routesJSONArray.length()) {
                val path: MutableList<HashMap<String, String>> =
                    ArrayList()
                legsJSONArray = (routesJSONArray[i] as JSONObject).getJSONArray("legs")
                for (j in 0 until legsJSONArray.length()) {
                    distanceJSONObject =
                        (legsJSONArray[j] as JSONObject).getJSONObject("distance")
                    val distanceHashMap =
                        HashMap<String, String>()
                    distanceHashMap["distance"] = distanceJSONObject.getString("text")
                    path.add(distanceHashMap)
                    durationJSONObject =
                        (legsJSONArray[j] as JSONObject).getJSONObject("duration")
                    val durationHashMap =
                        HashMap<String, String>()
                    durationHashMap["duration"] = durationJSONObject.getString("text")
                    path.add(durationHashMap)
                    stepsJSONArray =
                        (legsJSONArray[j] as JSONObject).getJSONArray("steps")
                    for (k in 0 until stepsJSONArray.length()) {
                        var tempPolyline: String
                        tempPolyline =
                            ((stepsJSONArray[k] as JSONObject)["polyline"] as JSONObject)["points"] as String
                        val decodedList =
                            decodePolyline(tempPolyline)
                        for (l in decodedList.indices) {
                            val hashMap =
                                HashMap<String, String>()
                            hashMap["lat"] = java.lang.Double.toString(decodedList[l].latitude)
                            hashMap["lng"] = java.lang.Double.toString(decodedList[l].longitude)
                            path.add(hashMap)
                        }
                    }
                }
                routes.add(path)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (ignored: Exception) {
        }
        Log.d("DirectionsJSONParser", "routes have been specified successfully")
        return routes
    }

    // ---------------------------------------------------------------------------------------------
    // Method that decodes the encoded polyline that is returned from Google Directions API. -------
    private fun decodePolyline(encodedPolyline: String): List<LatLng> {
        val decodedPolyline: MutableList<LatLng> = ArrayList()
        val encodedPolylineLength = encodedPolyline.length
        var tempLat = 0
        var tempLng = 0
        var tempLatLng: LatLng
        var index = 0
        var shift: Int
        var result: Int
        while (index < encodedPolylineLength) {
            var tempInt: Int
            shift = 0
            result = 0
            do {
                tempInt = encodedPolyline[index++].toInt() - 63
                result = result or (tempInt and 0x1F shl shift)
                shift += 5
            } while (tempInt >= 0x20)
            val dLat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            tempLat += dLat
            shift = 0
            result = 0
            do {
                tempInt = encodedPolyline[index++].toInt() - 63
                result = result or (tempInt and 0x1F shl shift)
                shift += 5
            } while (tempInt >= 0x20)
            val dLng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            tempLng += dLng
            tempLatLng =
                LatLng(tempLat.toDouble() / 1E5, tempLng.toDouble() / 1E5)
            decodedPolyline.add(tempLatLng)
        }
        return decodedPolyline
    }
}