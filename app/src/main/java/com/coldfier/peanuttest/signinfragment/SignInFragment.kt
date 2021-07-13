package com.coldfier.peanuttest.signinfragment

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.coldfier.peanuttest.databinding.SignInFragmentBinding
import com.coldfier.peanuttest.repository.UserData
import java.lang.NumberFormatException
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

        binding.signInButton.setOnClickListener {
            binding.apply {
                if (loginEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
                    signInProgressBar.visibility = View.VISIBLE
                    signInButton.isClickable = false
                    var login: Int = try {
                        loginEditText.text.toString().toInt()
                    } catch (e: NumberFormatException) {
                        0
                    }
                    viewModel.signIn(
                            login,
                            passwordEditText.text.toString(),
                            requireContext()
                    )
                    viewModel.signIn(
                        login,
                        passwordEditText.text.toString(),
                        requireContext()
                    )
                } else {
                    Toast.makeText(
                            requireContext(),
                            "Please insert the login and password",
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.loginEditText.setOnFocusChangeListener { v, hasFocus -> if (!hasFocus) hideKeyboard(v) }
        binding.passwordEditText.setOnFocusChangeListener { v, hasFocus -> if (!hasFocus) hideKeyboard(v) }

        viewModel.isFailedSignIn.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.signInButton.isClickable = true
                binding.signInProgressBar.visibility = View.GONE
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.authorizationState.observe(viewLifecycleOwner) {
            if (it) {
                val action = SignInFragmentDirections.actionSignInFragmentToUserFragment()
                findNavController().navigate(action)
            } else {
                binding.signInLayout.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    fun hideKeyboard(v: View) {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
    }
}