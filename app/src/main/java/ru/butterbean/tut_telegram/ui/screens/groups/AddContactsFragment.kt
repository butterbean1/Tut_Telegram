package ru.butterbean.tut_telegram.ui.screens.groups

import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_add_contacts.*
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.database.*
import ru.butterbean.tut_telegram.models.CommonModel
import ru.butterbean.tut_telegram.ui.screens.base.BaseFragment
import ru.butterbean.tut_telegram.utilites.*

class AddContactsFragment : BaseFragment(R.layout.fragment_add_contacts) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var  mAdapter:AddContactsAdapter
    private val mRefContactsList = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
    private val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    private var mListItems = listOf<CommonModel>()

    override fun onResume() {
        super.onResume()
        listContacts.clear()
        APP_ACTIVITY.title = "Добавить участников"
        hideKeyboard()
        initRecyclerView()
        add_contacts_btn_next.setOnClickListener {
            if (listContacts.isEmpty()){
                showToast("Добавьте участников")
            }else{
                replaceFragment(CreateGroupFragment(listContacts))

            }
        }
    }

    private fun initRecyclerView() {
        mRecyclerView = add_contacts_recycle_view
        mAdapter = AddContactsAdapter()

        //
        mRefContactsList.addListenerForSingleValueEvent(AppValueEventListener{ mainListSnapshot->
            mListItems = mainListSnapshot.children.map { it.getCommonModel() }
            mListItems.forEach {model ->

                //
                mRefUsers.child(model.id).addListenerForSingleValueEvent(AppValueEventListener{userSnapshot ->
                    val newModel = userSnapshot.getCommonModel()
                    //
                    mRefMessages.child(model.id).limitToLast(1)
                        .addListenerForSingleValueEvent(AppValueEventListener{lastMessageSnapshot ->
                        val tempList = lastMessageSnapshot.children.map { it.getCommonModel() }
                        if(tempList.isEmpty()){
                                newModel.lastMessage = "Чат очищен"
                            }else {
                                newModel.lastMessage = tempList[0].text
                            }
                        if (newModel.fullname.isEmpty()){
                            newModel.fullname = newModel.phone
                        }
                        mAdapter.updateListItems(newModel)
                    })
                })
            }

        })
        mRecyclerView.adapter = mAdapter
    }

    companion object{
        val listContacts = mutableListOf<CommonModel>()
    }
}