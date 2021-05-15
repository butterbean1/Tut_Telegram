package ru.butterbean.tut_telegram.ui.fragments

import android.view.View
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_info.view.*
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.models.CommonModel
import ru.butterbean.tut_telegram.models.UserModel
import ru.butterbean.tut_telegram.utilites.*

class SingleChatFragment(private val contact: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mListenerInfoToolbar: AppValueEventListener
    private lateinit var mReceivingUser: UserModel
    private lateinit var mToolbarInfo: View
    private lateinit var mRefUser: DatabaseReference

    override fun onResume() {
        super.onResume()
        mToolbarInfo = APP_ACTIVITY.mToolbar.toolbar_info
        mToolbarInfo.visibility = View.VISIBLE
        mListenerInfoToolbar = AppValueEventListener {
            mReceivingUser = it.getUserModel()
            initInfoToolbar()
        }
        mRefUser = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
        mRefUser.addValueEventListener(mListenerInfoToolbar)
        chat_btn_send_message.setOnClickListener {
            val message = chat_input_message.text.toString()
            if (message.isEmpty()){
                showToast("Введите сообщение")
            }else sendMessage(message,contact.id, TYPE_TEXT){
                chat_input_message.setText("")
            }
        }
    }


    private fun initInfoToolbar() {
        if (mReceivingUser.fullname.isEmpty()) {
            mToolbarInfo.toolbar_chat_contact_fullname.text = contact.fullname
        } else {
            mToolbarInfo.toolbar_chat_contact_fullname.text = mReceivingUser.fullname
        }
        mToolbarInfo.toolbar_chat_contact_image.downloadAndSetImage(mReceivingUser.photoUrl)

        mToolbarInfo.toolbar_chat_contact_status.text = mReceivingUser.state

    }

    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE
        mRefUser.removeEventListener(mListenerInfoToolbar)
    }
}