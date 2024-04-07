package com.example.musicplayer.activity.music.presentation

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.R
import com.example.musicplayer.activity.music.adapter.SongAdapter
import com.example.musicplayer.activity.music.model.MusicReqModel
import com.example.musicplayer.activity.music.model.MusicResultModel
import com.example.musicplayer.activity.music.vm.MusicViewModel
import com.example.musicplayer.databinding.ActivitySearchBinding
import com.example.musicplayer.helper.InterfaceDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SearchActivity : AppCompatActivity(), SearchOnClickListener<MusicResultModel> {

    private val TAG = this::class.java.simpleName

    val viewModel by viewModel<MusicViewModel>()

    private lateinit var mediaPlayer: MediaPlayer

    private val thisContext = this@SearchActivity

    private lateinit var songAdapter: SongAdapter
    private lateinit var musicResultModel: MusicResultModel

    private lateinit var binding: ActivitySearchBinding
    private lateinit var interfaceDialog: InterfaceDialog

    private lateinit var listMusic: List<MusicResultModel>

    private var currPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)

        interfaceDialog = InterfaceDialog(thisContext)
        songAdapter = SongAdapter()
        mediaPlayer = MediaPlayer()
        musicResultModel = MusicResultModel()

        setContentView(binding.root)
        supportActionBar!!.hide()

        binding.etSearchName.setText("justin beiber")
        binding.rvSearch.requestFocus()

        setupViewModel(viewModel, binding, songAdapter)
        setRequest(viewModel, binding, songAdapter)

        setOnListener()
    }

    /**
     * set view model
     */
    private fun setupViewModel(
        VM: MusicViewModel,
        binding: ActivitySearchBinding,
        adapter: SongAdapter
    ) {
        with(VM) {
            onSuccess.observe(thisContext) {
                it?.let {
                    if (it.isEmpty()) {
                        binding.tvEmpty.visibility = VISIBLE
                        binding.tvEmptySearch.visibility = VISIBLE
                    } else {
                        binding.tvEmpty.visibility = GONE
                        binding.tvEmptySearch.visibility = GONE
                    }

                    listMusic = it
                    adapter.provided(it, musicResultModel, thisContext)
                }
            }
            onError.observe(thisContext) {
                interfaceDialog.showDialogWarningConfirm("Please try again!", it, "OK!")
            }

            onProgress.observe(thisContext) { isVisible ->
                binding.progressBar.visibility = if (isVisible) VISIBLE else GONE
            }
        }
    }

    private fun setRequest(
        viewModel: MusicViewModel,
        binding: ActivitySearchBinding,
        adapter: SongAdapter
    ) {
        // set loading on ui
        binding.rvSearch.adapter = adapter
        viewModel.doIt(MusicReqModel(term = binding.etSearchName.text.toString()))
    }

    private fun setOnListener() = with(binding) {
        ivMusicPlay.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                musicResultModel.isPlay = false
                setupStartOrStop(false)
            } else {
                mediaPlayer.start()
                musicResultModel.isPlay = true
                setupStartOrStop(true)
                startMusicMeter()
            }
        }

        ivMusicNext.setOnClickListener {
            rvSearch.postDelayed({
                if (rvSearch.findViewHolderForAdapterPosition(0) != null) {
                    rvSearch.findViewHolderForAdapterPosition(currPosition + 1)!!.itemView.performClick()
                }
            }, 100)
        }

        ivMusicPrevious.setOnClickListener {
            rvSearch.postDelayed({
                if (rvSearch.findViewHolderForAdapterPosition(0) != null) {
                    if (currPosition > 0)
                        rvSearch.findViewHolderForAdapterPosition(currPosition - 1)!!.itemView.performClick()
                }
            }, 100)
        }

        ivSearch.setOnClickListener {
            setRequest(viewModel, binding, songAdapter)
        }

        ivCancel.setOnClickListener {
            etSearchName.setText("")
            setRequest(viewModel, binding, songAdapter)
        }

        etSearchName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setRequest(viewModel, binding, songAdapter)
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemClick(
        position: Int,
        model: MusicResultModel
    ) {
        if (currPosition == position) {
            playMusic()
        } else {
            musicResultModel = model
            currPosition = position
            binding.tvPlaySong.text = model.trackName
            stopMusic()
            playMusic()
        }
        songAdapter.provided(listMusic, musicResultModel, thisContext)
        songAdapter.notifyDataSetChanged()
    }

    private fun playMusic() {
        if (musicResultModel.isPlay){
            stopMusic()
        }
        mediaPlayer = MediaPlayer()
        try {
            mediaPlayer.setDataSource(musicResultModel.previewUrl)
            mediaPlayer.prepare()
            mediaPlayer.start()

            binding.sbProgress.max = mediaPlayer.duration
            musicResultModel.isPlay = true

            setupStartOrStop(true)
            startMusicMeter()
        } catch (ex: Exception) {
            Timber.tag("MySongAdapter").e("Error: " + ex.message)
        }
    }

    private fun stopMusic() {
        musicResultModel.isPlay = false
        mediaPlayer.stop()
        setupStartOrStop(false)
    }

    private fun setupStartOrStop(isStart: Boolean) = with(binding) {
        if (isStart) {
            ivMusicPlay.setImageBitmap(null)
            ivMusicPlay.setImageResource(android.R.color.transparent)
            ivMusicPlay.setBackgroundResource(R.drawable.ic_music_pause_2)
        } else {
            ivMusicPlay.setImageBitmap(null)
            ivMusicPlay.setImageResource(android.R.color.transparent)
            ivMusicPlay.setBackgroundResource(R.drawable.ic_music_play)
        }
    }

    private fun startMusicMeter() {
        Thread {
            var currentPosition = mediaPlayer.currentPosition
            val total = mediaPlayer.duration

            while (mediaPlayer.isPlaying && currentPosition < total) {
                currentPosition = try {
                    mediaPlayer.currentPosition
                } catch (e: InterruptedException) {
                    return@Thread
                } catch (e: java.lang.Exception) {
                    return@Thread
                }
                binding.sbProgress.progress = currentPosition

                if (currentPosition == total) {
                    stopMusic()
                }
            }
        }.start()
    }
}