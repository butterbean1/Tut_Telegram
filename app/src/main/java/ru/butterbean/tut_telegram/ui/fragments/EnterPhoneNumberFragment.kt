package ru.butterbean.tut_telegram.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_enter_code.*
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.utilites.replaceFragment
import ru.butterbean.tut_telegram.utilites.showToast

class EnterPhoneNumberFragment : BaseFragment(R.layout.fragment_enter_phone_number) {
    override fun onStart() {
        super.onStart()
        register_btn_next.setOnClickListener {
            sendCode()
        }
    }

    private fun sendCode() {
        if (register_input_phone_number.text.toString().isEmpty()){
            showToast(getString(R.string.register_toast_enter_code))
        }else{
            replaceFragment(EnterCodeFragment())
        }
    }
}