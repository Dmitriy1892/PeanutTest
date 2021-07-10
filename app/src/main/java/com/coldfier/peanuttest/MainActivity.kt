package com.coldfier.peanuttest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

//        binding.toolbar.setOnMenuItemClickListener {
//            if (it.itemId == R.id.logout) {
//                lifecycleScope.launch(Dispatchers.IO) {
//                    repository.deleteAccount()
//                }
//                findNavController()
//            }
//            true
//        }
    }
}