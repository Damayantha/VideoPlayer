/*
package com.cyberkey.videoplayer.fragments

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.cyberkey.commons.extensions.*
import com.cyberkey.commons.helpers.ensureBackgroundThread
import com.cyberkey.videoplayer.R
import com.cyberkey.videoplayer.activities.VideoActivity
import com.cyberkey.videoplayer.databinding.PagerVideoItemBinding
import com.cyberkey.videoplayer.extensions.config
import com.cyberkey.videoplayer.helpers.Config
import com.cyberkey.videoplayer.helpers.MEDIUM
import com.cyberkey.videoplayer.helpers.SHOULD_INIT_FRAGMENT
import com.cyberkey.videoplayer.models.Medium


class VideoFragment : ViewPagerFragment() {
    private val PROGRESS = "progress"

    private var mIsFullscreen = false
    private var mWasFragmentInit = false
    private var mIsPanorama = false
    private var mIsFragmentVisible = false
    private var mIsDragged = false
    private var mWasVideoStarted = false
    private var mWasPlayerInited = false
    private var mWasLastPositionRestored = false
    private var mPlayOnPrepared = false
    private var mIsPlayerPrepared = false
    private var mCurrTime = 0
    private var mDuration = 0
    private var mPositionWhenInit = 0
    private var mPositionAtPause = 0L
    var mIsPlaying = false

    private var mVideoSize = Point(1, 1)
    private var mTimerHandler = Handler()

    private var mStoredShowExtendedDetails = false
    private var mStoredHideExtendedDetails = false
    private var mStoredBottomActions = true
    private var mStoredExtendedDetails = 0
    private var mStoredRememberLastVideoPosition = false

    private lateinit var mTimeHolder: View
    private lateinit var binding: PagerVideoItemBinding
    private lateinit var mView: View
    private lateinit var mMedium: Medium
    private lateinit var mConfig: Config

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val context = requireContext()
        val activity = requireActivity()
        val arguments = requireArguments()

        mMedium = arguments.getSerializable(MEDIUM) as Medium
        mConfig = context.config
        binding = PagerVideoItemBinding.inflate(inflater, container, false).apply {



            videoPlayOutline.setOnClickListener {

            }





            videoPreview.setOnTouchListener { view, event ->
                handleEvent(event)
                false
            }

        }
        mView = binding.root

        if (!arguments.getBoolean(SHOULD_INIT_FRAGMENT, true)) {
            return mView
        }

        storeStateVariables()
        Glide.with(context).load(mMedium.path).into(binding.videoPreview)

        // setMenuVisibility is not called at VideoActivity (third party intent)
        if (!mIsFragmentVisible && activity is VideoActivity) {
            mIsFragmentVisible = true
        }

        mIsFullscreen = activity.window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_FULLSCREEN == View.SYSTEM_UI_FLAG_FULLSCREEN


        ensureBackgroundThread {
            activity.getVideoResolution(mMedium.path)?.apply {
                mVideoSize.x = x
                mVideoSize.y = y
            }
        }

        if (mIsPanorama) {
            binding.apply {
                panoramaOutline.beVisible()
                videoPlayOutline.beGone()

                Glide.with(context).load(mMedium.path).into(videoPreview)
            }
        }

        if (!mIsPanorama) {
            if (savedInstanceState != null) {
                mCurrTime = savedInstanceState.getInt(PROGRESS)
            }

            mWasFragmentInit = true

        }


        return mView
    }

    override fun onResume() {
        super.onResume()
        mConfig = requireContext().config      // make sure we get a new config, in case the user changed something in the app settings
        requireActivity().updateTextColors(binding.videoHolder)

        checkExtendedDetails()
        storeStateVariables()
    }

    override fun onPause() {
        super.onPause()
        storeStateVariables()

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)

        mIsFragmentVisible = menuVisible

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        checkExtendedDetails()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(PROGRESS, mCurrTime)
    }

    private fun storeStateVariables() {
        mConfig.apply {
            mStoredShowExtendedDetails = showExtendedDetails
            mStoredHideExtendedDetails = hideExtendedDetails
            mStoredExtendedDetails = extendedDetails
            mStoredBottomActions = bottomActions
            mStoredRememberLastVideoPosition = rememberLastVideoPosition
        }
    }








//    private fun launchVideoPlayer() {
//        listener?.launchViewVideoIntent(mMedium.path)
//    }





    private fun checkExtendedDetails() {
        if (mConfig.showExtendedDetails) {
            binding.videoDetails.apply {
                beInvisible()   // make it invisible so we can measure it, but not show yet
                text = getMediumExtendedDetails(mMedium)
                onGlobalLayout {
                    if (isAdded) {
                        val realY = getExtendedDetailsY(height)
                        if (realY > 0) {
                            y = realY
                            beVisibleIf(text.isNotEmpty())
                            alpha = if (!mConfig.hideExtendedDetails || !mIsFullscreen) 1f else 0f
                        }
                    }
                }
            }
        } else {
            binding.videoDetails.beGone()
        }
    }






    private fun getExtendedDetailsY(height: Int): Float {
        val smallMargin = context?.resources?.getDimension(com.cyberkey.commons.R.dimen.small_margin) ?: return 0f
        val fullscreenOffset = smallMargin + if (mIsFullscreen) 0 else requireContext().navigationBarHeight
        var actionsHeight = 0f
        if (!mIsFullscreen) {
            actionsHeight += resources.getDimension(R.dimen.video_player_play_pause_size)
            if (mConfig.bottomActions) {
                actionsHeight += resources.getDimension(R.dimen.bottom_actions_height)
            }
        }
        return requireContext().realScreenSize.y - height - actionsHeight - fullscreenOffset
    }



}
*/
package com.cyberkey.videoplayer.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.bumptech.glide.Glide
import com.cyberkey.commons.extensions.*
import com.cyberkey.commons.helpers.ensureBackgroundThread
import com.cyberkey.videoplayer.BuildConfig
import com.cyberkey.videoplayer.PlayerActivity
import com.cyberkey.videoplayer.R
import com.cyberkey.videoplayer.activities.VideoActivity
import com.cyberkey.videoplayer.databinding.PagerVideoItemBinding
import com.cyberkey.videoplayer.extensions.config
import com.cyberkey.videoplayer.helpers.Config
import com.cyberkey.videoplayer.helpers.MEDIUM
import com.cyberkey.videoplayer.helpers.SHOULD_INIT_FRAGMENT
import com.cyberkey.videoplayer.models.Medium


