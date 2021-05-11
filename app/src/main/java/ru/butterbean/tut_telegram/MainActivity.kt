package ru.butterbean.tut_telegram

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ru.butterbean.tut_telegram.activities.RegisterActivity
import ru.butterbean.tut_telegram.databinding.ActivityMainBinding
import ru.butterbean.tut_telegram.models.User
import ru.butterbean.tut_telegram.ui.fragments.ChatsFragment
import ru.butterbean.tut_telegram.ui.objects.AppDrawer
import ru.butterbean.tut_telegram.utilites.*

class  MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mToolbar: Toolbar
    lateinit var mAppDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY = this
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
        initUser()
    }

    private fun initUser() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
            .addListenerForSingleValueEvent(AppValueEventListener{
                USER = it.getValue(User::class.java) ?: User()
            })
    }

}