package ru.butterbean.tut_telegram.ui.message_recycler_view.views

import ru.butterbean.tut_telegram.models.CommonModel
import ru.butterbean.tut_telegram.utilites.TYPE_MESSAGE_FILE
import ru.butterbean.tut_telegram.utilites.TYPE_MESSAGE_IMAGE
import ru.butterbean.tut_telegram.utilites.TYPE_MESSAGE_VOICE

class AppViewFactory {
    companion object{
        fun getView(message:CommonModel):MessageView{
            return when (message.type){
                TYPE_MESSAGE_IMAGE -> ViewImageMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl
                )
                TYPE_MESSAGE_VOICE -> ViewVoiceMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl
                )
                TYPE_MESSAGE_FILE -> ViewFileMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl,
                    message.text
                )
                else -> ViewTextMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl,
                    message.text
                )
            }
        }
    }
}