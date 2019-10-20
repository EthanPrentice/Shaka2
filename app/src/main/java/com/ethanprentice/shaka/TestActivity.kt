package com.ethanprentice.shaka

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.ethanprentice.shaka.adt.UserInfo
import com.ethanprentice.shaka.connection_manager.messages.InfoResponse
import com.ethanprentice.shaka.information_manager.InfoManager
import com.ethanprentice.shaka.ui.ActivityManager
import com.ethanprentice.shaka.ui.frags.LoginFragment
import com.ethanprentice.shaka.ui.frags.ScanNetworkFragment


class TestActivity : AppCompatActivity(), LoginFragment.OnLoginListener, ScanNetworkFragment.ScanNetworkFragListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        ActivityManager.activityQueue.push(this)

        replaceFragment(LoginFragment.newInstance())
    }

    override fun onDestroy() {
        ActivityManager.activityQueue.pop()

        super.onDestroy()
    }

    /* Frag interface methods */
    override fun onLogin(userInfo: UserInfo) {
        InfoManager.userInfo = userInfo
        replaceFragment(ScanNetworkFragment.newInstance())
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    /* End of frag interface methods */

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.login_frame_container, fragment)
                .commit()
    }

    /**
     * Adds a DeviceInfoView to the LinearLayout using data included in the InfoResponse
     */
    fun addDeviceInfo(infoRsp: InfoResponse) {
        val frag = supportFragmentManager.findFragmentById(R.id.login_frame_container)
        if (frag != null && frag is ScanNetworkFragment) {
            runOnUiThread {
                frag.addDeviceInfo(infoRsp)
            }
        }
    }
}
