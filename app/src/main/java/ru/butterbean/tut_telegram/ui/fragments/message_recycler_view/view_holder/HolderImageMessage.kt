package ru.butterbean.tut_telegram.ui.fragments.message_recycler_view.view_holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.message_item_image.view.*
import ru.butterbean.tut_telegram.database.CURRENT_UID
import ru.butterbean.tut_telegram.ui.fragments.message_recycler_view.views.MessageView
import ru.butterbean.tut_telegram.utilites.asTime
import ru.butterbean.tut_telegram.utilites.downloadAndSetImage

class HolderImageMessage(view: View): RecyclerView.ViewHolder(view) {
    val blockUserImageMessage: ConstraintLayout = view.block_user_image_message
    val chatUserImage: ImageView = view.chat_user_image
    val chatUserImageMessageTime: TextView = view.chat_user_image_message_time

    val blockReceivedImageMessage: ConstraintLayout = view.block_received_image_message
    val chatReceivedImage: ImageView = view.chat_received_image
    val chatReceivedImageMessageTime: TextView = view.chat_received_image_message_time

    fun drawMessageImage(view:MessageView) {

        if (view.from == CURRENT_UID) {
            blockReceivedImageMessage.visibility = View.GONE
            blockUserImageMessage.visibility = View.VISIBLE

            chatUserImage.downloadAndSetImage(view.fileUrl)
            chatUserImageMessageTime.text =
                view.timeStamp.asTime()
        } else {
            blockReceivedImageMessage.visibility = View.VISIBLE
            blockUserImageMessage.visibility = View.GONE

            chatReceivedImage.downloadAndSetImage(view.fileUrl)
            chatReceivedImageMessageTime.text =
                view.timeStamp.asTime()
        }
    }
}