class VideoFragment : ViewPagerFragment() {
    private val PROGRESS = "progress"

    private var mIsFullscreen = false
    private var mWasFragmentInit = false
    private var mIsPanorama = false
    private var mIsFragmentVisible = false
    private var mIsDragged = false
    private var mWasVideoStarted = false
    private var mWasPlayerInited = false
    private var mWasLastPositionRestored = false
    private var mPlayOnPrepared = false
    private var mIsPlayerPrepared = false
    private var mCurrTime = 0
    private var mDuration = 0
    private var mPositionWhenInit = 0
    private var mPositionAtPause = 0L
    var mIsPlaying = false

    private var mVideoSize = Point(1, 1)
    private var mTimerHandler = Handler()

    private var mStoredShowExtendedDetails = false
    private var mStoredHideExtendedDetails = false
    private var mStoredBottomActions = true
    private var mStoredExtendedDetails = 0
    private var mStoredRememberLastVideoPosition = false

    private lateinit var mTimeHolder: View
    private lateinit var binding: PagerVideoItemBinding
    private lateinit var mView: View
    private lateinit var mMedium: Medium
    private lateinit var mConfig: Config

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val context = requireContext()
        val activity = requireActivity()
        val arguments = requireArguments()

        mMedium = arguments.getSerializable(MEDIUM) as Medium
        mConfig = context.config
        binding = PagerVideoItemBinding.inflate(inflater, container, false).apply {


            videoPlayOutline.setOnClickListener {

                launchVideoPlayer()
            }





            videoPreview.setOnTouchListener { view, event ->
                handleEvent(event)
                false
            }

        }
        mView = binding.root

        if (!arguments.getBoolean(SHOULD_INIT_FRAGMENT, true)) {
            return mView
        }

