package com.coldfier.peanuttest

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.coldfier.peanuttest.databinding.ActivityMainBinding
import com.coldfier.peanuttest.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: AppRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = AppRepository.getInstance(applicationContext)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.logout) {
                lifecycleScope.launch(Dispatchers.IO) {
                    repository.deleteAccount()
                }


                binding.toolbar.menu.findItem(R.id.logout).isVisible = false
                binding.bottomNavigationView.visibility = View.GONE
                findNavController(R.id.nav_host_fragment).navigate(R.id.signInFragment)
            }
            true
        }

        binding.toolbar.menu.findItem(R.id.logout).isVisible = false
        binding.bottomNavigationView.visibility = View.GONE

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.account_page -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.userFragment)
                }
                R.id.quotes_page -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.quotesFragment)
                }
            }
            true
        }
    }

    fun setMenusVisible() {
        binding.toolbar.menu.findItem(R.id.logout).isVisible = true
        binding.bottomNavigationView.visibility = View.VISIBLE
    }
}