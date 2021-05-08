package ru.butterbean.tut_telegram.ui.fragments

import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_code.*
import ru.butterbean.tut_telegram.MainActivity
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.activities.RegisterActivity
import ru.butterbean.tut_telegram.utilites.*

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
                val uid = AUTH.currentUser.uid.toString()
                val dataMap = mutableMapOf<String,Any>()
                dataMap[CHILD_ID] = uid
                dataMap[CHILD_PHONE] = phoneNumber
                dataMap[CHILD_USERNAME]  = uid

                REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dataMap)
                    .addOnCompleteListener {task2 ->
                    if (task2.isSuccessful){
                        showToast("Добро пожаловать")
                        (activity as RegisterActivity).replaceActivity(MainActivity())
                    }else{
                        showToast(task2.exception?.message.toString())
                    }
                }

                showToast("Добро пожаловать!")
                (activity as RegisterActivity).replaceActivity(MainActivity())
            }else{showToast(task.exception?.message.toString())}
        }
    }
}