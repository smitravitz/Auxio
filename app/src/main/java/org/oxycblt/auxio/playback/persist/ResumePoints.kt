/*
 * Copyright (c) 2026 Auxio Project
 * ResumePoints.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.oxycblt.auxio.playback.persist

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import org.oxycblt.musikr.Song

/**
 * Remembers where you left off in each show or movie: one saved position per file, keyed by the
 * file's UID. Positions near the start or end are dropped, so finished files start from the top.
 */
@Singleton
class ResumePoints @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("resume_points", Context.MODE_PRIVATE)

    /** The saved position for [song], or 0 if there is none. */
    fun get(song: Song): Long = prefs.getLong(song.uid.toString(), 0L)

    /** Save [positionMs] for [song], or forget it if it's too close to the start or end. */
    fun set(song: Song, positionMs: Long) {
        val nearStart = positionMs < MIN_POSITION_MS
        val nearEnd = song.durationMs > 0 && positionMs > song.durationMs - END_MARGIN_MS
        if (nearStart || nearEnd) {
            clear(song)
        } else {
            prefs.edit { putLong(song.uid.toString(), positionMs) }
        }
    }

    /** Forget the saved position for [song]. */
    fun clear(song: Song) {
        prefs.edit { remove(song.uid.toString()) }
    }

    private companion object {
        const val MIN_POSITION_MS = 5_000L
        const val END_MARGIN_MS = 30_000L
    }
}
