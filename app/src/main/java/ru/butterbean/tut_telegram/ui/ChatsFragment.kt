package ru.butterbean.tut_telegram.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.databinding.FragmentChatsBinding

class ChatsFragment : Fragment() {

    private lateinit var mBinding:FragmentChatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentChatsBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()

    }
}