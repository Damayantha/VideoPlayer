package com.cyberkey.videoplayer.dialogs

import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.cyberkey.commons.activities.BaseSimpleActivity
import com.cyberkey.commons.extensions.getAlertDialogBuilder
import com.cyberkey.commons.extensions.getProperPrimaryColor
import com.cyberkey.commons.extensions.setupDialogStuff
import com.cyberkey.commons.helpers.VIEW_TYPE_GRID
import com.cyberkey.commons.views.MyGridLayoutManager
import com.cyberkey.videoplayer.R
import com.cyberkey.videoplayer.adapters.MediaAdapter
import com.cyberkey.videoplayer.asynctasks.GetMediaAsynctask
import com.cyberkey.videoplayer.databinding.DialogMediumPickerBinding
import com.cyberkey.videoplayer.extensions.config
import com.cyberkey.videoplayer.extensions.getCachedMedia
import com.cyberkey.videoplayer.helpers.GridSpacingItemDecoration
import com.cyberkey.videoplayer.helpers.SHOW_ALL
import com.cyberkey.videoplayer.models.Medium
import com.cyberkey.videoplayer.models.ThumbnailItem
import com.cyberkey.videoplayer.models.ThumbnailSection

@Suppress("UNCHECKED_CAST")
class PickMediumDialog(val activity: BaseSimpleActivity, val path: String, val callback: (path: String) -> Unit) {
    private var dialog: AlertDialog? = null
    private var shownMedia = ArrayList<ThumbnailItem>()
    private val binding = DialogMediumPickerBinding.inflate(activity.layoutInflater)
    private val config = activity.config
    private val viewType = config.getFolderViewType(if (config.showAll) SHOW_ALL else path)
    private var isGridViewType = viewType == VIEW_TYPE_GRID

    init {
        (binding.mediaGrid.layoutManager as MyGridLayoutManager).apply {
            orientation = if (config.scrollHorizontally && isGridViewType) RecyclerView.HORIZONTAL else RecyclerView.VERTICAL
            spanCount = if (isGridViewType) config.mediaColumnCnt else 1
        }

        binding.mediaFastscroller.updateColors(activity.getProperPrimaryColor())

        activity.getAlertDialogBuilder()
            .setPositiveButton(com.cyberkey.commons.R.string.ok, null)
            .setNegativeButton(com.cyberkey.commons.R.string.cancel, null)
            .setNeutralButton(R.string.other_folder) { _, _ -> showOtherFolder() }
            .apply {
                activity.setupDialogStuff(binding.root, this, com.cyberkey.commons.R.string.select_photo) { alertDialog ->
                    dialog = alertDialog
                }
            }

        activity.getCachedMedia(path) {
            val media = it.filter { it is Medium } as ArrayList
            if (media.isNotEmpty()) {
                activity.runOnUiThread {
                    gotMedia(media)
                }
            }
        }

        GetMediaAsynctask(activity, path, false, false, false) {
            gotMedia(it)
        }
    }

    private fun showOtherFolder() {
        PickDirectoryDialog(activity, path, true, true, false, false) {
            callback(it)
            dialog?.dismiss()
        }
    }

    private fun gotMedia(media: ArrayList<ThumbnailItem>) {
        if (media.hashCode() == shownMedia.hashCode())
            return

        shownMedia = media
        val adapter = MediaAdapter(activity, shownMedia.clone() as ArrayList<ThumbnailItem>, null, true, false, path, binding.mediaGrid) {
            if (it is Medium) {
                callback(it.path)
                dialog?.dismiss()
            }
        }

        val scrollHorizontally = config.scrollHorizontally && isGridViewType
        binding.apply {
            mediaGrid.adapter = adapter
            mediaFastscroller.setScrollVertically(!scrollHorizontally)
        }
        handleGridSpacing(media)
    }

    private fun handleGridSpacing(media: ArrayList<ThumbnailItem>) {
        if (isGridViewType) {
            val spanCount = config.mediaColumnCnt
            val spacing = config.thumbnailSpacing
            val useGridPosition = media.firstOrNull() is ThumbnailSection

            var currentGridDecoration: GridSpacingItemDecoration? = null
            if (binding.mediaGrid.itemDecorationCount > 0) {
                currentGridDecoration = binding.mediaGrid.getItemDecorationAt(0) as GridSpacingItemDecoration
                currentGridDecoration.items = media
            }

            val newGridDecoration = GridSpacingItemDecoration(spanCount, spacing, config.scrollHorizontally, config.fileRoundedCorners, media, useGridPosition)
            if (currentGridDecoration.toString() != newGridDecoration.toString()) {
                if (currentGridDecoration != null) {
                    binding.mediaGrid.removeItemDecoration(currentGridDecoration)
                }
                binding.mediaGrid.addItemDecoration(newGridDecoration)
            }
        }
    }
}
