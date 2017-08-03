package humazed.github.com.smartbaking.ui.step_details

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import humazed.github.com.smartbaking.R
import humazed.github.com.smartbaking.model.Step
import humazed.github.com.smartbaking.utils.hideSystemUI
import kotlinx.android.synthetic.main.activity_step_detail.*
import kotlinx.android.synthetic.main.step_detail.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn

class StepDetailFragment : Fragment(), AnkoLogger, ExoPlayer.EventListener {
    val TAG: String = StepDetailFragment::class.java.simpleName

    companion object {
        val ARG_STEP = "StepDetailFragment:step"

        fun newInstance(step: Step): StepDetailFragment {
            val fragment = StepDetailFragment()
            val args = Bundle()
            args.putParcelable(ARG_STEP, step)
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var step: Step
    var mExoPlayer: SimpleExoPlayer? = null
    var mMediaSession: MediaSessionCompat? = null
    var mStateBuilder: PlaybackStateCompat.Builder? = null

    lateinit var mExoPlayerView: SimpleExoPlayerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments.containsKey(ARG_STEP)) {
            step = arguments.getParcelable(ARG_STEP)
            activity.toolbar_layout?.title = step.shortDescription
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.step_detail, container, false).apply {
            stepDescriptionTextView.text = step.description
            mExoPlayerView = exoPlayerView

            if (!TextUtils.isEmpty(step.videoURL)) {
                initializeMediaSession()
                initializePlayer(Uri.parse(step.videoURL), exoPlayerView)
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE && !resources.getBoolean(R.bool.isTablet)) {
                    hideSystemUI(activity as AppCompatActivity)
                    stepDescriptionTextView.visibility = View.GONE
                    exoPlayerView.layoutParams.height = MATCH_PARENT
                    exoPlayerView.layoutParams.width = MATCH_PARENT
                } else stepDescriptionTextView.text = step.description
            } else exoPlayerView.visibility = View.GONE
        }
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private fun initializePlayer(mediaUri: Uri, exoPlayer: SimpleExoPlayerView) {
        // Create an instance of the ExoPlayer.
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
        exoPlayer.player = mExoPlayer
        // Prepare the MediaSource.
        val mediaSource = ExtractorMediaSource(mediaUri, DefaultDataSourceFactory(
                context, Util.getUserAgent(context, getString(R.string.app_name))), DefaultExtractorsFactory(), null, null)
        mExoPlayer?.addListener(this)
        mExoPlayer?.prepare(mediaSource)
        mExoPlayer?.playWhenReady = true
    }


    private fun initializeMediaSession() {
        mMediaSession?.apply {
            mMediaSession = MediaSessionCompat(context, TAG)
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

            setMediaButtonReceiver(null)
            mStateBuilder = PlaybackStateCompat.Builder()
                    .setActions(PlaybackStateCompat.ACTION_PLAY or
                            PlaybackStateCompat.ACTION_PAUSE or
                            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                            PlaybackStateCompat.ACTION_PLAY_PAUSE)

            setPlaybackState(mStateBuilder?.build())
            setCallback(MySessionCallback())
            isActive = true
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // ExoPlayer.EventListener
    ///////////////////////////////////////////////////////////////////////////
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        warn { "onPlayerStateChanged() called with: playWhenReady = [$playWhenReady], playbackState = [$playbackState]" }
        mExoPlayer?.currentPosition?.also {
            if (playbackState == ExoPlayer.STATE_READY && playWhenReady) {
                mStateBuilder?.setState(PlaybackStateCompat.STATE_PLAYING, it, 1f)
            } else if (playbackState == ExoPlayer.STATE_READY) {
                mStateBuilder?.setState(PlaybackStateCompat.STATE_PAUSED, it, 1f)
            }
        }
        mMediaSession?.setPlaybackState(mStateBuilder?.build())
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        warn { "onPlayerError() called with: error = [$error]" }
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
        warn { "onPlaybackParametersChanged() called with: playbackParameters = [$playbackParameters]" }
    }

    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
        warn { "onTracksChanged() called with: trackGroups = [$trackGroups], trackSelections = [$trackSelections]" }
    }

    override fun onLoadingChanged(isLoading: Boolean) {
        warn { "onLoadingChanged() called with: isLoading = [$isLoading]" }
    }

    override fun onPositionDiscontinuity() {
        warn { "onPositionDiscontinuity() called with: " }
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {
        warn { "onTimelineChanged() called with: timeline = [$timeline], manifest = [$manifest]" }
    }

    private inner class MySessionCallback : MediaSessionCompat.Callback() {
        override fun onPlay() {
            warn { "onPlay() called with: " }
            mExoPlayer?.playWhenReady = true
        }

        override fun onPause() {
            warn { "onPause() called with: " }
            mExoPlayer?.playWhenReady = false
        }

        override fun onSkipToPrevious() {
            warn { "onSkipToPrevious() called with: " }
            mExoPlayer?.seekTo(0)
        }
    }

    override fun onDestroy() {
        warn { "onDestroy() called with: " }
        super.onDestroy()
        mExoPlayer?.apply {
            stop()
            release()
            mExoPlayer = null
        }
    }

}