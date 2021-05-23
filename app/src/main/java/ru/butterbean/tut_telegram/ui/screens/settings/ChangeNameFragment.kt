package ru.butterbean.tut_telegram.ui.screens.settings

import kotlinx.android.synthetic.main.fragment_change_name.*
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.database.USER
import ru.butterbean.tut_telegram.database.setNameToDatabase
import ru.butterbean.tut_telegram.ui.screens.BaseChangeFragment
import ru.butterbean.tut_telegram.utilites.*


class ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {
    override fun onResume() {
        super.onResume()
        initFullnameList()
    }

    private fun initFullnameList() {
        val fullnameList = USER.fullname.split(" ")
        settings_input_name.setText(fullnameList[0])
        if (fullnameList.size > 1) {
            settings_input_surname.setText(fullnameList[1])
        }
    }

    override fun change() {
        val name = settings_input_name.text.toString()
        val surname = settings_input_surname.text.toString()
        if (name.isEmpty()) {
            showToast(getString(R.string.settings_toast_name_isempty), true)
        } else {
            val fullname = "$name $surname"
            setNameToDatabase(fullname)
        }
    }
}