package io.weshlist.minutemaid.model.mongo

import io.weshlist.minutemaid.repository.Music
import io.weshlist.minutemaid.utils.ChannelID
import io.weshlist.minutemaid.utils.MusicID
import io.weshlist.minutemaid.utils.UserID
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document(collection = "channel")
data class ChannelTable(
	@Id
	var channelId: ChannelID,
	var channelName: String,
	var channelCreator: UserID,
	var currentMusicId: MusicID?,
	var playlist: List<Music>,
	var userlist: List<UserID>,
	var streamingUri: String = "wesh://streaming-uri/gg"
) : Serializable

@Document(collection = "music")
data class MusicTable(
	@Id
	var musicId: MusicID,
	var musicName: String,
	var artist: String,
	var length: Int
) : Serializable

@Document(collection = "music")
data class UserTable(
	@Id
	var userId: UserID,
	var userName: String,
	var profileImageUri: String,
	var playlist: List<MusicID>
) : Serializable

