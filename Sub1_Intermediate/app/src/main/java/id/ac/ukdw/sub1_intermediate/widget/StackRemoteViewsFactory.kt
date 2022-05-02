package id.ac.ukdw.sub1_intermediate.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import id.ac.ukdw.sub1_intermediate.Injection
import id.ac.ukdw.sub1_intermediate.R
import id.ac.ukdw.sub1_intermediate.homeStory.ListStoryItem
import id.ac.ukdw.sub1_intermediate.homeStory.StoryViewModel
import id.ac.ukdw.sub1_intermediate.homeStory.ViewModelFactory
import id.ac.ukdw.sub1_intermediate.userSession.UserPreferencesDS

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {
    private val mWidgetItems = ArrayList<ListStoryItem>()
    private lateinit var storyViewModel: StoryViewModel

    override fun onDataSetChanged() {
        val repo = Injection.provideRepository(mContext)
        val data = repo.getMapListStory()
        mWidgetItems.addAll(data.listStory)
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        val bannerImage = Glide.with(mContext)
            .asBitmap()
            .load(mWidgetItems[position].photoUrl)
            .submit().get()
        rv.setImageViewBitmap(R.id.imageView, bannerImage)

        val extras = bundleOf(
            ImagesBannerWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun onCreate() {
        //do nothing
    }

    override fun onDestroy() {
        //do nothing
    }

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1
}