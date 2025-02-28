package com.github.libretube.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.libretube.R
import com.github.libretube.api.SubscriptionHelper
import com.github.libretube.databinding.ChannelSubscriptionRowBinding
import com.github.libretube.extensions.toID
import com.github.libretube.obj.Subscription
import com.github.libretube.util.ImageHelper
import com.github.libretube.util.NavigationHelper

class SubscriptionChannelAdapter(private val subscriptions: MutableList<Subscription>) :
    RecyclerView.Adapter<SubscriptionChannelViewHolder>() {

    override fun getItemCount(): Int {
        return subscriptions.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
        SubscriptionChannelViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ChannelSubscriptionRowBinding.inflate(layoutInflater, parent, false)
        return SubscriptionChannelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriptionChannelViewHolder, position: Int) {
        val subscription = subscriptions[position]
        var subscribed = true

        holder.binding.apply {
            subscriptionChannelName.text = subscription.name
            ImageHelper.loadImage(subscription.avatar, subscriptionChannelImage)
            root.setOnClickListener {
                NavigationHelper.navigateChannel(root.context, subscription.url)
            }
            subscriptionSubscribe.setOnClickListener {
                val channelId = subscription.url.toID()
                if (subscribed) {
                    subscriptionSubscribe.text = root.context.getString(R.string.subscribe)
                    SubscriptionHelper.unsubscribe(channelId)
                    subscribed = false
                } else {
                    subscriptionSubscribe.text =
                        root.context.getString(R.string.unsubscribe)
                    SubscriptionHelper.subscribe(channelId)
                    subscribed = true
                }
            }
        }
    }
}

class SubscriptionChannelViewHolder(val binding: ChannelSubscriptionRowBinding) :
    RecyclerView.ViewHolder(binding.root)
