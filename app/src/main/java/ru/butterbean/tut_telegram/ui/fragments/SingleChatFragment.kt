package ru.butterbean.tut_telegram.ui.fragments

import android.view.View
import kotlinx.android.synthetic.main.activity_main.view.*
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.models.CommonModel
import ru.butterbean.tut_telegram.utilites.APP_ACTIVITY

class SingleChatFragment(contact: CommonModel) : BaseFragment(R.layout.fragment_single_chat) {
    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.mToolbar.toolbar_info.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        APP_ACTIVITY.mToolbar.toolbar_info.visibility = View.GONE
    }
}