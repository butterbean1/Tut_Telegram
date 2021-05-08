package ru.butterbean.tut_telegram.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.databinding.ActivityRegisterBinding
import ru.butterbean.tut_telegram.ui.fragments.EnterPhoneNumberFragment
import ru.butterbean.tut_telegram.utilites.initFirebase
import ru.butterbean.tut_telegram.utilites.replaceFragment

class RegisterActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initFirebase()
    }

    override fun onStart() {
        super.onStart()
        mToolbar = mBinding.registerToolbar
        setSupportActionBar(mToolbar)
        title = getString(R.string.register_title_your_phone)
        replaceFragment(EnterPhoneNumberFragment(),false)
    }
}