package ru.butterbean.tut_telegram.ui.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.message_item_voice.view.*
import ru.butterbean.tut_telegram.database.CURRENT_UID
import ru.butterbean.tut_telegram.ui.message_recycler_view.views.MessageView
import ru.butterbean.tut_telegram.utilites.AppVoicePlayer
import ru.butterbean.tut_telegram.utilites.asTime

class HolderVoiceMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {

    private val mAppVoicePlayer = AppVoicePlayer()

    private val blockUserVoiceMessage: ConstraintLayout = view.block_user_voice_message
    private val chatUserVoiceMessageTime: TextView = view.chat_user_voice_message_time
    private val chatUserBtnPlay: ImageView = view.chat_user_btn_play
    private val chatUserBtnStop: ImageView = view.chat_user_btn_stop

    private val blockReceivedVoiceMessage: ConstraintLayout = view.block_received_voice_message
    private val chatReceivedVoiceMessageTime: TextView = view.chat_received_voice_message_time
    private val chatReceivedBtnPlay: ImageView = view.chat_received_btn_play
    private val chatReceivedBtnStop: ImageView = view.chat_received_btn_stop

    override fun drawMessage(view: MessageView) {
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

    override fun onAttach(view: MessageView) {
        mAppVoicePlayer.init()
        if (view.from == CURRENT_UID) { // our message
            chatUserBtnPlay.setOnClickListener {
                chatUserBtnPlay.visibility = View.GONE
                chatUserBtnStop.visibility = View.VISIBLE
                chatUserBtnStop.setOnClickListener {
                    stop{
                        chatUserBtnStop.setOnClickListener(null)
                        chatUserBtnPlay.visibility = View.VISIBLE
                        chatUserBtnStop.visibility = View.GONE
                    }
                }
                play(view) {
                    chatUserBtnPlay.visibility = View.VISIBLE
                    chatUserBtnStop.visibility = View.GONE
                }
            }
        } else {
            chatReceivedBtnPlay.setOnClickListener {
                chatReceivedBtnPlay.visibility = View.GONE
                chatReceivedBtnStop.visibility = View.VISIBLE
                chatReceivedBtnStop.setOnClickListener {
                    stop{
                        chatReceivedBtnStop.setOnClickListener(null)
                        chatReceivedBtnPlay.visibility = View.VISIBLE
                        chatReceivedBtnStop.visibility = View.GONE
                    }
                }
                play(view) {
                    chatReceivedBtnPlay.visibility = View.VISIBLE
                    chatReceivedBtnStop.visibility = View.GONE
                }
            }
        }
    }

    private fun stop(function: () -> Unit) {
        mAppVoicePlayer.stop { function() }
    }

    private fun play(view: MessageView, function: () -> Unit) {
        mAppVoicePlayer.play(view.id, view.fileUrl) {
            function()
        }
    }

    override fun onDetach() {
        // удаляем слушатели когда вьюшки ушли с экрана
        chatUserBtnPlay.setOnClickListener(null)
        chatReceivedBtnPlay.setOnClickListener(null)
        mAppVoicePlayer.release()
    }
}