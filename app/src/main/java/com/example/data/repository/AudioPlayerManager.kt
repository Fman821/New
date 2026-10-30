package com.example.data.repository

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Playback state for streamed Quran and Azkar recitations.
 */
sealed class AudioPlaybackState {
    object Idle : AudioPlaybackState()
    data class Buffering(val audioUrl: String) : AudioPlaybackState()
    data class Playing(
        val audioUrl: String,
        val currentPositionMs: Int = 0,
        val durationMs: Int = 0
    ) : AudioPlaybackState()
    data class Paused(
        val audioUrl: String,
        val currentPositionMs: Int = 0,
        val durationMs: Int = 0
    ) : AudioPlaybackState()
    data class Error(val message: String) : AudioPlaybackState()
}

/**
 * Lifecycle-safe wrapper around Android MediaPlayer.
 *
 * All player callbacks verify that they still belong to the active player,
 * preventing an old stream from changing the state of a newer stream.
 */
class AudioPlayerManager(@Suppress("unused") context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var currentUrl: String? = null

    private val _playbackState = MutableStateFlow<AudioPlaybackState>(AudioPlaybackState.Idle)
    val playbackState: StateFlow<AudioPlaybackState> = _playbackState.asStateFlow()

    fun play(url: String) {
        val safeUrl = url.trim()
        if (safeUrl.isEmpty()) {
            _playbackState.value = AudioPlaybackState.Error("የድምፅ አድራሻ አልተገኘም")
            return
        }

        val activePlayer = mediaPlayer
        if (currentUrl == safeUrl && activePlayer != null) {
            when (_playbackState.value) {
                is AudioPlaybackState.Paused -> {
                    runCatching {
                        activePlayer.start()
                        _playbackState.value = AudioPlaybackState.Playing(
                            safeUrl,
                            activePlayer.currentPosition,
                            activePlayer.duration
                        )
                    }.onFailure { failPlayer(activePlayer, it) }
                    return
                }
                is AudioPlaybackState.Playing -> {
                    runCatching {
                        activePlayer.pause()
                        _playbackState.value = AudioPlaybackState.Paused(
                            safeUrl,
                            activePlayer.currentPosition,
                            activePlayer.duration
                        )
                    }.onFailure { failPlayer(activePlayer, it) }
                    return
                }
                else -> Unit
            }
        }

        releaseCurrentPlayer()
        currentUrl = safeUrl
        _playbackState.value = AudioPlaybackState.Buffering(safeUrl)

        val player = MediaPlayer()
        mediaPlayer = player

        try {
            player.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            player.setDataSource(safeUrl)
            player.setOnPreparedListener { prepared ->
                if (mediaPlayer !== prepared) return@setOnPreparedListener
                runCatching {
                    prepared.start()
                    _playbackState.value = AudioPlaybackState.Playing(
                        safeUrl,
                        prepared.currentPosition,
                        prepared.duration
                    )
                }.onFailure { failPlayer(prepared, it) }
            }
            player.setOnCompletionListener { completed ->
                if (mediaPlayer !== completed) return@setOnCompletionListener
                releaseCurrentPlayer()
                _playbackState.value = AudioPlaybackState.Idle
            }
            player.setOnErrorListener { failed, what, extra ->
                if (mediaPlayer === failed) {
                    failPlayer(failed, IllegalStateException("($what, $extra)"))
                }
                true
            }
            player.prepareAsync()
        } catch (error: Exception) {
            failPlayer(player, error)
        }
    }

    fun pause() {
        val player = mediaPlayer ?: return
        val url = currentUrl ?: return
        runCatching {
            if (player.isPlaying) {
                player.pause()
                _playbackState.value = AudioPlaybackState.Paused(
                    url,
                    player.currentPosition,
                    player.duration
                )
            }
        }.onFailure { failPlayer(player, it) }
    }

    fun stop() {
        releaseCurrentPlayer()
        _playbackState.value = AudioPlaybackState.Idle
    }

    fun release() = stop()

    private fun failPlayer(player: MediaPlayer, error: Throwable) {
        if (mediaPlayer !== player) return
        releaseCurrentPlayer()
        val detail = error.localizedMessage?.takeIf { it.isNotBlank() }
        _playbackState.value = AudioPlaybackState.Error(
            if (detail == null) "ድምፁን ማጫወት አልተቻለም" else "ድምፁን ማጫወት አልተቻለም: $detail"
        )
    }

    private fun releaseCurrentPlayer() {
        val player = mediaPlayer
        mediaPlayer = null
        currentUrl = null
        if (player != null) {
            runCatching { player.setOnPreparedListener(null) }
            runCatching { player.setOnCompletionListener(null) }
            runCatching { player.setOnErrorListener(null) }
            runCatching { player.release() }
        }
    }
}
