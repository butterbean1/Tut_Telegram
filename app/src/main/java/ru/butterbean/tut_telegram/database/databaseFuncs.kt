package ru.butterbean.tut_telegram.database

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.models.CommonModel
import ru.butterbean.tut_telegram.models.UserModel
import ru.butterbean.tut_telegram.utilites.APP_ACTIVITY
import ru.butterbean.tut_telegram.utilites.AppValueEventListener
import ru.butterbean.tut_telegram.utilites.TYPE_GROUP
import ru.butterbean.tut_telegram.utilites.showToast
import java.io.File
import java.util.HashMap

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    USER = UserModel()
    CURRENT_UID = AUTH.currentUser?.uid.toString()
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}

inline fun putUrlToDatabase(url: String, crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .child(CHILD_PHOTO_URL).setValue(url)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener { function(it.toString()) }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun putFileToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun initUser(crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .addListenerForSingleValueEvent(AppValueEventListener {
            USER = it.getValue(UserModel::class.java) ?: UserModel()
            if (USER.username.isEmpty()) {
                USER.username = CURRENT_UID
            }
            function()
        }
        )
}

fun updatePhonesToDatabase(arrayContacts: ArrayList<CommonModel>) {
    if (AUTH.currentUser != null) {
        REF_DATABASE_ROOT.child(NODE_PHONES).addListenerForSingleValueEvent(AppValueEventListener {
            it.children.forEach { snapshot ->
                arrayContacts.forEach { contact ->
                    if (snapshot.key == contact.phone) {
                        REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
                            .child(snapshot.value.toString()).child(CHILD_ID)
                            .setValue(snapshot.value.toString())
                            .addOnFailureListener { showToast(it.message.toString()) }
                        REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
                            .child(snapshot.value.toString()).child(CHILD_FULLNAME)
                            .setValue(contact.fullname)
                            .addOnFailureListener { showToast(it.message.toString()) }
                    }
                }
            }
        })
    }
}

fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()

fun DataSnapshot.getUserModel(): UserModel =
    this.getValue(UserModel::class.java) ?: UserModel()

fun sendMessage(message: String, receivingUserId: String, typeText: String, function: () -> Unit) {
    val refDialogUser = "$NODE_MESSAGES/$CURRENT_UID/$receivingUserId"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserId/$CURRENT_UID"
    val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key
    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = CURRENT_UID
    mapMessage[CHILD_TYPE] = typeText
    mapMessage[CHILD_TEXT] = message
    mapMessage[CHILD_ID] = messageKey.toString()
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialog)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }

}

fun sendMessageAsFile(
    receivingUserId: String,
    fileUrl: String,
    messageKey: String,
    typeMessage: String,
    filename: String = ""
) {
    val refDialogUser = "$NODE_MESSAGES/$CURRENT_UID/$receivingUserId"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserId/$CURRENT_UID"
    val mapMessage = hashMapOf<String, Any>()

    mapMessage[CHILD_FROM] = CURRENT_UID
    mapMessage[CHILD_TYPE] = typeMessage
    mapMessage[CHILD_ID] = messageKey
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
    mapMessage[CHILD_FILE_URL] = fileUrl
    mapMessage[CHILD_TEXT] = filename

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialog)
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun updateCurrentUsername(newUsername: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_USERNAME)
        .setValue(newUsername)
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.data_updated))
            deleteOldUserName(newUsername)
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}

private fun deleteOldUserName(newUsername: String) {
    REF_DATABASE_ROOT.child(NODE_USERNAMES).child(USER.username).removeValue()
        .addOnSuccessListener {
//                    showToast(getString(R.string.data_updated))
            APP_ACTIVITY.supportFragmentManager.popBackStack()
            USER.username = newUsername
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun setBioToDatabase(newBio: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_BIO).setValue(newBio)
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.data_updated), true)
            USER.bio = newBio
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun getMessageKey(id: String) = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    .child(id).push().key.toString()

fun uploadFileToStorage(
    uri: Uri,
    messageKey: String,
    receivedID: String,
    typeMessage: String,
    filename: String = ""
) {
    val path = REF_STORAGE_ROOT.child(FOLDER_FILES).child(messageKey)
    putFileToStorage(uri, path) {
        getUrlFromStorage(path) {
            sendMessageAsFile(receivedID, it, messageKey, typeMessage, filename)
        }
    }
}

