package com.coldfier.peanuttest.userfragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.coldfier.peanuttest.R
import com.coldfier.peanuttest.databinding.UserFragmentBinding

class UserFragment : Fragment() {

    private lateinit var viewModel: UserViewModel
    private lateinit var binding: UserFragmentBinding
    private lateinit var json: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        json = UserFragmentArgs.fromBundle(requireArguments()).userInfo
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = UserFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, UserViewModelFactory(json)).get(UserViewModel::class.java)

        viewModel.accountInformation.observe(viewLifecycleOwner) {
            binding.accountInformation = it
        }

        viewModel.phoneNumber.observe(viewLifecycleOwner) {
            binding.phoneNumber = it
        }

        binding.quotesButton.setOnClickListener {
            val action = UserFragmentDirections.actionUserFragmentToQuotesFragment(json)
            findNavController().navigate(action)
        }

        return binding.root
    }
}