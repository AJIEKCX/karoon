package com.example.ajiekc.karoon.ui.newsfeed

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.ajiekc.karoon.LceState
import com.example.ajiekc.karoon.R
import com.example.ajiekc.karoon.entity.VKNewsfeed
import com.example.ajiekc.karoon.widget.BaseRecyclerAdapter

class NewsfeedAdapter(items: List<VKNewsfeed>) : BaseRecyclerAdapter<NewsfeedAdapter.RepositoryHolder, VKNewsfeed>(items) {

    private var mListener: RepeatButtonClickListener? = null

    abstract class RepositoryHolder(view: View) : RecyclerView.ViewHolder(view)

    class RepositoryItemHolder(val view: View) : RepositoryHolder(view) {
        private val userNameView: TextView = view.findViewById(R.id.user_name)
        private val postTextView: TextView = view.findViewById(R.id.post_text)
        private val userAvatarView: ImageView = view.findViewById(R.id.user_avatar)

        fun bind(news: VKNewsfeed) {
            userNameView.text = "Vasya pupkin"
            postTextView.text = news.text
            //Picasso.get().loadRoundedImage(news.avatarUrl, userAvatarView, R.drawable.ic_user)
        }
    }

    class RepositoryLoadingHolder(val view: View) : RepositoryHolder(view)

    class RepositoryErrorHolder(val view: View) : RepositoryHolder(view) {
        private val repeatButton = view.findViewById<ImageButton>(R.id.repeat_button)

        fun setRepeatButtonClickListener(listener: RepeatButtonClickListener?) {
            repeatButton.setOnClickListener {
                listener?.onRepeatButtonClick()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
/*        if (getItem(position).type == LceState.LOADING.name) {
            return LceState.LOADING.ordinal
        }
        if (getItem(position).type == LceState.ERROR.name) {
            return LceState.ERROR.ordinal
        }*/

        return LceState.CONTENT.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryHolder {
        when (viewType) {
            LceState.LOADING.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_loading, parent, false)
                return RepositoryLoadingHolder(view)
            }
            LceState.ERROR.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_error, parent, false)
                return RepositoryErrorHolder(view)
            }
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vk_news, parent, false)
        return RepositoryItemHolder(view)
    }

    override fun onBindViewHolder(holder: RepositoryHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        when (getItemViewType(position)) {
            LceState.CONTENT.ordinal -> {
                val user = getItem(position)
                (holder as RepositoryItemHolder).bind(user)
            }
            LceState.ERROR.ordinal -> {
                (holder as RepositoryErrorHolder).setRepeatButtonClickListener(mListener)
            }
        }
    }

/*    fun isLastItemUser(): Boolean {
        val user = getLastItem()
        return user?.type != LceState.LOADING.name && user?.type != LceState.ERROR.name
    }*/

    fun setOnRepeatButtonClickListener(listener: RepeatButtonClickListener) {
        mListener = listener
    }

    interface RepeatButtonClickListener {
        fun onRepeatButtonClick()
    }
}