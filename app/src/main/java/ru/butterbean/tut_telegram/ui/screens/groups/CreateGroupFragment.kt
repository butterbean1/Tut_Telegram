package ru.butterbean.tut_telegram.ui.screens.groups

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_create_group.*
import kotlinx.android.synthetic.main.fragment_settings.*
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.database.*
import ru.butterbean.tut_telegram.models.CommonModel
import ru.butterbean.tut_telegram.ui.screens.base.BaseFragment
import ru.butterbean.tut_telegram.ui.screens.main_list.MainListFragment
import ru.butterbean.tut_telegram.utilites.*

class CreateGroupFragment(private var listContacts:List<CommonModel>):BaseFragment(R.layout.fragment_create_group) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var  mAdapter:AddContactsAdapter
    private var mUri = Uri.EMPTY

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.create_group)
        hideKeyboard()
        initRecyclerView()
        create_group_btn_complete.setOnClickListener {
            val nameGroup = create_group_input_name.text.toString()
            if (nameGroup.isEmpty()){
                showToast("Введите имя")
            }else{
                createGroupToDatabase(nameGroup,mUri,listContacts){
                    replaceFragment(MainListFragment())
                }
            }
        }
        create_group_photo.setOnClickListener { addPhoto() }
        create_group_input_name.requestFocus()
        create_group_counts.text = getPlurals(listContacts.size)
    }

    private fun addPhoto() {
        CropImage.activity()
            .setAspectRatio(1,1)
            .setRequestedSize(250,250)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY,this)
    }

    private fun initRecyclerView() {
        mRecyclerView = create_group_recycle_view
        mAdapter = AddContactsAdapter()

        mRecyclerView.adapter = mAdapter
        listContacts.forEach {
            mAdapter.updateListItems(it)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode== Activity.RESULT_OK
            && data!=null){
            mUri = CropImage.getActivityResult(data)?.uriContent!!
            create_group_photo.setImageURI(mUri)

        }else{
            showToast("Ошибка прикрепления фото $resultCode")
        }
    }
}