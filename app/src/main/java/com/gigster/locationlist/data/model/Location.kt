package com.gigster.locationlist.data.model

data class Location(
    val latitude: Double,
    val longitude: Double
) {
    var distance: Int = 0
        get() {
            if (field == 0) {
                calcDistance()
            }
            return field
        }
        private set

    private fun calcDistance() {
        var startPoint = android.location.Location("provider")
        startPoint.latitude = 41.870275
        startPoint.longitude = -71.584628

        var nativeLocation = android.location.Location("provider")
        nativeLocation.latitude = latitude
        nativeLocation.longitude = longitude

        distance = nativeLocation.distanceTo(startPoint).toInt()
    }
}
