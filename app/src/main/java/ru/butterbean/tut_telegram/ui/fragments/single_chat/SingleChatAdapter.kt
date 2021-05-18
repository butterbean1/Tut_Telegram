package ru.butterbean.tut_telegram.ui.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.message_item.view.*
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.models.CommonModel
import ru.butterbean.tut_telegram.utilites.CURRENT_UID
import ru.butterbean.tut_telegram.utilites.DiffUtilCallback
import ru.butterbean.tut_telegram.utilites.asTime
import java.text.SimpleDateFormat
import java.util.*

class SingleChatAdapter: RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var mListMessagesCache = mutableListOf<CommonModel>()
    private lateinit var mDiffResult: DiffUtil.DiffResult

    class SingleChatHolder(view: View):RecyclerView.ViewHolder(view){
        val blockUserMessage:ConstraintLayout = view.block_user_message
        val chatUserMessage:TextView = view.chat_user_message
        val chatUserMessageTime:TextView = view.chat_user_message_time

        val blockReceivedMessage:ConstraintLayout = view.block_recieved_message
        val chatReceivedMessage:TextView = view.chat_recieved_message
        val chatReceivedMessageTime:TextView = view.chat_recieved_message_time

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item,parent,false)
        return SingleChatHolder(view)
    }

    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
        if (mListMessagesCache[position].from == CURRENT_UID){
            holder.blockUserMessage.visibility = View.VISIBLE
            holder.blockReceivedMessage.visibility = View.GONE
            holder.chatUserMessage.text = mListMessagesCache[position].text
            holder.chatUserMessageTime.text =
                mListMessagesCache[position].timeStamp.toString().asTime()
        }else{
            holder.blockUserMessage.visibility = View.GONE
            holder.blockReceivedMessage.visibility = View.VISIBLE
            holder.chatReceivedMessage.text = mListMessagesCache[position].text
            holder.chatReceivedMessageTime.text =
                mListMessagesCache[position].timeStamp.toString().asTime()

        }
    }

    override fun getItemCount(): Int = mListMessagesCache.size


    fun addItem(item:CommonModel,
                toBottom:Boolean,
                onSucess:()->Unit){
        if (toBottom){
            if (!mListMessagesCache.contains(item)){
                mListMessagesCache.add(item)
                notifyItemInserted(mListMessagesCache.size)
            }
        }else{
            if (!mListMessagesCache.contains(item)){
                mListMessagesCache.add(item)
                mListMessagesCache.sortBy { it.timeStamp.toString() }
                notifyItemInserted(0)
            }
        }
        onSucess()
    }
}


