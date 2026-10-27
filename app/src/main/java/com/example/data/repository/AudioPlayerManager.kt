package com.example.data.repository

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * State representing audio recitation playback.
 */
sealed class AudioPlaybackState {
    object Idle : AudioPlaybackState()
    data class Buffering(val audioUrl: String) : AudioPlaybackState()
    data class Playing(val audioUrl: String, val currentPositionMs: Int = 0, val durationMs: Int = 0) : AudioPlaybackState()
    data class Paused(val audioUrl: String, val currentPositionMs: Int = 0, val durationMs: Int = 0) : AudioPlaybackState()
    data class Error(val message: String) : AudioPlaybackState()
}

/**
 * Clean wrapper around Android MediaPlayer to stream Quran & Azkar recitations.
 */
class AudioPlayerManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var currentUrl: String? = null

    private val _playbackState = MutableStateFlow<AudioPlaybackState>(AudioPlaybackState.Idle)
    val playbackState: StateFlow<AudioPlaybackState> = _playbackState.asStateFlow()

    fun play(url: String) {
        if (url.isBlank()) return

        // If resuming paused audio of the same url
        if (currentUrl == url && mediaPlayer != null) {
            val state = _playbackState.value
            if (state is AudioPlaybackState.Paused) {
                mediaPlayer?.start()
                _playbackState.value = AudioPlaybackState.Playing(
                    url,
                    mediaPlayer?.currentPosition ?: 0,
                    mediaPlayer?.duration ?: 0
                )
                return
            } else if (state is AudioPlaybackState.Playing) {
                // Pause it
                mediaPlayer?.pause()
                _playbackState.value = AudioPlaybackState.Paused(
                    url,
                    mediaPlayer?.currentPosition ?: 0,
                    mediaPlayer?.duration ?: 0
                )
                return
            }
        }

        // New audio stream
        stop()
        currentUrl = url
        _playbackState.value = AudioPlaybackState.Buffering(url)

        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                setOnPreparedListener { mp ->
                    mp.start()
                    _playbackState.value = AudioPlaybackState.Playing(
                        url,
                        mp.currentPosition,
                        mp.duration
                    )
                }
                setOnCompletionListener {
                    _playbackState.value = AudioPlaybackState.Idle
                    currentUrl = null
                }
                setOnErrorListener { _, what, extra ->
                    _playbackState.value = AudioPlaybackState.Error("ድምፁን ማጫወት አልተቻለም ($what, $extra)")
                    currentUrl = null
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            _playbackState.value = AudioPlaybackState.Error(e.localizedMessage ?: "ስህተት ተፈጥሯል")
            currentUrl = null
        }
    }

    fun pause() {
        mediaPlayer?.let { mp ->
            if (mp.isPlaying && currentUrl != null) {
                mp.pause()
                _playbackState.value = AudioPlaybackState.Paused(
                    currentUrl!!,
                    mp.currentPosition,
                    mp.duration
                )
            }
        }
    }

    fun stop() {
        try {
            mediaPlayer?.let { mp ->
                if (mp.isPlaying) {
                    mp.stop()
                }
                mp.reset()
                mp.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mediaPlayer = null
        currentUrl = null
        _playbackState.value = AudioPlaybackState.Idle
    }

    fun release() {
        stop()
    }
}
