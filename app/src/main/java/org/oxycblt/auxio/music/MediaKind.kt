/*
 * Copyright (c) 2026 Auxio Project
 * MediaKind.kt is part of Auxio.
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

package org.oxycblt.auxio.music

import org.oxycblt.musikr.Song

/**
 * What kind of audio a file is, decided only by the folder it sits in. No tagging needed: drop a
 * file anywhere under a folder named "Shows" or "Movies" and it is treated as such.
 */
enum class MediaKind {
    MUSIC,
    SHOW,
    MOVIE;

    /** Whether playback should remember where you left off in each file. */
    val resumable: Boolean
        get() = this != MUSIC

    companion object {
        /**
         * Pick a [MediaKind] from a file's folders, outermost first. The first folder named
         * "Shows" or "Movies" (any case) wins. Anything else is [MUSIC].
         */
        fun fromFolders(folders: List<String>): MediaKind {
            for (folder in folders) {
                if (folder.equals("Shows", ignoreCase = true)) return SHOW
                if (folder.equals("Movies", ignoreCase = true)) return MOVIE
            }
            return MUSIC
        }
    }
}

/** The [MediaKind] of this song, based on the folders it lives in. */
val Song.kind: MediaKind
    get() = MediaKind.fromFolders(path.directory.components.components)
