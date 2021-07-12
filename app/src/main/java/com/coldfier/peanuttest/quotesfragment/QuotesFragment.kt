package com.coldfier.peanuttest.quotesfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coldfier.peanuttest.MainActivity
import com.coldfier.peanuttest.databinding.QuotesFragmentBinding
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import java.text.SimpleDateFormat
import java.util.*

class QuotesFragment : Fragment() {

    private lateinit var viewModel: QuotesViewModel
    private lateinit var binding: QuotesFragmentBinding

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = QuotesFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            QuotesViewModelFactory(requireActivity().application)
        ).get(QuotesViewModel::class.java)

        val quotesRVAdapter = QuotesRVAdapter()
        binding.quotesRecyclerView.apply {
            adapter = quotesRVAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        }

        viewModel.toastCatcher.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    "Please check the internet connection",
                    Toast.LENGTH_SHORT
                ).show()
                //TODO: Show the repeat button for getting an account data
            }
        }

        viewModel.userData.observe(viewLifecycleOwner) {
            viewModel.getQuoteList()
        }

        viewModel.pairs.observe(viewLifecycleOwner) {
            if (viewModel.quotesList.value != null) viewModel.getQuoteList()
        }

        viewModel.startDateTime.observe(viewLifecycleOwner) {
            if (viewModel.quotesList.value != null)viewModel.getQuoteList()
            binding.startDateTimeButton.text =
                SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss").format(Date(it))
        }

        viewModel.endDateTime.observe(viewLifecycleOwner) {
            if (viewModel.quotesList.value != null)viewModel.getQuoteList()
            binding.endDateTimeButton.text =
                SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss").format(Date(it))
        }

        viewModel.quotesList.observe(viewLifecycleOwner) {
            quotesRVAdapter.submitList(it)
        }

        //this need to prevent duplicated saving of pairs when the user rotated the screen
        viewModel.clearPairsList()

        binding.chipGroup.forEachIndexed { index, view ->
            // the item with 0 is a TextView, need to skip
            if (index != 0) {
                val chip = view as Chip
                //init check and adding to LiveData
                if (chip.isChecked) {
                    viewModel.addCheckedChip(chip)
                }
                if (!viewModel.quotesList.value.isNullOrEmpty()) viewModel.getQuoteList()
                //adding listener to chip
                chip.setOnCheckedChangeListener { chip1, isChecked ->
                    if (isChecked) {
                        viewModel.addCheckedChip(chip1 as Chip)
                    } else {
                        viewModel.deleteUncheckedChip(chip1 as Chip)
                    }
                }
            }
        }

        binding.startDateTimeButton.setOnClickListener {
            datePicker(viewModel.startDateTime) {
                timePicker(viewModel.startDateTime)
            }
        }

        binding.endDateTimeButton.setOnClickListener {
            datePicker(viewModel.endDateTime) {
                timePicker(viewModel.endDateTime)
            }
        }

        //set visible the toolbar logout icon and the bottom navigation menu
        (requireActivity() as MainActivity).setMenusVisible()

        return binding.root
    }

    private fun datePicker(liveData: MutableLiveData<Long>, timePicker: () -> Unit) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(liveData.value)
            .build()
        datePicker.addOnPositiveButtonClickListener {
            liveData.value = it
            timePicker()
        }
        datePicker.show(parentFragmentManager, "date_picker")
    }

    private fun timePicker(liveData: MutableLiveData<Long>) {
        val timePicker = MaterialTimePicker.Builder()
            .setTitleText("Select time")
            .setHour(0)
            .setMinute(0)
            .setTimeFormat(CLOCK_24H)
            .build()
        timePicker.addOnPositiveButtonClickListener {
            val time = (timePicker.hour * 3600000) + (timePicker.minute * 60000)
            liveData.value = liveData.value?.plus(time)
        }
        timePicker.show(parentFragmentManager, "time_picker")
    }
}