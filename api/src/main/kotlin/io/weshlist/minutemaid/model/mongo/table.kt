package io.weshlist.minutemaid.model.mongo

import io.weshlist.minutemaid.model.Music
import io.weshlist.minutemaid.utils.ChannelID
import io.weshlist.minutemaid.utils.MusicID
import io.weshlist.minutemaid.utils.Timestamp
import io.weshlist.minutemaid.utils.UserID
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

interface Table

/**
 * ChannelTable
 */
@Document(collection = "channel")
data class ChannelTable(
	@Id
	var channelId: ChannelID,
	var channelName: String,
	var channelCreator: UserID,
	var playlist: List<MusicID>,
	var userlist: List<UserID>,
	var currentMusicId: MusicID?,
	var currentMusicStartTime: Timestamp?
) : Serializable, Table

@Document(collection = "channel")
data class ChannelUserlistTable(
	@Id
	var channelId: ChannelID,
	var userlist: List<UserID>
) : Serializable, Table

@Document(collection = "channel")
data class ChannelPlaylistTable(
	@Id
	var channelId: ChannelID,
	var playlist: List<MusicID>
) : Serializable, Table

@Document(collection = "channel")
data class ChannelMusicStatusTable(
	@Id
	var channelId: ChannelID,
	var playlist: List<MusicID>,
	var currentMusicId: MusicID?,
	var currentMusicStartTime: Timestamp?
) : Serializable, Table

/**
 * Music Table
 */
@Document(collection = "music")
data class MusicTable(
	@Id
	var musicId: MusicID,
	var musicName: String,
	var artist: String,
	var length: Int,
	var streamingFileList: List<String> = listOf(),
	var streamingFileChunk: Int = 10

) : Serializable, Table

/**
 * User Table
 */
@Document(collection = "user")
data class UserTable(
	@Id
	var userId: UserID,
	var userName: String,
	var profileImageUri: String,
	var playlist: List<MusicID>
) : Serializable, Table

@Document(collection = "user")
data class UserPlaylistTable(
	@Id
	var userId: UserID,
	var playlist: List<MusicID>
) : Serializable, Table