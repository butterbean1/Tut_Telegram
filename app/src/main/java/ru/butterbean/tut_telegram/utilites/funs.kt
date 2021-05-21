package ru.butterbean.tut_telegram.utilites

import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import ru.butterbean.tut_telegram.MainActivity
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.database.updatePhonesToDatabase
import ru.butterbean.tut_telegram.models.CommonModel
import java.text.SimpleDateFormat
import java.util.*

fun showToast(message: String, centerGravity: Boolean = false) {
    val toast = Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT)
    if (centerGravity) {
        toast.setGravity(Gravity.CENTER, 0, 0)
    }
    toast.show()
}
fun hideKeyboard(){
    val imm = APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken,0)
}

fun restartActivity() {
    val intent = Intent(APP_ACTIVITY, MainActivity::class.java)
    APP_ACTIVITY.startActivity(intent)
    APP_ACTIVITY.finish()
}

fun replaceFragment(fragment: Fragment, addStack: Boolean = true) {
    if (addStack) {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.data_container, fragment
            )
            .commit()
    } else {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .replace(
                R.id.data_container, fragment
            )
            .commit()
    }
}

fun ImageView.downloadAndSetImage(url: String){
    Picasso.get()
        .load(url)
        .fit()
        .placeholder(R.drawable.def_photo)
        .into(this)
}

fun initContacts() {
    if (checkPremission(READ_CONTACTS)) {
        var arrayContacts = arrayListOf<CommonModel>()
        val cursor = APP_ACTIVITY.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
        )
        cursor?.let {
            while (it.moveToNext()) {
                val fullName =
                    it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phone =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val newModel = CommonModel()
                newModel.fullname = fullName
                newModel.phone = phone.replace(Regex("[\\s,-]"), "")
                arrayContacts.add(newModel)
            }
        }
        cursor?.close()
        updatePhonesToDatabase(arrayContacts)
    }

}

fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}

