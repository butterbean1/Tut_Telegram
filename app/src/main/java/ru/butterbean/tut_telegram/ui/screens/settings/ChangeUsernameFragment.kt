package ru.butterbean.tut_telegram.ui.screens.settings

import kotlinx.android.synthetic.main.fragment_change_username.*
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.database.*
import ru.butterbean.tut_telegram.ui.screens.base.BaseChangeFragment
import ru.butterbean.tut_telegram.utilites.*

class ChangeUsernameFragment : BaseChangeFragment(R.layout.fragment_change_username) {

    lateinit var mNewUsername:String

    override fun onResume() {
        super.onResume()
        settings_input_username.setText(USER.username)
    }

    override fun change() {
        mNewUsername = settings_input_username.text.toString().toLowerCase()
        if (mNewUsername.isEmpty()){
            showToast(getString(R.string.settings_toast_name_isempty),true)
        }else if (mNewUsername!= USER.username){
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener{
                    if (it.hasChild(mNewUsername)){
                        showToast(getString(R.string.username_is_busy),true)
                    }else{
                        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mNewUsername).setValue(
                            CURRENT_UID
                        )
                            .addOnCompleteListener {
                                if (it.isSuccessful){
                                    updateCurrentUsername(mNewUsername)
                                }
                            }
                    }
                })
        }else fragmentManager?.popBackStack()
    }
}