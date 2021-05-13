package ru.butterbean.tut_telegram

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.butterbean.tut_telegram.activities.RegisterActivity
import ru.butterbean.tut_telegram.databinding.ActivityMainBinding
import ru.butterbean.tut_telegram.models.User
import ru.butterbean.tut_telegram.ui.fragments.ChatsFragment
import ru.butterbean.tut_telegram.ui.objects.AppDrawer
import ru.butterbean.tut_telegram.utilites.*

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mToolbar: Toolbar
    lateinit var mAppDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        initFirebase()
        initUser {
            CoroutineScope(Dispatchers.IO).launch {
                initContacts()
            }

            initFields()
            initFunc()
        }
    }

    private fun initContacts() {
        if (checkPremission(READ_CONTACTS)){
            val  array = arrayOfNulls<Int>(50000)
            array.forEach { println(it) }
        }
    }

    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }

    private fun initFunc() {
        setSupportActionBar(mToolbar)
        mAppDrawer.create()
        if (AUTH.currentUser != null) {
            replaceFragment(ChatsFragment(), false)
        } else {
            replaceActivity(RegisterActivity())
        }
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer(this, mToolbar)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(APP_ACTIVITY, READ_CONTACTS)==PackageManager.PERMISSION_GRANTED){
            initContacts()
        }
    }
}