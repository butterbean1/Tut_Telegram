package ru.butterbean.tut_telegram.ui.fragments.message_recycler_view.views

import ru.butterbean.tut_telegram.models.CommonModel
import ru.butterbean.tut_telegram.utilites.TYPE_MESSAGE_IMAGE

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