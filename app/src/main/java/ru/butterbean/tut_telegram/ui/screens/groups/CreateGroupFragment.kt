package ru.butterbean.tut_telegram.ui.screens.groups

import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_create_group.*
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.models.CommonModel
import ru.butterbean.tut_telegram.ui.screens.base.BaseFragment
import ru.butterbean.tut_telegram.utilites.APP_ACTIVITY
import ru.butterbean.tut_telegram.utilites.hideKeyboard
import ru.butterbean.tut_telegram.utilites.showToast

class CreateGroupFragment(private var listContacts:List<CommonModel>):BaseFragment(R.layout.fragment_create_group) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var  mAdapter:AddContactsAdapter

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.create_group)
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
        initRecyclerView()
        create_group_btn_complete.setOnClickListener {
            showToast("DONE")
        }
        create_group_input_name.requestFocus()
    }

    private fun initRecyclerView() {
        mRecyclerView = create_group_recycle_view
        mAdapter = AddContactsAdapter()

        mRecyclerView.adapter = mAdapter
        listContacts.forEach {
            mAdapter.updateListItems(it)
        }
    }
}