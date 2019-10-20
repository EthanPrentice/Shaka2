package com.ethanprentice.shaka.ui.frags

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout

import com.ethanprentice.shaka.R
import com.ethanprentice.shaka.connection_manager.ConnectionManager
import com.ethanprentice.shaka.connection_manager.messages.InfoResponse
import com.ethanprentice.shaka.message_manager.MessageManager
import com.ethanprentice.shaka.ui.views.DeviceInfoView


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ScanNetworkFragment.ScanNetworkFragListener] interface
 * to handle interaction events.
 * Use the [ScanNetworkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScanNetworkFragment : Fragment() {
    private var listener: ScanNetworkFragListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (container == null) {
            return null
        }

        val layout = inflater.inflate(R.layout.fragment_scan_network_framework, container, false)

        setScanOnClick(layout)

        return layout
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ScanNetworkFragListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement ScanNetworkFragListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun addDeviceInfo(infoRsp: InfoResponse) {
        try {
            val linLayout = (activity)?.findViewById<LinearLayout>(R.id.found_devices_ll)
            val deviceInfo = DeviceInfoView(activity!!, infoRsp)

            linLayout?.addView(deviceInfo)
        }
        catch(e: Exception) {
            Log.w(TAG, "Could not add device info to ui fragment", e)
        }
    }

    private fun setScanOnClick(layout: View) {
        val scanBtn = layout.findViewById<Button>(R.id.test_scan_btn)
        scanBtn?.setOnClickListener {
            ConnectionManager.scanNetwork()
        }
    }


    interface ScanNetworkFragListener {
        fun onFragmentInteraction(uri: Uri)
    }


    companion object {
        private val TAG = this::class.java.canonicalName

        @JvmStatic
        fun newInstance() = ScanNetworkFragment()
    }
}
