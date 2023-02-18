package com.example.android.metercollectionapp.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.R
import com.example.android.metercollectionapp.databinding.FragmentLoginUserBinding
import com.example.android.metercollectionapp.di.ViewModelFactory
import com.example.android.metercollectionapp.presentation.viewmodels.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class LoginFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _loginViewModel: LoginViewModel? = null
    private val loginViewModel: LoginViewModel
        get() = _loginViewModel!!

    private var _binding: FragmentLoginUserBinding? = null
    private val binding: FragmentLoginUserBinding
        get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (getContext()?.applicationContext as MeterCollectionApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _loginViewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_user, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.loginViewModel = loginViewModel

        if (savedInstanceState == null) {
            val arguments = LoginFragmentArgs.fromBundle(requireArguments())
            loginViewModel.setup(arguments.isNew, arguments.userId)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.loginUiState.observe(viewLifecycleOwner) {
            if (it.isNewUser) {
                binding.loginTwUserName.setText(R.string.login_lbl_new_user)
            } else {
                binding.loginTwUserName.text = it.name
            }
            if (it.loginError) {
                Snackbar.make(binding.root, R.string.error_message_wrong_pass, Snackbar.LENGTH_SHORT).show()
            }
        }

        loginViewModel.navigationNextLiveData.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToScanObjectFragment(),
                    )
                    loginViewModel.navigationNextDone()
                }
            }
        }
    }

}
