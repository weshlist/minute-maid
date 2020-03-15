package io.weshlist.minutemaid.model.mongo

import io.weshlist.minutemaid.model.Music
import io.weshlist.minutemaid.utils.ChannelID
import io.weshlist.minutemaid.utils.MusicID
import io.weshlist.minutemaid.utils.UserID
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable
import java.sql.Timestamp

interface Table

@Document(collection = "channel")
data class ChannelTable(
	@Id
	var channelId: ChannelID,
	var channelName: String,
	var channelCreator: UserID,
	var currentMusicId: MusicID?,
	var playlist: List<Music>,
	var userlist: List<UserID>,
	var streamingUri: String = "wesh://streaming-uri/gg",
	var ts0: String,
	var ts1: String,
	var ts2: String,
	var timestamp: String
) : Serializable, Table

@Document(collection = "channel")
data class ChannelUserlistTable(
	@Id
	var channelId: ChannelID,
	var userlist: List<UserID>
) : Serializable, Table

@Document(collection = "music")
data class MusicTable(
	@Id
	var musicId: MusicID,
	var musicName: String,
	var artist: String,
	var length: Int
) : Serializable, Table

@Document(collection = "user")
data class UserTable(
	@Id
	var userId: UserID,
	var userName: String,
	var profileImageUri: String,
	var playlist: List<MusicID>
) : Serializable, Table

@Document(collection = "music")
data class UserPlaylistTable(
	@Id
	var userId: UserID,
	var playlist: List<MusicID>
) : Serializable, Table

@Document(collection = "channel")
data class M3u8Table(
	@Id
	var channelId: ChannelID,
	var ts0: String,
	var ts1: String,
	var ts2: String,
	var timestamp: String
) : Serializable, Table