package com.cyberkey.videoplayer.asynctasks

import android.content.Context
import com.cyberkey.commons.helpers.FAVORITES
import com.cyberkey.commons.helpers.SORT_BY_DATE_MODIFIED
import com.cyberkey.commons.helpers.SORT_BY_DATE_TAKEN
import com.cyberkey.commons.helpers.SORT_BY_SIZE
import com.cyberkey.videoplayer.extensions.config
import com.cyberkey.videoplayer.extensions.getFavoritePaths
import com.cyberkey.videoplayer.helpers.*
import com.cyberkey.videoplayer.models.Medium
import com.cyberkey.videoplayer.models.ThumbnailItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class GetMediaAsynctask(
    val context: Context, val mPath: String, val isPickImage: Boolean = false, val isPickVideo: Boolean = false,
    val showAll: Boolean, val callback: ((media: ArrayList<ThumbnailItem>) -> Unit)?
) {
    //AsyncTask<Void, Void, ArrayList<ThumbnailItem>>() {
    private val mediaFetcher = MediaFetcher(context)

    // override fun doInBackground(vararg params: Void): ArrayList<ThumbnailItem> {
    suspend fun getMedia(): ArrayList<ThumbnailItem> = withContext(Dispatchers.IO) {
        val pathToUse = if (showAll) SHOW_ALL else mPath
        val folderGrouping = context.config.getFolderGrouping(pathToUse)
        val folderSorting = context.config.getFolderSorting(pathToUse)
        val getProperDateTaken = folderSorting and SORT_BY_DATE_TAKEN != 0 ||
            folderGrouping and GROUP_BY_DATE_TAKEN_DAILY != 0 ||
            folderGrouping and GROUP_BY_DATE_TAKEN_MONTHLY != 0

        val getProperLastModified = folderSorting and SORT_BY_DATE_MODIFIED != 0 ||
            folderGrouping and GROUP_BY_LAST_MODIFIED_DAILY != 0 ||
            folderGrouping and GROUP_BY_LAST_MODIFIED_MONTHLY != 0

        val getProperFileSize = folderSorting and SORT_BY_SIZE != 0
        val favoritePaths = context.getFavoritePaths()
        val getVideoDurations = context.config.showThumbnailVideoDuration
        val lastModifieds = if (getProperLastModified) mediaFetcher.getLastModifieds() else HashMap()
        val dateTakens = if (getProperDateTaken) mediaFetcher.getDateTakens() else HashMap()

        val media = if (showAll) {
            val foldersToScan = mediaFetcher.getFoldersToScan().filter { it != RECYCLE_BIN && it != FAVORITES && !context.config.isFolderProtected(it) }
            val media = ArrayList<Medium>()
            foldersToScan.forEach {
                val newMedia = mediaFetcher.getFilesFrom(
                    it, isPickImage, isPickVideo, getProperDateTaken, getProperLastModified, getProperFileSize,
                    favoritePaths, getVideoDurations, lastModifieds, dateTakens.clone() as HashMap<String, Long>, null
                )
                media.addAll(newMedia)
            }

            mediaFetcher.sortMedia(media, context.config.getFolderSorting(SHOW_ALL))
            media
        } else {
            mediaFetcher.getFilesFrom(
                mPath, isPickImage, isPickVideo, getProperDateTaken, getProperLastModified, getProperFileSize, favoritePaths,
                getVideoDurations, lastModifieds, dateTakens, null
            )
        }

        return@withContext mediaFetcher.groupMedia(media, pathToUse)
    }



    fun stopFetching() {

        mediaFetcher.shouldStop = true

    }
}
