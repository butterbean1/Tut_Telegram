package ru.butterbean.tut_telegram.ui.fragments.message_recycler_view.view_holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.message_item_image.view.*

class HolderImageMessage(view: View): RecyclerView.ViewHolder(view) {
    val blockUserImageMessage: ConstraintLayout = view.block_user_image_message
    val chatUserImage: ImageView = view.chat_user_image
    val chatUserImageMessageTime: TextView = view.chat_user_image_message_time

    val blockReceivedImageMessage: ConstraintLayout = view.block_received_image_message
    val chatReceivedImage: ImageView = view.chat_received_image
    val chatReceivedImageMessageTime: TextView = view.chat_received_image_message_time
}