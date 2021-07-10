package com.coldfier.peanuttest.signinfragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.coldfier.peanuttest.databinding.SignInFragmentBinding
import com.coldfier.peanuttest.repository.UserData

class SignInFragment : Fragment() {

    private lateinit var viewModel: SignInViewModel
    private lateinit var binding: SignInFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SignInFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        binding.signInButton.setOnClickListener {
            binding.apply {
                if (loginEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
                    viewModel.signIn(
                        loginEditText.text.toString().toInt(),
                        passwordEditText.text.toString()
                    )
                }
            }
        }

        viewModel.isFailedSignIn.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.partnerAuthToken.observe(viewLifecycleOwner) {
            if (it != null && viewModel.peanutAuthToken.value?.isNotEmpty() == true) {
                val userInfo = viewModel.serializeToJson(
                    UserData(
                        viewModel.getUserLoginAndPassword().login,
                        viewModel.getUserLoginAndPassword().password,
                        viewModel.peanutAuthToken.value!!,
                        it
                    )
                )
                val action = SignInFragmentDirections.actionSignInFragmentToUserFragment(userInfo)
                findNavController().navigate(action)
            }
        }

        viewModel.peanutAuthToken.observe(viewLifecycleOwner) {
            if (it != null && viewModel.partnerAuthToken.value?.isNotEmpty() == true) {
                val userInfo = viewModel.serializeToJson(
                    UserData(
                        viewModel.getUserLoginAndPassword().login,
                        viewModel.getUserLoginAndPassword().password,
                        it,
                        viewModel.partnerAuthToken.value!!
                    )
                )
                val action = SignInFragmentDirections.actionSignInFragmentToUserFragment(userInfo)
                findNavController().navigate(action)
            }
        }

        return binding.root
    }
}