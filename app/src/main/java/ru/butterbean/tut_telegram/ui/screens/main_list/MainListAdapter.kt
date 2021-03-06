package ru.butterbean.tut_telegram.ui.screens.main_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.main_list_item.view.*
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.models.CommonModel
import ru.butterbean.tut_telegram.ui.screens.groups.GroupChatFragment
import ru.butterbean.tut_telegram.ui.screens.single_chat.SingleChatFragment
import ru.butterbean.tut_telegram.utilites.TYPE_CHAT
import ru.butterbean.tut_telegram.utilites.TYPE_GROUP
import ru.butterbean.tut_telegram.utilites.downloadAndSetImage
import ru.butterbean.tut_telegram.utilites.replaceFragment

class MainListAdapter: RecyclerView.Adapter<MainListAdapter.MainListHolder>() {

    private var listItems = mutableListOf<CommonModel>()

    class MainListHolder(view: View):RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.main_list_item_name
        val itemLastMessage:TextView = view.main_list_item_last_message
        val itemPhoto:ImageView = view.main_list_item_photo

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_list_item,parent,false)
        val holder = MainListHolder(view)
        holder.itemView.setOnClickListener {

            when(listItems[holder.bindingAdapterPosition].type){
                TYPE_CHAT -> replaceFragment(SingleChatFragment(listItems[holder.bindingAdapterPosition]))
                TYPE_GROUP -> replaceFragment(GroupChatFragment(listItems[holder.bindingAdapterPosition]))
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: MainListHolder, position: Int) {
        holder.itemName.text = listItems[position].fullname
        holder.itemLastMessage.text = listItems[position].lastMessage
        holder.itemPhoto.downloadAndSetImage(listItems[position].photoUrl)
    }

    fun updateListItems(item:CommonModel){
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }

    override fun getItemCount(): Int = listItems.size
}