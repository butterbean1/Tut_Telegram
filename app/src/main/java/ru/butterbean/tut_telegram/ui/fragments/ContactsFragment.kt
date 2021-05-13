package ru.butterbean.tut_telegram.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.utilites.APP_ACTIVITY


class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {
    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Контакты"
    }
}