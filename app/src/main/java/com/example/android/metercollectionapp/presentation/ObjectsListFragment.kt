package com.example.android.metercollectionapp.presentation

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.R
import com.example.android.metercollectionapp.databinding.FragmentObjectsListBinding
import com.example.android.metercollectionapp.di.ViewModelFactory
import com.example.android.metercollectionapp.presentation.adapters.ObjectsListAdapter
import com.example.android.metercollectionapp.presentation.viewmodels.ObjectsListViewModel
import javax.inject.Inject

class ObjectsListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var objectsListViewModel: ObjectsListViewModel
    private lateinit var binding: FragmentObjectsListBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (getContext()?.applicationContext as MeterCollectionApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        objectsListViewModel = ViewModelProvider(this, viewModelFactory).get(ObjectsListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_objects_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.objectsListViewModel = objectsListViewModel

        val adapter = ObjectsListAdapter(null)
        binding.rwObjects.adapter = adapter
        objectsListViewModel.uiState.observe(viewLifecycleOwner) {
            if (!it.isLoading) {
                adapter.submitList(it.objects)
            }
        }

        objectsListViewModel.navigateToAdd.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(
                    ObjectsListFragmentDirections.actionObjectsListFragmentToAddObjectFragment()
                )
                objectsListViewModel.navigateToAddDone()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        objectsListViewModel.setup()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.objects_search_action, menu)

                val searchItem = menu.findItem(R.id.objects_search)
                val searchView = searchItem.actionView as SearchView

                searchView.queryHint = getString(R.string.search_object)

                searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        newText?.let {
                            objectsListViewModel.filtered(it)
                        }
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.objects_search -> {
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}
