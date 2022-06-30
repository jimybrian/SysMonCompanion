package com.dzworks.sysmoncompanion.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dzworks.sysmoncompanion.R
import com.dzworks.sysmoncompanion.databinding.FragmentMonitorFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MonitorFragment : Fragment() {

    lateinit var binding: FragmentMonitorFragmentBinding

    val viewModel: MonitorViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_monitor_fragment, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupListeners()
    }


    override fun onPause() {
        super.onPause()
        viewModel.disconnectFromHub()
    }

    override fun onResume() {
        super.onResume()
        viewModel.connectToHub()
    }

    private fun setupListeners(){
        viewModel.sysInfo.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                binding.arcCPUTemp.progress = data.cpuTempVal?.toInt() ?: 0
                binding.arcCPUUsage.progress = data.cpuUsageVal?.toInt() ?: 0

                binding.arcGPUTemp.progress = data.gpuTempVal?.toInt() ?: 0
                binding.arcGPUUsage.progress = data.gpuUsageVal?.toInt() ?: 0

                binding.barRAMLoad.setProgressPercentage(data.ramLoad?.toDouble() ?: 0.0, true)

            }
        }
    }

}