package ru.butterbean.tut_telegram.ui.screens.base

import androidx.fragment.app.Fragment
import ru.butterbean.tut_telegram.utilites.APP_ACTIVITY

open class BaseFragment(val layout: Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()
    }


}