package io.weshlist.minutemaid.model

import io.weshlist.minutemaid.model.mongo.*
import io.weshlist.minutemaid.utils.ChannelID
import io.weshlist.minutemaid.utils.MusicID
import io.weshlist.minutemaid.utils.Timestamp
import io.weshlist.minutemaid.utils.UserID

interface ConvertTable<T : Table, M> {
	fun fromTableRow(table: T): M
	fun fromTableRows(tables: List<T>): List<M>
}

/**
 * Channel
 */
data class Channel(
	val channelId: ChannelID,
	val channelName: String,
	val channelCreator: UserID,
	var playlist: List<MusicID> = emptyList(),
	var userlist: List<UserID> = emptyList(),
	var currentMusic: MusicID? = null,
	var currentMusicStartTime: Timestamp? = null
) {
	companion object : ConvertTable<ChannelTable, Channel> {
		override fun fromTableRow(table: ChannelTable): Channel {
			// TOOD: currentMusic & playlist are needed to be update
			return Channel(
				channelId = table.channelId,
				channelName = table.channelName,
				channelCreator = table.channelCreator,
				playlist = table.playlist,
				userlist = table.userlist,
				currentMusic = table.currentMusicId,
				currentMusicStartTime = table.currentMusicStartTime
			)
		}

		override fun fromTableRows(tables: List<ChannelTable>): List<Channel> {
			return tables.map(::fromTableRow)
		}
	}
}

/**
 * Current Music from Channel
 */
data class ChannelMusicStatus(
	var playlist: List<MusicID> = emptyList(),
	var currentMusic: MusicID? = null,
	var currentMusicStartTime: Timestamp? = null
) {
	companion object : ConvertTable<ChannelMusicStatusTable, ChannelMusicStatus> {
		override fun fromTableRow(table: ChannelMusicStatusTable): ChannelMusicStatus {
			// TOOD: currentMusic & playlist are needed to be update
			return ChannelMusicStatus(
				playlist = table.playlist,
				currentMusic = table.currentMusicId,
				currentMusicStartTime = table.currentMusicStartTime
			)
		}

		override fun fromTableRows(tables: List<ChannelMusicStatusTable>): List<ChannelMusicStatus> {
			return tables.map(::fromTableRow)
		}
	}
}

/**
 * Music
 */
data class Music(
	val musicId: String,
	val musicMeta: MusicMeta
) {
	companion object: ConvertTable<MusicTable, Music> {
		override fun fromTableRow(table: MusicTable): Music {
			return Music(
				musicId = table.musicId,
				musicMeta = MusicMeta(
					title = table.musicName,
					artist = table.artist,
					length = table.length
				)
			)
		}

		override fun fromTableRows(tables: List<MusicTable>): List<Music> {
			return tables.map(::fromTableRow)
		}
	}
}

data class MusicMeta(
	val title: String,
	val artist: String,
	val length: Int // ms
)

/**
 * User
 */
data class User(
	val userId: UserID,
	val userName: String,
	val profileImageUri: String = "wesh://profile_image_uri",
	var playlist: List<MusicID> = emptyList()
) {
	companion object : ConvertTable<UserTable, User> {
		override fun fromTableRow(table: UserTable): User {
			return User(
				userId = table.userId,
				userName = table.userName,
				profileImageUri = table.profileImageUri,
				playlist = table.playlist
			)
		}

		override fun fromTableRows(tables: List<UserTable>): List<User> {
			return tables.map(::fromTableRow)
		}
	}
}