package com.gigster.locationlist.ui.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gigster.locationlist.R
import com.gigster.locationlist.data.model.Entry

class LocationsAdapter(val itemListener: LocationItemListener): RecyclerView.Adapter<LocationsAdapter.ViewHolder>() {
    private var locations: MutableList<Entry?> = mutableListOf()

    fun setData(newData: List<Entry?>) {
        locations = newData.toMutableList()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
        return ViewHolder(view, itemListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(locations[position])
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    class ViewHolder(v: View, listener: LocationItemListener): RecyclerView.ViewHolder(v) {
        var entry: Entry? = null
        var locationNameText: TextView = v.findViewById(R.id.location_name)
        var locationText: TextView = v.findViewById(R.id.location_position)
        var distanceText: TextView = v.findViewById(R.id.distance_km)
        var physicalTypeText: TextView = v.findViewById(R.id.location_type)

        init {
            physicalTypeText.setOnClickListener {
                entry?.let {
                    if (it.resource.physicalType.coding.isNotEmpty()) {
                        listener.onClickPhysicalType(it.resource.physicalType.coding[0].display)
                    }
                }
            }
        }

        fun bindData(locationEntry: Entry?) {
            if (locationEntry != null) {
                entry = locationEntry
                locationNameText.text = locationEntry.resource.name
                locationText.text = locationText.context.getString(R.string.location_format, locationEntry.resource.position.latitude, locationEntry.resource.position.longitude)
                distanceText.text = locationText.context.getString(R.string.distance_format, locationEntry.resource.position.distance / 1000f)
                if (locationEntry.resource.physicalType.coding.isNotEmpty()) {
                    physicalTypeText.text =  locationEntry.resource.physicalType.coding[0].display
                }
            }
        }
    }
}

interface LocationItemListener {
    fun onClickPhysicalType(physicalType: String)
}