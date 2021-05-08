package ru.butterbean.tut_telegram.ui.fragments

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import ru.butterbean.tut_telegram.MainActivity
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.activities.RegisterActivity
import ru.butterbean.tut_telegram.utilites.AUTH
import ru.butterbean.tut_telegram.utilites.replaceActivity
import ru.butterbean.tut_telegram.utilites.replaceFragment
import ru.butterbean.tut_telegram.utilites.showToast
import java.util.concurrent.TimeUnit

class EnterPhoneNumberFragment : BaseFragment(R.layout.fragment_enter_phone_number) {

    private lateinit var mPhoneNumber:String
    private lateinit var mCallBack:PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onStart() {
        super.onStart()
        AUTH = FirebaseAuth.getInstance()
        mCallBack = object:PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        showToast("Добро пожаловать!")
                        (activity as RegisterActivity).replaceActivity(MainActivity())
                    }else{showToast(task.exception?.message.toString())}
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(EnterCodeFragment(mPhoneNumber,id))
            }
        }
        register_btn_next.setOnClickListener {
            sendCode()
        }
    }

    private fun sendCode() {
        if (register_input_phone_number.text.toString().isEmpty()){
            showToast(getString(R.string.register_toast_enter_code))
        }else{
            authUser()
        }
    }

    private fun authUser() {
        mPhoneNumber = register_input_phone_number.text.toString()
        val phoneAuthOptions = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(mPhoneNumber)
            .setTimeout(60,TimeUnit.SECONDS)
            .setActivity(activity as RegisterActivity)
            .setCallbacks(mCallBack)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)
    }
}