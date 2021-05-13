package ru.butterbean.tut_telegram.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.contact_item.view.*
import kotlinx.android.synthetic.main.fragment_contacts.*
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.models.CommonModel
import ru.butterbean.tut_telegram.utilites.*

class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter:FirebaseRecyclerAdapter<CommonModel,ContactsHolder>
    private lateinit var mRefContacts:DatabaseReference
    private lateinit var mRefUsers:DatabaseReference

    class ContactsHolder(view: View):RecyclerView.ViewHolder(view) {
        val name = view.contact_fullname
        val status = view.contact_status
        val photo = view.contact_photo
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Контакты"
        initRecycleView()
    }

    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()
    }

    private fun initRecycleView() {
        mRecyclerView = contacts_recycle_view
        mRefContacts = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(mRefContacts,CommonModel::class.java)
            .build()

        mAdapter = object: FirebaseRecyclerAdapter<CommonModel,ContactsHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_item,parent,false)
                return ContactsHolder(view)
            }

            override fun onBindViewHolder(
                holder: ContactsHolder,
                position: Int,
                model: CommonModel
            ) {
                mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)
                mRefUsers.addValueEventListener(AppValueEventListener{
                    val contact = it.getCommonModel()
                    holder.name.text = contact.fullname
                    holder.status.text = contact.state
                    holder.photo.downloadAndSetImage(contact.photoUrl)
                })
            }

        }
        mRecyclerView.adapter = mAdapter
        mAdapter.startListening()
    }
}



