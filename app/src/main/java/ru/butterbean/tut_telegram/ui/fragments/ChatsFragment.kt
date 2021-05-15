package ru.butterbean.tut_telegram.ui.fragments

import androidx.fragment.app.Fragment
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.utilites.APP_ACTIVITY

class ChatsFragment : Fragment(R.layout.fragment_chats) {
    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Чаты"
    }
}