package humazed.github.com.smartbaking.ui.step_details

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
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
import humazed.github.com.smartbaking.utils.gone
import humazed.github.com.smartbaking.utils.hideSystemUI
import kotlinx.android.synthetic.main.fragment_step_detail.view.*
import org.jetbrains.anko.AnkoLogger

class StepDetailFragment : Fragment(), AnkoLogger, ExoPlayer.EventListener {
    val TAG: String = StepDetailFragment::class.java.simpleName

    companion object {
        val TAG = "StepDetailFragment:tag"
        val ARG_STEPS = "StepDetailFragment:mSteps"
        val ARG_POSITION = "StepDetailFragment:mPosition"

        fun newInstance(steps: ArrayList<Step>, position: Int): StepDetailFragment {
            val fragment = StepDetailFragment()
            val args = Bundle()
            args.putParcelableArrayList(ARG_STEPS, steps)
            args.putInt(ARG_POSITION, position)
            fragment.arguments = args
            return fragment
        }

        fun start(activity: FragmentActivity, it: ArrayList<Step>, position: Int) {
            activity.supportFragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, StepDetailFragment.newInstance(it, position), TAG)
                    .commit()
        }

        fun switchTo(activity: FragmentActivity, it: ArrayList<Step>, position: Int) {
            activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.recipe_detail_container, StepDetailFragment.newInstance(it, position), TAG)
                    .commit()
        }
    }

    var mPosition: Int = 0
    lateinit var mSteps: ArrayList<Step>
    val mStep: Step by lazy { mSteps[mPosition] }

    var mExoPlayer: SimpleExoPlayer? = null
    var mMediaSession: MediaSessionCompat? = null
    var mStateBuilder: PlaybackStateCompat.Builder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments.containsKey(ARG_STEPS)) {
            mPosition = arguments.getInt(ARG_POSITION)
            mSteps = arguments.getParcelableArrayList<Step>(ARG_STEPS)
            (activity as AppCompatActivity).supportActionBar?.title = mStep.shortDescription
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_step_detail, container, false).apply {
            stepDescriptionTextView.text = mStep.description

            when (mPosition) {
                0 -> previousFab.gone()
                mSteps.size - 1 -> nextFab.gone()
            }

            nextFab.setOnClickListener {
                switchTo(activity, mSteps, mStep.id + 1)
            }

            previousFab.setOnClickListener {
                switchTo(activity, mSteps, mStep.id - 1)
            }

            if (!TextUtils.isEmpty(mStep.videoURL)) {
                initializeMediaSession()
                initializePlayer(Uri.parse(mStep.videoURL), exoPlayerView)
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE && !resources.getBoolean(R.bool.isTablet)) {
                    hideSystemUI(activity as AppCompatActivity)
                    stepDescriptionTextView.gone()
                    exoPlayerView.layoutParams.height = MATCH_PARENT
                    exoPlayerView.layoutParams.width = MATCH_PARENT
                } else stepDescriptionTextView.text = mStep.description
            } else exoPlayerView.gone()
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
        mExoPlayer?.currentPosition?.also {
            if (playbackState == ExoPlayer.STATE_READY && playWhenReady) {
                mStateBuilder?.setState(PlaybackStateCompat.STATE_PLAYING, it, 1f)
            } else if (playbackState == ExoPlayer.STATE_READY) {
                mStateBuilder?.setState(PlaybackStateCompat.STATE_PAUSED, it, 1f)
            }
        }
        mMediaSession?.setPlaybackState(mStateBuilder?.build())
    }

    override fun onPlayerError(error: ExoPlaybackException?) {}
    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {}
    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {}
    override fun onLoadingChanged(isLoading: Boolean) {}
    override fun onPositionDiscontinuity() {}
    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {}

    private inner class MySessionCallback : MediaSessionCompat.Callback() {
        override fun onPlay() {
            mExoPlayer?.playWhenReady = true
        }

        override fun onPause() {
            mExoPlayer?.playWhenReady = false
        }

        override fun onSkipToPrevious() {
            mExoPlayer?.seekTo(0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mExoPlayer?.apply {
            stop()
            release()
            mExoPlayer = null
        }
    }
}