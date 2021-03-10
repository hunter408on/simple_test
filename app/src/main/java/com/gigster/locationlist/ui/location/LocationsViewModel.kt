package com.gigster.locationlist.ui.location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gigster.locationlist.data.apiservices.APIListener
import com.gigster.locationlist.data.apiservices.APIManager
import com.gigster.locationlist.data.model.APIResult
import com.gigster.locationlist.data.model.Entry

class LocationsViewModel : ViewModel() {
    private var locationsList: MutableList<Entry> = mutableListOf()
    private var activeLocationsList: MutableLiveData<List<Entry?>> = MutableLiveData()
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun isLoading(): MutableLiveData<Boolean> = isLoading
    fun getLocations(): MutableLiveData<List<Entry?>> = activeLocationsList

    fun fetchLocations() {
        isLoading.value = true
        APIManager.getInstance().getLocations(object : APIListener {
            override fun onSuccess(apiResult: APIResult?) {
                isLoading.postValue(false)
                apiResult?.let {
                    locationsList = it.entry.toMutableList()
                    activeLocationsList.postValue(it.entry)
                }
            }
        })
    }

    fun sortLocations() {
        activeLocationsList.value?.let {
            val sortedList = countingSort(it)
            activeLocationsList.value = sortedList
        }
    }

    fun filterByGroups(physicalType: String) {
        val filteredList = locationsList.filter {
            if (it.resource.physicalType.coding.isEmpty()) {
                false
            } else {
                it.resource.physicalType.coding[0].display == physicalType
            }
        }
        activeLocationsList.value = filteredList
    }

    fun showAllLocations() {
        activeLocationsList.value = locationsList
    }

    private fun countingSort(input: List<Entry?>): List<Entry?> {
        //create a hashmap of that key is distance and value is count of distance
        val hashMap: HashMap<Int, Int> = HashMap()
        for (element in input) {
            if (element != null) {
                val distance = element.resource.position.distance
                if (hashMap[distance] == null) {
                    hashMap[distance] = 1
                } else {
                    hashMap[distance] = hashMap[distance]!! + 1
                }
            }
        }

        //sort the hashmap keys
        val sortedKeys = hashMap.keys.sorted()

        //create output by iterating input and hashmap
        val output: MutableList<Entry?> = MutableList(input.size) { null }
        for (element in input) {
            if (element != null) {
                val distance = element.resource.position.distance
                //find the index of the distance in keys
                val indexOfDistance = sortedKeys.indexOfFirst { it == distance }
                if (indexOfDistance > -1) {
                    var totalCountUpto = 0 //calculate upto the distance
                    for (i in 0 until indexOfDistance) {
                        val key = sortedKeys[i]
                        totalCountUpto += hashMap[key]!!
                    }
                    loop@for (i in totalCountUpto until totalCountUpto + sortedKeys[indexOfDistance]) {
                        if (output[i] == null) {
                            output[i] = element
                            break@loop
                        }
                    }
                }
            }
        }
        return output.toList()
    }
}