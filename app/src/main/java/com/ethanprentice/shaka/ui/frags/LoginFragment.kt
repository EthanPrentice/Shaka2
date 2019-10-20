package com.ethanprentice.shaka.ui.frags

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout

import com.ethanprentice.shaka.R
import com.ethanprentice.shaka.adt.UserInfo


/**
 * [Fragment] for to handle logging the user in, for now logging in is just entering a display name
 * TODO: when Spotify is implemented include Spotify API sign in
 *
 * @author Ethan Prentice
 */
class LoginFragment : Fragment() {
    private var listener: OnLoginListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, _container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (_container == null) {
            return inflater.inflate(R.layout.fragment_login, _container, false)
        }

        val container = inflater.inflate(R.layout.fragment_login, _container, false) as FrameLayout

        val nameInput = container.findViewById<EditText>(R.id.login_name_input)
        val loginBtn = container.findViewById<Button>(R.id.login_btn)

        fun Button.disable() {
            setBackgroundColor(resources.getColor(R.color.darkGrey, null))
            isClickable = false
        }

        fun Button.enable() {
            setBackgroundColor(resources.getColor(R.color.grey, null))
            isClickable = true
        }

        nameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun afterTextChanged(p0: Editable?) { }

            override fun onTextChanged(str: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (str != null && 4 <= str.length && str.length <= 20) {
                    loginBtn.enable()
                }
                else {
                    loginBtn.disable()
                }
            }
        })

        loginBtn.setOnClickListener {
            val uInfo = UserInfo(nameInput.text.toString(), null)
            listener?.onLogin(uInfo)
        }

        // Inflate the layout for this fragment
        return container
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLoginListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnLoginListener")
        }

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnLoginListener {
        fun onLogin(userInfo: UserInfo)
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}
