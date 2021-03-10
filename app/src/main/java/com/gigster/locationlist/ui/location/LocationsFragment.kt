package com.gigster.locationlist.ui.location

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gigster.locationlist.R
import com.gigster.locationlist.ui.MainActivity

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LocationsFragment : Fragment() {
    private val viewModel: LocationsViewModel by viewModels()
    private lateinit var adapter: LocationsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).setSupportActionBar(toolbar)
        subscribeUI()
        viewModel.fetchLocations()
    }

    private fun initView(view: View) {
        progressBar = view.findViewById(R.id.progress_bar)
        toolbar = view.findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.locations)
        recyclerView = view.findViewById(R.id.location_list)
        val layoutMng = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutMng
        adapter = LocationsAdapter(object: LocationItemListener {
            override fun onClickPhysicalType(physicalType: String) {
                viewModel.filterByGroups(physicalType)
            }
        })
        recyclerView.adapter = adapter
    }

    private fun subscribeUI() {
        viewModel.getLocations().observe(viewLifecycleOwner, {
            adapter.setData(it)
        })
        viewModel.isLoading().observe(viewLifecycleOwner, {
            if (it) progressBar.visibility = View.VISIBLE else progressBar.visibility = View.GONE
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        menuInflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort -> {
                viewModel.sortLocations()
                true
            }
            R.id.action_show_all -> {
                viewModel.showAllLocations()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}