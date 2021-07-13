package com.coldfier.peanuttest.userfragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.coldfier.peanuttest.MainActivity
import com.coldfier.peanuttest.databinding.UserFragmentBinding

class UserFragment : Fragment() {

    private lateinit var viewModel: UserViewModel
    private lateinit var binding: UserFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            UserViewModelFactory(requireActivity().application)
        ).get(UserViewModel::class.java)

        viewModel.accountInformation.observe(viewLifecycleOwner) {
            if (it.name.isNotBlank()) binding.progressBar.visibility = View.GONE
            binding.accountInformation = it

        }

        viewModel.phoneNumber.observe(viewLifecycleOwner) {
            binding.phoneNumber = it
        }

        viewModel.userData.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.getAccountInformation(it, requireContext())
            }
        }

        viewModel.toastCatcher.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    "Please check the internet connection",
                    Toast.LENGTH_SHORT
                ).show()
                //TODO: Show the repeat button for getting an account data
                binding.refreshFAB.show()
                binding.progressBar.visibility = View.GONE
            }
        }

        binding.refreshFAB.setOnClickListener {
            viewModel.userData.value?.let { userData -> viewModel.getAccountInformation(userData, requireContext()) }
            binding.refreshFAB.hide()
            binding.progressBar.visibility = View.VISIBLE
        }

        //set visible the toolbar logout icon and the bottom navigation menu
        (requireActivity() as MainActivity).setMenusVisible()

        return binding.root
    }
}