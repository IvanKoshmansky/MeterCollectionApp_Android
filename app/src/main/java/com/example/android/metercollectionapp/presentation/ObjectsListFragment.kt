package com.example.android.metercollectionapp.presentation

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
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

    private var _objectsListViewModel: ObjectsListViewModel? = null
    private val objectsListModel: ObjectsListViewModel
        get() = _objectsListViewModel!!

    private var _binding: FragmentObjectsListBinding? = null
    private val binding: FragmentObjectsListBinding
        get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (getContext()?.applicationContext as MeterCollectionApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _objectsListViewModel = ViewModelProvider(this, viewModelFactory).get(ObjectsListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_objects_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.objectsListViewModel = objectsListModel

        val adapter = ObjectsListAdapter()
        binding.objectsListRwObjectsList.adapter = adapter
        objectsListModel.uiState.observe(viewLifecycleOwner) {
            it?.let {
                when {
                    it.isEmpty -> adapter.submitList(listOf())
                    it.isLoading -> {}
                    else -> {
                        adapter.submitList(it.objectsUiState)
                    }
                }
            }
        }

        objectsListModel.navigateToAdd.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    findNavController().navigate(
                        ObjectsListFragmentDirections.actionObjectsListFragmentToAddObjectFragment()
                    )
                    objectsListModel.navigateToAddDone()
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        objectsListModel.setup()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.objects_search_action, menu)

                val searchItem = menu.findItem(R.id.objects_search)
                val searchView = searchItem.actionView as SearchView

                searchView.queryHint = getString(R.string.objects_list_search)

                searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        newText?.let {
                            objectsListModel.filtered(it)
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
