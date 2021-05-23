package ru.butterbean.tut_telegram.ui.screens.main_list

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main_list.*
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.database.*
import ru.butterbean.tut_telegram.models.CommonModel
import ru.butterbean.tut_telegram.utilites.APP_ACTIVITY
import ru.butterbean.tut_telegram.utilites.AppValueEventListener
import ru.butterbean.tut_telegram.utilites.hideKeyboard
import ru.butterbean.tut_telegram.utilites.showToast

class MainListFragment : Fragment(R.layout.fragment_main_list) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var  mAdapter:MainListAdapter
    private val mRefMainList = REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID)
    private val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    private var mListItems = listOf<CommonModel>()

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Телега"
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView = main_list_recycle_view
        mAdapter = MainListAdapter()

        //
        mRefMainList.addListenerForSingleValueEvent(AppValueEventListener{mainListSnapshot->
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
}