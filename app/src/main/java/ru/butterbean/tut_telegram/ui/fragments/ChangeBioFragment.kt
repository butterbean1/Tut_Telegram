package ru.butterbean.tut_telegram.ui.fragments

import kotlinx.android.synthetic.main.fragment_change_bio.*
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.database.USER
import ru.butterbean.tut_telegram.database.setBioToDatabase
import ru.butterbean.tut_telegram.utilites.*

class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {
    override fun onResume() {
        super.onResume()
        settings_input_bio.setText(USER.bio)
    }

    override fun change() {
        val newBio = settings_input_bio.text.toString()

        setBioToDatabase(newBio)
    }

}