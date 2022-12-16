package com.tridev.observablepatterns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import com.tridev.observablepatterns.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        val repository = Repository()
        val viewModelProviderFactory = ViewModelProviderFactory(repository)
        mainViewModel = ViewModelProvider(this, viewModelProviderFactory)[MainViewModel::class.java]

        mBinding.btLivedata.setOnClickListener {
            mainViewModel.triggerLiveData()
        }

        mBinding.btStateflow.setOnClickListener {
            mainViewModel.triggerStateFlow()
        }

        mBinding.btFlow.setOnClickListener {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    mainViewModel.triggerFLow().collectLatest {
                        mBinding.tvFlow.text = it
                    }
                }
            }
        }

        mBinding.btSharedFlow.setOnClickListener {
            mainViewModel.triggerSharedFlow()
        }

        subscribeToObservables()
    }

    private fun subscribeToObservables() {
        //observe() is called whenever there is a change in livedata value
        mainViewModel.liveData.observe(this) {
            mBinding.tvLivedata.text = it
        }

        /*Hot flow -> Keep emitting values even though there are no collectors
        *Stateflow SharedFlow are Hot flows
        *Cold flow-> Won't emit any values if there is not any collector
        */
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.stateFlow.collectLatest {
                    Snackbar.make(mBinding.root, it, Snackbar.LENGTH_LONG).show()
                    mBinding.tvStateflow.text = it
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.sharedFLow.collectLatest {
                    Snackbar.make(mBinding.root, it, Snackbar.LENGTH_LONG).show()
                    mBinding.tvSharedFlow.text = it
                }
            }
        }
    }
}