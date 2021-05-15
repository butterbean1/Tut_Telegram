package ru.butterbean.tut_telegram.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_settings.*
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.activities.RegisterActivity
import ru.butterbean.tut_telegram.utilites.*

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {
    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
    }

    private fun initFields() {
        settings_bio.text = USER.bio
        settings_full_name.text = USER.fullname
        settings_username.text = USER.username
        settings_status.text = USER.state
        settings_phone_number.text = USER.phone
        settings_btn_change_username.setOnClickListener {
            replaceFragment(ChangeUsernameFragment())
        }
        settings_btn_change_bio.setOnClickListener {
            replaceFragment(ChangeBioFragment())
        }
        settings_btn_change_photo.setOnClickListener {
            changePhotoUser()
        }
        settings_user_photo.downloadAndSetImage(USER.photoUrl)
    }

    private fun changePhotoUser() {
        CropImage.activity()
            .setAspectRatio(1,1)
            .setRequestedSize(600,600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY,this)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_exit -> {
                AppStates.updateState(AppStates.OFFLINE)
                AUTH.signOut()
                (APP_ACTIVITY).replaceActivity(RegisterActivity())
            }
            R.id.settings_menu_change_name -> replaceFragment(ChangeNameFragment())
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode== RESULT_OK
            && data!=null){
            val uri = CropImage.getActivityResult(data)?.uriContent!!
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
                .child(CURRENT_UID)

            putImageToStorage(uri,path){
                getUrlFromStorage(path){
                    putUrlToDatabase(it){
                        settings_user_photo.downloadAndSetImage(it)
                        showToast(getString(R.string.data_updated))
                        USER.photoUrl = it
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                    }
                }
            }

        }else{
            showToast("Ошибка прикрепления фото $resultCode")
        }
    }

}