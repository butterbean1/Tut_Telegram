package ru.butterbean.tut_telegram.ui.fragments.single_chat

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.tut_telegram.database.CURRENT_UID
import ru.butterbean.tut_telegram.ui.fragments.message_recycler_view.view_holder.AppHolderFactory
import ru.butterbean.tut_telegram.ui.fragments.message_recycler_view.view_holder.HolderImageMessage
import ru.butterbean.tut_telegram.ui.fragments.message_recycler_view.view_holder.HolderTextMessage
import ru.butterbean.tut_telegram.ui.fragments.message_recycler_view.view_holder.HolderVoiceMessage
import ru.butterbean.tut_telegram.ui.fragments.message_recycler_view.views.MessageView
import ru.butterbean.tut_telegram.utilites.asTime
import ru.butterbean.tut_telegram.utilites.downloadAndSetImage

class SingleChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListMessagesCache = mutableListOf<MessageView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return AppHolderFactory.getHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HolderImageMessage -> holder.drawMessageImage(mListMessagesCache[position])
            is HolderTextMessage -> holder.drawMessageText(mListMessagesCache[position])
            is HolderVoiceMessage -> holder.drawMessageVoice(mListMessagesCache[position])
            else -> {
            } // do nothing
        }
    }

    override fun getItemViewType(position: Int): Int {
        return mListMessagesCache[position].getTypeView()
    }

    override fun getItemCount(): Int = mListMessagesCache.size


    fun addItemToBottom(
        item: MessageView,
        onSuccess: () -> Unit
    ) {
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            notifyItemInserted(mListMessagesCache.size)
        }
        onSuccess()
    }

    fun addItemToTop(
        item: MessageView,
        onSuccess: () -> Unit
    ) {
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            mListMessagesCache.sortBy { it.timeStamp }
            notifyItemInserted(0)
        }
        onSuccess()
    }

}


