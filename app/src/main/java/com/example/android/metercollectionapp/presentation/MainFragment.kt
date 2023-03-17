package com.example.android.metercollectionapp.presentation

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.R
import com.example.android.metercollectionapp.databinding.FragmentMainBinding
import com.example.android.metercollectionapp.di.ViewModelFactory
import com.example.android.metercollectionapp.presentation.adapters.UserClickListener
import com.example.android.metercollectionapp.presentation.adapters.UsersListAdapter
import com.example.android.metercollectionapp.presentation.viewmodels.MainViewModel
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var mainViewModel: MainViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (getContext()?.applicationContext as MeterCollectionApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main,
            container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel

        binding.btnAddUser.setOnClickListener {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToLoginFragment(0, true)
            )
        }

        val adapter = UsersListAdapter(
            UserClickListener { id ->
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToLoginFragment(id, false)
                )
            }
        )
        binding.rwSelectUser.adapter = adapter
        mainViewModel.uiState.observe(viewLifecycleOwner) {
            if (!it.isLoading) {
                adapter.submitList(it.users)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.setup()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.main_menu_edit_objects_list_id -> {
                        findNavController().navigate(
                            MainFragmentDirections.actionMainFragmentToObjectsListFragment()
                        )
                        true
                    }
                    R.id.main_menu_device_params_list_id -> {
                        findNavController().navigate(
                            MainFragmentDirections.actionMainFragmentToDeviceParamsListFragment()
                        )
                        true
                    }
                    R.id.main_menu_set_params_to_object_id -> {
                        findNavController().navigate(
                            MainFragmentDirections.actionMainFragmentToDeviceParamsSelectFragment()
                        )
                        true
                    }
                    R.id.main_menu_global_settings_id -> {

                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}

//onCreateView()
//onViewCreated()
//вызываются при восстановлении фрагмента из бэкстека когда фрагмент поднимается выше
//т.е. когда фрагмент переходит в состояние CREATED после вызовов onStop() и onDestroyView()
//но не вызываются когда все приложение сворачивается в бэкстек (переходит в бэкграунд)
//при уходе фрагмента в бэкстек под новый фрагмент, вью "старого" фрагмента уничтожаются
