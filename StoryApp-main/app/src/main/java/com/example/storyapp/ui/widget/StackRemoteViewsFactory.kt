package com.example.storyapp.ui.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.di.Injection

internal class StackRemoteViewsFactory(
    private val mContext: Context,
) :
    RemoteViewsService
    .RemoteViewsFactory {
    private val mWidgetItems = ArrayList<ListStoryItem>()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        val storyRepository = Injection.provideRepository(mContext)
        val responseBody = storyRepository.getStories()
        mWidgetItems.addAll(responseBody.listStory)
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        val story = mWidgetItems[position]

        val resource = Glide.with(mContext)
            .asBitmap()
            .load(story.photoUrl)
            .submit()
            .get()
        rv.setImageViewBitmap(R.id.iv_widget_item, resource)

        val extras = bundleOf(
            StoriesWidget.EXTRA_ITEM to story.name
        )

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.iv_widget_item, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}