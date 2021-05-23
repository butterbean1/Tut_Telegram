package ru.butterbean.tut_telegram.ui.message_recycler_view.view_holders

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.message_item_file.view.*
import ru.butterbean.tut_telegram.database.CURRENT_UID
import ru.butterbean.tut_telegram.database.getFileFromStorage
import ru.butterbean.tut_telegram.ui.message_recycler_view.views.MessageView
import ru.butterbean.tut_telegram.utilites.WRITE_FILES
import ru.butterbean.tut_telegram.utilites.asTime
import ru.butterbean.tut_telegram.utilites.checkPremission
import ru.butterbean.tut_telegram.utilites.showToast
import java.io.File
import java.lang.Exception

class HolderFileMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {

    private val blockUserFileMessage: ConstraintLayout = view.block_user_file_message
    private val chatUserFileMessageTime: TextView = view.chat_user_file_message_time
    private val chatUserBtnDownload: ImageView = view.chat_user_btn_download
    private val chatUserFileName: TextView = view.chat_user_file_name
    private val chatUserProgressBar: ProgressBar = view.chat_user_progress_bar

    private val blockReceivedFileMessage: ConstraintLayout = view.block_received_file_message
    private val chatReceivedFileMessageTime: TextView = view.chat_received_file_message_time
    private val chatReceivedBtnDownload: ImageView = view.chat_received_btn_download
    private val chatReceivedFileName: TextView = view.chat_received_file_name
    private val chatReceivedProgressBar: ProgressBar = view.chat_received_progress_bar

    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockReceivedFileMessage.visibility = View.GONE
            blockUserFileMessage.visibility = View.VISIBLE
            chatUserFileMessageTime.text = view.timeStamp.asTime()
            chatUserFileName.text = view.text
        } else {
            blockReceivedFileMessage.visibility = View.VISIBLE
            blockUserFileMessage.visibility = View.GONE
            chatReceivedFileMessageTime.text = view.timeStamp.asTime()
            chatReceivedFileName.text = view.text
        }
    }

    override fun onAttach(view: MessageView) {
        if (view.from == CURRENT_UID)chatUserBtnDownload.setOnClickListener { clickToBtnFile(view) }
        else chatReceivedBtnDownload.setOnClickListener { clickToBtnFile(view) }
    }

    private fun clickToBtnFile(view: MessageView) {
        if (view.from == CURRENT_UID){
            chatUserBtnDownload.visibility = View.INVISIBLE
            chatUserProgressBar.visibility = View.VISIBLE
        }else{
            chatReceivedBtnDownload.visibility = View.INVISIBLE
            chatReceivedBtnDownload.visibility = View.VISIBLE
        }

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            view.text
        )

        try {
            if (checkPremission(WRITE_FILES)){
                file.createNewFile()
                getFileFromStorage(file,view.fileUrl){
                    if (view.from == CURRENT_UID){
                        chatUserBtnDownload.visibility = View.VISIBLE
                        chatUserProgressBar.visibility = View.INVISIBLE
                    }else{
                        chatReceivedBtnDownload.visibility = View.VISIBLE
                        chatReceivedBtnDownload.visibility = View.INVISIBLE
                    }
                }
                showToast("Copied to ${Environment.DIRECTORY_DOWNLOADS}")
            }
        }catch (e:Exception){showToast(e.message.toString())}
    }

    override fun onDetach() {
        // удаляем слушатели когда вьюшки ушли с экрана
        chatUserBtnDownload.setOnClickListener(null)
        chatReceivedBtnDownload.setOnClickListener(null)
    }
}