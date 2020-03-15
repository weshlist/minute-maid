package io.weshlist.minutemaid.model

import io.weshlist.minutemaid.model.mongo.*
import io.weshlist.minutemaid.utils.ChannelID
import io.weshlist.minutemaid.utils.MusicID
import io.weshlist.minutemaid.utils.UserID
import org.springframework.data.annotation.Id
import java.sql.Timestamp

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
	var currentMusic: Music? = null,
	var playlist: List<Music> = emptyList(),
	var userlist: List<UserID> = emptyList(),
	val streamingUri: String = "wesh://streaming_uri/123123"
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
				streamingUri = table.streamingUri
			)
		}

		override fun fromTableRows(tables: List<ChannelTable>): List<Channel> {
			return tables.map(::fromTableRow)
		}
	}
}

/**
 * M3U8
 */
data class M3u8(
	@Id
	var channelId: ChannelID,
	var ts0: String,
	var ts1: String,
	var ts2: String,
	var timestamp: String
) {
	companion object : ConvertTable<M3u8Table, M3u8> {
		override fun fromTableRow(table: M3u8Table): M3u8 {
			// TOOD: currentMusic & playlist are needed to be update
			return M3u8(
					channelId = table.channelId,
					ts0 = table.ts0,
					ts1 = table.ts1,
					ts2 = table.ts2,
					timestamp = table.timestamp
			)
		}

		override fun fromTableRows(tables: List<M3u8Table>): List<M3u8> {
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