package ru.butterbean.tut_telegram.ui.fragments

import kotlinx.android.synthetic.main.fragment_enter_code.*
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.utilites.AppTextWatcher
import ru.butterbean.tut_telegram.utilites.showToast

class EnterCodeFragment : BaseFragment(R.layout.fragment_enter_code) {
    override fun onStart() {
        super.onStart()
        register_input_code.addTextChangedListener(AppTextWatcher{
                val string = register_input_code.text.toString()
                if (string.length == 6){
                    verificateCode()
                }
        })
    }

    private fun verificateCode() {
        showToast("Ok")

    }
}