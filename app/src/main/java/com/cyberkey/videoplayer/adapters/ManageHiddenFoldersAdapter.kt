package com.cyberkey.videoplayer.adapters

import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.cyberkey.commons.activities.BaseSimpleActivity
import com.cyberkey.commons.adapters.MyRecyclerViewAdapter
import com.cyberkey.commons.extensions.getProperTextColor
import com.cyberkey.commons.extensions.isPathOnSD
import com.cyberkey.commons.extensions.setupViewBackground
import com.cyberkey.commons.interfaces.RefreshRecyclerViewListener
import com.cyberkey.commons.views.MyRecyclerView
import com.cyberkey.videoplayer.R
import com.cyberkey.videoplayer.databinding.ItemManageFolderBinding
import com.cyberkey.videoplayer.extensions.removeNoMedia

class ManageHiddenFoldersAdapter(
    activity: BaseSimpleActivity, var folders: ArrayList<String>, val listener: RefreshRecyclerViewListener?,
    recyclerView: MyRecyclerView, itemClick: (Any) -> Unit
) : MyRecyclerViewAdapter(activity, recyclerView, itemClick) {

    init {
        setupDragListener(true)
    }

    override fun getActionMenuId() = R.menu.cab_hidden_folders

    override fun prepareActionMode(menu: Menu) {}

    override fun actionItemPressed(id: Int) {
        when (id) {
            R.id.cab_unhide -> tryUnhideFolders()
        }
    }

    override fun getSelectableItemCount() = folders.size

    override fun getIsItemSelectable(position: Int) = true

    override fun getItemSelectionKey(position: Int) = folders.getOrNull(position)?.hashCode()

    override fun getItemKeyPosition(key: Int) = folders.indexOfFirst { it.hashCode() == key }

    override fun onActionModeCreated() {}

    override fun onActionModeDestroyed() {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return createViewHolder(ItemManageFolderBinding.inflate(layoutInflater, parent, false).root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val folder = folders[position]
        holder.bindView(folder, true, true) { itemView, adapterPosition ->
            setupView(itemView, folder)
        }
        bindViewHolder(holder)
    }

    override fun getItemCount() = folders.size

    private fun getSelectedItems() = folders.filter { selectedKeys.contains(it.hashCode()) } as ArrayList<String>

    private fun setupView(view: View, folder: String) {
        ItemManageFolderBinding.bind(view).apply {
            root.setupViewBackground(activity)
            manageFolderHolder.isSelected = selectedKeys.contains(folder.hashCode())
            manageFolderTitle.apply {
                text = folder
                setTextColor(context.getProperTextColor())
            }
        }
    }

    private fun tryUnhideFolders() {
        val removeFolders = ArrayList<String>(selectedKeys.size)

        val sdCardPaths = ArrayList<String>()
        getSelectedItems().forEach {
            if (activity.isPathOnSD(it)) {
                sdCardPaths.add(it)
            }
        }

        if (sdCardPaths.isNotEmpty()) {
            activity.handleSAFDialog(sdCardPaths.first()) {
                if (it) {
                    unhideFolders(removeFolders)
                }
            }
        } else {
            unhideFolders(removeFolders)
        }
    }

    private fun unhideFolders(removeFolders: ArrayList<String>) {
        val position = getSelectedItemPositions()
        getSelectedItems().forEach {
            removeFolders.add(it)
            activity.removeNoMedia(it)
        }

        folders.removeAll(removeFolders)
        removeSelectedItems(position)
        if (folders.isEmpty()) {
            listener?.refreshItems()
        }
    }
}
