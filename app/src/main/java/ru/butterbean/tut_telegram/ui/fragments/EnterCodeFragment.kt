package ru.butterbean.tut_telegram.ui.fragments

import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_code.*
import ru.butterbean.tut_telegram.MainActivity
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.activities.RegisterActivity
import ru.butterbean.tut_telegram.utilites.AUTH
import ru.butterbean.tut_telegram.utilites.AppTextWatcher
import ru.butterbean.tut_telegram.utilites.replaceActivity
import ru.butterbean.tut_telegram.utilites.showToast

class EnterCodeFragment(val phoneNumber: String, val id: String) : BaseFragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()
        (activity as RegisterActivity).title = phoneNumber
        register_input_code.addTextChangedListener(AppTextWatcher{
                val string = register_input_code.text.toString()
                if (string.length == 6){
                    enterCode()
                }
        })
    }

    private fun enterCode() {
        val code = register_input_code.text.toString()
        val credential = PhoneAuthProvider.getCredential(id,code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful){
                showToast("Добро пожаловать!")
                (activity as RegisterActivity).replaceActivity(MainActivity())
            }else{showToast(task.exception?.message.toString())}
        }
    }
}