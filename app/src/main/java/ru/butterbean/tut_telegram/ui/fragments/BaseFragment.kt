package ru.butterbean.tut_telegram.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

open class BaseFragment(val layout:Int) : Fragment(layout) {

       override fun onStart() {
        super.onStart()
    }
}