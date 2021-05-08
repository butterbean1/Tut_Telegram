package ru.butterbean.tut_telegram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.google.firebase.FirebaseApiNotAvailableException
import com.google.firebase.auth.FirebaseAuth
import ru.butterbean.tut_telegram.activities.RegisterActivity
import ru.butterbean.tut_telegram.databinding.ActivityMainBinding
import ru.butterbean.tut_telegram.ui.fragments.ChatsFragment
import ru.butterbean.tut_telegram.ui.objects.AppDrawer
import ru.butterbean.tut_telegram.utilites.AUTH
import ru.butterbean.tut_telegram.utilites.initFirebase
import ru.butterbean.tut_telegram.utilites.replaceActivity
import ru.butterbean.tut_telegram.utilites.replaceFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mToolbar: Toolbar
    private lateinit var mAppDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        initFields()
        initFunc()
    }

    private fun initFunc() {
        if (AUTH.currentUser != null) {
            setSupportActionBar(mToolbar)
            mAppDrawer.create()
            replaceFragment(ChatsFragment(),false)
        } else {
            replaceActivity(RegisterActivity())
        }
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer(this, mToolbar)
        initFirebase()

    }
}