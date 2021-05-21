package ru.butterbean.tut_telegram.ui.fragments.message_recycler_view.view_holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.message_item_voice.view.*
import ru.butterbean.tut_telegram.database.CURRENT_UID
import ru.butterbean.tut_telegram.ui.fragments.message_recycler_view.views.MessageView
import ru.butterbean.tut_telegram.utilites.asTime

class HolderVoiceMessage(view: View): RecyclerView.ViewHolder(view) {
    val blockUserVoiceMessage: ConstraintLayout = view.block_user_voice_message
    val chatUserVoiceMessageTime: TextView = view.chat_user_voice_message_time
    val chatUserBtnPlay: ImageView = view.chat_user_btn_play
    val chatUserBtnStop: ImageView = view.chat_user_btn_stop

    val blockReceivedVoiceMessage: ConstraintLayout = view.block_received_voice_message
    val chatReceivedVoiceMessageTime: TextView = view.chat_received_voice_message_time
    val chatReceivedBtnPlay: ImageView = view.chat_received_btn_play
    val chatReceivedBtnStop: ImageView = view.chat_received_btn_stop

    fun drawMessageVoice(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockReceivedVoiceMessage.visibility = View.GONE
            blockUserVoiceMessage.visibility = View.VISIBLE

            chatUserVoiceMessageTime.text =
                view.timeStamp.asTime()
        } else {
            blockReceivedVoiceMessage.visibility = View.VISIBLE
            blockUserVoiceMessage.visibility = View.GONE

            chatReceivedVoiceMessageTime.text =
                view.timeStamp.asTime()
        }

    }

}