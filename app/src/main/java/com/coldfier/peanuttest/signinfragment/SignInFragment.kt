package com.coldfier.peanuttest.signinfragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.coldfier.peanuttest.databinding.SignInFragmentBinding
import com.coldfier.peanuttest.repository.UserData
import kotlin.math.log

class SignInFragment : Fragment() {

    private val viewModel: SignInViewModel by viewModels()
    private lateinit var binding: SignInFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getAccount(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SignInFragmentBinding.inflate(inflater, container, false)
        //viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        binding.signInButton.setOnClickListener {
            binding.apply {
                if (loginEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
                    viewModel.signIn(
                        loginEditText.text.toString().toInt(),
                        passwordEditText.text.toString(),
                        requireContext()
                    )
                }
            }
        }

        viewModel.isFailedSignIn.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.authorizationState.observe(viewLifecycleOwner) {
            if (it) {
                val action = SignInFragmentDirections.actionSignInFragmentToUserFragment()
                findNavController().navigate(action)
            }
        }

        return binding.root
    }
}