fun setNameToDatabase(fullname: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_FULLNAME)
        .setValue(fullname)
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.data_updated))
            USER.fullname = fullname
            APP_ACTIVITY.mAppDrawer.updateHeader()
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun getFileFromStorage(mFile: File, fileUrl: String, function: () -> Unit) {
    val path = REF_STORAGE_ROOT.storage.getReferenceFromUrl(fileUrl)
    path.getFile(mFile)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun saveToMainList(id: String, typeChat: String) {
    val refUser = "$NODE_MAIN_LIST/$CURRENT_UID/$id"
    val refReceived = "$NODE_MAIN_LIST/$id/$CURRENT_UID"

    val mapUser = hashMapOf<String, Any>()
    val mapReceived = hashMapOf<String, Any>()

    mapUser[CHILD_ID] = id
    mapUser[CHILD_TYPE] = typeChat

    mapReceived[CHILD_ID] = CURRENT_UID
    mapReceived[CHILD_TYPE] = typeChat

    val commonMap = hashMapOf<String, Any>()
    commonMap[refUser] = mapUser
    commonMap[refReceived] = mapReceived

    REF_DATABASE_ROOT.updateChildren(commonMap)
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun deleteChat(id: String, function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID).child(id).removeValue()
        .addOnFailureListener { showToast(it.message.toString()) }
        .addOnSuccessListener { function() }
}

fun clearChat(id: String, function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID).child(id)
        .removeValue()
        .addOnFailureListener { showToast(it.message.toString()) }
        .addOnSuccessListener {
            REF_DATABASE_ROOT.child(NODE_MESSAGES).child(id)
                .child(CURRENT_UID)
                .removeValue()
                .addOnSuccessListener { function() }
                .addOnFailureListener { showToast(it.message.toString()) }
        }
}


fun createGroupToDatabase(
    nameGroup: String,
    uri: Uri,
    listContacts: List<CommonModel>,
    function: () -> Unit
) {
    val keyGroup = REF_DATABASE_ROOT.child(NODE_GROUPS).push().key.toString()
    val path = REF_DATABASE_ROOT.child(NODE_GROUPS).child(keyGroup)

    val pathStorage = REF_STORAGE_ROOT.child(FOLDER_GROUPS_IMAGE).child(keyGroup)

    val mapData = hashMapOf<String, Any>()
    mapData[CHILD_ID] = keyGroup
    mapData[CHILD_FULLNAME] = nameGroup
    mapData[CHILD_PHOTO_URL] = "empty"

    val mapMembers = hashMapOf<String, Any>()
    listContacts.forEach { contact ->
        mapMembers[contact.id] = USER_MEMBER
    }
    mapMembers[CURRENT_UID] = USER_CREATOR // добавляем себя в качестве создателя

    mapData[NODE_MEMBERS] = mapMembers

    path.updateChildren(mapData)
        .addOnSuccessListener {

            if (uri != Uri.EMPTY) {

                putFileToStorage(uri, pathStorage) {
                    getUrlFromStorage(pathStorage) { url ->

                        path.child(CHILD_PHOTO_URL).setValue(url)
                        addGroupsToMainList(mapData,listContacts){
                            function()
                        }
                    }
                }
            }else{
                addGroupsToMainList(mapData,listContacts){
                    function()
                }
            }

        }
        .addOnFailureListener { showToast(it.message.toString()) }


}

fun addGroupsToMainList(
    mapData: HashMap<String, Any>,
    listContacts: List<CommonModel>,
    function: () -> Unit
) {
    val path = REF_DATABASE_ROOT.child(NODE_MAIN_LIST)
    val map = hashMapOf<String,Any>()
    map[CHILD_ID] = mapData[CHILD_ID].toString()
    map[CHILD_TYPE] = TYPE_GROUP
    listContacts.forEach {
        path.child(it.id).child(map[CHILD_ID].toString()).updateChildren(map)
    }

    path.child(CURRENT_UID).child(map[CHILD_ID].toString()).updateChildren(map)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun sendMessageToGroup(message: String, groupId: String, typeText: String, function: () -> Unit) {

    val refMessages = "$NODE_GROUPS/$groupId/$NODE_MESSAGES"
    val messageKey = REF_DATABASE_ROOT.child(refMessages).push().key
    val mapMessage = hashMapOf<String, Any>()

    mapMessage[CHILD_FROM] = CURRENT_UID
    mapMessage[CHILD_TYPE] = typeText
    mapMessage[CHILD_TEXT] = message
    mapMessage[CHILD_ID] = messageKey.toString()
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP

    REF_DATABASE_ROOT.child(refMessages).child(messageKey.toString())
        .updateChildren(mapMessage)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }

}
