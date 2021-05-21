package ru.butterbean.tut_telegram.ui.fragments.message_recycler_view.view_holder

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.message_item_text.view.*
import ru.butterbean.tut_telegram.database.CURRENT_UID
import ru.butterbean.tut_telegram.ui.fragments.message_recycler_view.views.MessageView
import ru.butterbean.tut_telegram.utilites.asTime

class HolderTextMessage (view: View): RecyclerView.ViewHolder(view) {
    val blockUserMessage: ConstraintLayout = view.block_user_message
    val chatUserMessage: TextView = view.chat_user_message
    val chatUserMessageTime: TextView = view.chat_user_message_time

    val blockReceivedMessage: ConstraintLayout = view.block_recieved_message
    val chatReceivedMessage: TextView = view.chat_recieved_message
    val chatReceivedMessageTime: TextView = view.chat_recieved_message_time

    fun drawMessageText(view:MessageView) {

        if (view.from == CURRENT_UID) {
            blockUserMessage.visibility = View.VISIBLE
            blockReceivedMessage.visibility = View.GONE
            chatUserMessage.text = view.text
            chatUserMessageTime.text =
                view.timeStamp.asTime()
        } else {
            blockUserMessage.visibility = View.GONE
            blockReceivedMessage.visibility = View.VISIBLE
            chatReceivedMessage.text = view.text
            chatReceivedMessageTime.text =
                view.timeStamp.asTime()

        }
    }
}