        storeStateVariables()
        Glide.with(context).load(mMedium.path).into(binding.videoPreview)

        // setMenuVisibility is not called at VideoActivity (third party intent)
        if (!mIsFragmentVisible && activity is VideoActivity) {
            mIsFragmentVisible = true
        }

        mIsFullscreen =
            activity.window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_FULLSCREEN == View.SYSTEM_UI_FLAG_FULLSCREEN


        ensureBackgroundThread {
            activity.getVideoResolution(mMedium.path)?.apply {
                mVideoSize.x = x
                mVideoSize.y = y
            }
        }

        if (mIsPanorama) {
            binding.apply {
                panoramaOutline.beVisible()
                videoPlayOutline.beGone()

                Glide.with(context).load(mMedium.path).into(videoPreview)
            }
        }

        if (!mIsPanorama) {
            if (savedInstanceState != null) {
                mCurrTime = savedInstanceState.getInt(PROGRESS)
            }

            mWasFragmentInit = true

        }


        return mView
    }

    override fun onResume() {
        super.onResume()
        mConfig =
            requireContext().config      // make sure we get a new config, in case the user changed something in the app settings
        requireActivity().updateTextColors(binding.videoHolder)

        checkExtendedDetails()
        storeStateVariables()
    }

    override fun onPause() {
        super.onPause()
        storeStateVariables()

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)

        mIsFragmentVisible = menuVisible

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        checkExtendedDetails()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(PROGRESS, mCurrTime)
    }

    private fun storeStateVariables() {
        mConfig.apply {
            mStoredShowExtendedDetails = showExtendedDetails
            mStoredHideExtendedDetails = hideExtendedDetails
            mStoredExtendedDetails = extendedDetails
            mStoredBottomActions = bottomActions
            mStoredRememberLastVideoPosition = rememberLastVideoPosition
        }
    }


//    private fun launchVideoPlayer() {
//        listener?.launchViewVideoIntent(mMedium.path)
//    }


    private fun checkExtendedDetails() {
        if (mConfig.showExtendedDetails) {
            binding.videoDetails.apply {
                beInvisible()   // make it invisible so we can measure it, but not show yet
                text = getMediumExtendedDetails(mMedium)
                onGlobalLayout {
                    if (isAdded) {
                        val realY = getExtendedDetailsY(height)
                        if (realY > 0) {
                            y = realY
                            beVisibleIf(text.isNotEmpty())
                            alpha = if (!mConfig.hideExtendedDetails || !mIsFullscreen) 1f else 0f
                        }
                    }
                }
            }
        } else {
            binding.videoDetails.beGone()
        }
    }


    private fun getExtendedDetailsY(height: Int): Float {
        val smallMargin =
            context?.resources?.getDimension(com.cyberkey.commons.R.dimen.small_margin) ?: return 0f
        val fullscreenOffset =
            smallMargin + if (mIsFullscreen) 0 else requireContext().navigationBarHeight
        var actionsHeight = 0f
        if (!mIsFullscreen) {
            actionsHeight += resources.getDimension(R.dimen.video_player_play_pause_size)
            if (mConfig.bottomActions) {
                actionsHeight += resources.getDimension(R.dimen.bottom_actions_height)
            }
        }
        return requireContext().realScreenSize.y - height - actionsHeight - fullscreenOffset
    }

    @OptIn(UnstableApi::class)
    private fun launchVideoPlayer() {
        val newUri = requireActivity().getFinalUriFromPath(mMedium.path, BuildConfig.APPLICATION_ID)
        if (newUri == null) {
            requireActivity().toast(com.cyberkey.commons.R.string.unknown_error_occurred)
            return
        }

        requireActivity().hideKeyboard()
        val mimeType = requireActivity().getUriMimeType(mMedium.path, newUri)
        Intent(requireContext(), PlayerActivity::class.java).apply {
            setDataAndType(newUri, mimeType)
            addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
            if (requireActivity().intent.extras != null) {
                putExtras(requireActivity().intent.extras!!)
            }

            startActivity(this)
        }

        requireActivity().finish()
    }


}
