package com.cyberkey.videoplayer.activities

import android.os.Bundle
import com.cyberkey.commons.dialogs.FilePickerDialog
import com.cyberkey.commons.extensions.beVisibleIf
import com.cyberkey.commons.extensions.getProperTextColor
import com.cyberkey.commons.extensions.viewBinding
import com.cyberkey.commons.helpers.NavigationIcon
import com.cyberkey.commons.helpers.ensureBackgroundThread
import com.cyberkey.commons.interfaces.RefreshRecyclerViewListener
import com.cyberkey.videoplayer.R
import com.cyberkey.videoplayer.adapters.ManageHiddenFoldersAdapter
import com.cyberkey.videoplayer.databinding.ActivityManageFoldersBinding
import com.cyberkey.videoplayer.extensions.addNoMedia
import com.cyberkey.videoplayer.extensions.config
import com.cyberkey.videoplayer.extensions.getNoMediaFolders

class HiddenFoldersActivity : SimpleActivity(), RefreshRecyclerViewListener {

    private val binding by viewBinding(ActivityManageFoldersBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        isMaterialActivity = true
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        updateFolders()
        setupOptionsMenu()
        binding.manageFoldersToolbar.title = getString(R.string.hidden_folders)

        updateMaterialActivityViews(binding.manageFoldersCoordinator, binding.manageFoldersList, useTransparentNavigation = true, useTopSearchMenu = false)
        setupMaterialScrollListener(binding.manageFoldersList, binding.manageFoldersToolbar)
    }

    override fun onResume() {
        super.onResume()
        setupToolbar(binding.manageFoldersToolbar, NavigationIcon.Arrow)
    }

    private fun updateFolders() {
        getNoMediaFolders {
            runOnUiThread {
                binding.manageFoldersPlaceholder.apply {
                    text = getString(R.string.hidden_folders_placeholder)
                    beVisibleIf(it.isEmpty())
                    setTextColor(getProperTextColor())
                }

                val adapter = ManageHiddenFoldersAdapter(this, it, this, binding.manageFoldersList) {}
                binding.manageFoldersList.adapter = adapter
            }
        }
    }

    private fun setupOptionsMenu() {
        binding.manageFoldersToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add_folder -> addFolder()
                else -> return@setOnMenuItemClickListener false
            }
            return@setOnMenuItemClickListener true
        }
    }

    override fun refreshItems() {
        updateFolders()
    }

    private fun addFolder() {
        FilePickerDialog(this, config.lastFilepickerPath, false, config.shouldShowHidden, false, true) {
            config.lastFilepickerPath = it
            ensureBackgroundThread {
                addNoMedia(it) {
                    updateFolders()
                }
            }
        }
    }
}
