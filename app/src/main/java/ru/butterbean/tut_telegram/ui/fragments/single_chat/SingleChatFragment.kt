package ru.butterbean.tut_telegram.ui.fragments.single_chat

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.MotionEvent
import android.view.View
import android.widget.AbsListView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_info.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.butterbean.tut_telegram.R
import ru.butterbean.tut_telegram.models.CommonModel
import ru.butterbean.tut_telegram.models.UserModel
import ru.butterbean.tut_telegram.ui.fragments.BaseFragment
import ru.butterbean.tut_telegram.utilites.*

class SingleChatFragment(private val contact: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mListenerInfoToolbar: AppValueEventListener
    private lateinit var mReceivingUser: UserModel
    private lateinit var mToolbarInfo: View
    private lateinit var mRefUser: DatabaseReference
    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessagesListener: AppChildEventListener
    private var mCountMessages = 10
    private var mIsScrolling = false
    private var mSmoothScrollToPosition = true
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onResume() {
        super.onResume()
        initFields()
        initToolbar()
        initRecycleView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initFields() {
        mSwipeRefreshLayout = chat_swipe_refresh
        mLayoutManager = LinearLayoutManager(this.context)
        chat_input_message.addTextChangedListener(AppTextWatcher{
            val string = chat_input_message.text.toString()
            if (string.isEmpty()||string=="Запись"){
                chat_btn_send_message.visibility = View.GONE
                chat_btn_attach.visibility = View.VISIBLE
                chat_btn_voice.visibility = View.VISIBLE
            }else{
                chat_btn_send_message.visibility = View.VISIBLE
                chat_btn_attach.visibility = View.GONE
                chat_btn_voice.visibility = View.GONE
            }
        })
        chat_btn_attach.setOnClickListener{ attachFile() }

        CoroutineScope(Dispatchers.IO).launch {
            chat_btn_voice.setOnTouchListener { v, event ->
                if (checkPremission(RECORD_AUDIO)){
                    when (event.action){
                        MotionEvent.ACTION_DOWN -> {
                            //TODO record
                            chat_input_message.setText("Запись")
                            chat_btn_voice.setColorFilter(ContextCompat.getColor(APP_ACTIVITY,R.color.primary))
                        }
                        MotionEvent.ACTION_UP -> {
                            //TODO stop record
                            chat_input_message.setText("")
                            chat_btn_voice.colorFilter = null
                        }
                    }
                }
                true
            }
        }
    }

    private fun attachFile() {
        CropImage.activity()
            .setAspectRatio(1,1)
            .setRequestedSize(600,600)
            .start(APP_ACTIVITY,this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode== Activity.RESULT_OK
            && data!=null){
            val uri = CropImage.getActivityResult(data)?.uriContent!!
            val messageKey = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
                .child(contact.id).push().key.toString()
            val path = REF_STORAGE_ROOT
                .child(FOLDER_MESSAGES_IMAGES)
                .child(messageKey)

            putImageToStorage(uri,path){
                getUrlFromStorage(path){

                    sendMessageAsImage(contact.id,it,messageKey)
                    mSmoothScrollToPosition = true
                }
            }

        }else{
            showToast("Ошибка прикрепления фото $resultCode")
        }
    }

     private fun initRecycleView() {
        mRecyclerView = chat_recycle_view
        mAdapter = SingleChatAdapter()
        mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES)
            .child(CURRENT_UID)
            .child(contact.id)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.layoutManager = mLayoutManager
        mMessagesListener = AppChildEventListener { snapshot ->
            val message = snapshot.getCommonModel()
            if (mSmoothScrollToPosition){
                mAdapter.addItemToBottom(message){
                    mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
                }
            }else{
                mAdapter.addItemToTop(message){
                    mSwipeRefreshLayout.isRefreshing = false
                }
            }
        }
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling && dy < 0 && mLayoutManager.findFirstVisibleItemPosition()<=3) {
                    println(mRecyclerView.recycledViewPool.getRecycledViewCount(0))
                    updateData()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true
                }
            }

        })

        mSwipeRefreshLayout.setOnRefreshListener { updateData() }
    }

    private fun updateData() {
        mSmoothScrollToPosition = false
        mIsScrolling = false
        mCountMessages += 10
        mRefMessages.removeEventListener(mMessagesListener)
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
    }

    private fun initToolbar() {
        mToolbarInfo = APP_ACTIVITY.mToolbar.toolbar_info
        mToolbarInfo.visibility = View.VISIBLE
        mListenerInfoToolbar = AppValueEventListener {
            mReceivingUser = it.getUserModel()
            initInfoToolbar()
        }
        mRefUser = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
        mRefUser.addValueEventListener(mListenerInfoToolbar)
        chat_btn_send_message.setOnClickListener {
            mSmoothScrollToPosition = true
            val message = chat_input_message.text.toString()
            if (message.isEmpty()) {
                showToast("Введите сообщение",true)
            } else sendMessage(message, contact.id, TYPE_TEXT) {
                chat_input_message.setText("")
            }
        }
    }


    private fun initInfoToolbar() {
        if (mReceivingUser.fullname.isEmpty()) {
            mToolbarInfo.toolbar_chat_contact_fullname.text = contact.fullname
        } else {
            mToolbarInfo.toolbar_chat_contact_fullname.text = mReceivingUser.fullname
        }
        mToolbarInfo.toolbar_chat_contact_image.downloadAndSetImage(mReceivingUser.photoUrl)

        mToolbarInfo.toolbar_chat_contact_status.text = mReceivingUser.state

    }

    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE
        mRefUser.removeEventListener(mListenerInfoToolbar)
        mRefMessages.removeEventListener(mMessagesListener)
    }
}