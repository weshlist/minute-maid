package io.weshlist.minutemaid.repository

import io.weshlist.minutemaid.model.Music
import io.weshlist.minutemaid.model.MusicMeta
import io.weshlist.minutemaid.result.MusicError
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.utils.MusicID

interface MusicRepository {
	fun getMusic(musicId: MusicID): Result<Music, MusicError>
	fun mgetMusic(musicIds: List<MusicID>): Result<List<Music>, MusicError>
	fun getAddableMusicList(channelId: String): Result<List<Music>, MusicError>
	fun addMusic(musicMeta: MusicMeta): Result<Boolean, MusicError>
}