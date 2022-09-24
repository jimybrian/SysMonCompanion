package com.dzworks.sysmoncompanion.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dzworks.sysmoncompanion.R
import com.dzworks.sysmoncompanion.connections.AppPreferences
import com.dzworks.sysmoncompanion.databinding.FragmentMonitorFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MonitorFragment : Fragment() {

    lateinit var binding: FragmentMonitorFragmentBinding

    val viewModel: MonitorViewModel by viewModels()

    @Inject
    lateinit var appPreferences: AppPreferences


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_monitor_fragment, container, false)

        binding.btnConnect.setOnClickListener {
            viewModel.connectToHub()
        }

        binding.btnSave.setOnClickListener {
            if(!viewModel.savedUrl.value.isNullOrEmpty())
                appPreferences.url = viewModel.savedUrl.value
        }

        binding.btnFillAddress.setOnClickListener {
            val myIpAddress = getIpAddress()

            val dialog =  AlertDialog.Builder(requireContext())
                .setTitle("Confirm Ip Address")
                .setMessage("Is your IP Address $myIpAddress")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                    val url = "https://$myIpAddress:82/"
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })

            dialog.show()
        }

        return binding.root
    }

    private fun getIpAddress() : String?{
        try {
            val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf: NetworkInterface = en.nextElement()
                val enumIpAddr: Enumeration<InetAddress> = intf.getInetAddresses()
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress: InetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (ex: SocketException) {
            ex.printStackTrace()
        }
        return null
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

        viewModel.isConnectedLiveData.observe(viewLifecycleOwner) { connectionState ->
            when{
                connectionState -> {
                    binding.cardCpuData.visibility = View.VISIBLE
                    binding.cardGPUData.visibility = View.VISIBLE
                    binding.cardRAMData.visibility = View.VISIBLE

                    binding.btnConnect.text = "Disconnect"
                }
                else -> {
                    binding.cardCpuData.visibility = View.GONE
                    binding.cardGPUData.visibility = View.GONE
                    binding.cardRAMData.visibility = View.GONE

                    binding.btnConnect.text = "Connect"
                }
            }
        }
    }

}