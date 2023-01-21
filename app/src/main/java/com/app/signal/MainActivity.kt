package com.app.signal

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.signal.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startCollectors()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun startCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { bindFlows() }
                launch {

                }
                launch {

                }
            }
        }
    }

    private suspend fun bindFlows() {
        println("!!!!!! Bind Flows")
        vm.photos.collect {
            it.data?.items?.map { photo ->
                println("Large Image ${photo.img.largeImageUrl}")
                println("Small Image ${photo.img.smallImageUrl}")
                println("Thumbnail Image ${photo.img.thumbNailUrl}")
                vm.savePhoto(photo).collect {
                    println("!!!!!!!! Photo saved ${it.isSuccess} $photo")
                    if (it.isSuccess) {
                        vm.getSavedPhotos().collect { state ->
                            state.data?.map {
                                println("!!!!!!! Saved Image $it")
                                if (state.isSuccess) {
                                    vm.deletePhoto(it.id).collect {
                                        println("!!!!!!! Image deleted ${photo.id}")
                                        vm.getSavedPhotos().collect {
                                            println("!!!!!!! ${it.isSuccess} No photos ${it.data}")
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}