package io.weshlist.minutemaid

import io.weshlist.minutemaid.model.MusicMeta
import io.weshlist.minutemaid.result.get
import io.weshlist.minutemaid.service.ChannelService
import io.weshlist.minutemaid.service.MusicService
import io.weshlist.minutemaid.service.UserService
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component


@Component
class DatabaseInitialize(
	private val userService: UserService,
	private val channelService: ChannelService,
	private val musicService: MusicService
)  {

	@EventListener(ApplicationReadyEvent::class)
	fun doInitializeAfterStartup() {

		/**
		 * ADD Users
		 */
		userService.createUser("영호")    // user0
		userService.createUser("효찬")    // user1
		userService.createUser("수종")    // user2
		userService.createUser("해빈")    // user3
		userService.createUser("주환")    // user4

		println(userService.mgetUser(listOf("user0", "user1", "user2", "user3", "user4")).get())

		/**
		 * Add Musics
		 */
		musicService.add(MusicMeta("합정역 5번 출구", "유산슬", 150))    // music0
		musicService.add(MusicMeta("사랑의 재개발", "유산슬", 150))      // music1
		musicService.add(MusicMeta("사랑니", "아이유", 150))          // music2
		musicService.add(MusicMeta("분홍신", "아이유", 150))          // music3
		musicService.add(MusicMeta("기차를 타고", "아이유", 150))      // music4
		musicService.add(MusicMeta("금요일에 만나요", "아이유", 150))      // music5
		musicService.add(MusicMeta("이태원 프리덤", "UV", 150))      // music6
		musicService.add(MusicMeta("하루끝", "아이유", 150))      // music7

		println(musicService.mget(listOf("music0", "music1")))

		/**
		 * Add Channels
		 */
		println(channelService.join("user0", "사랑의 채널").get())   // channel0
		println(channelService.join("user1", "사랑의 채널").get())


		/**
		 * Add playlist by each user
		 */
		userService.requestMusic("user0", "music0")
		userService.requestMusic("user1", "music1")
		userService.requestMusic("user1", "music2")
		userService.requestMusic("user1", "music3")
		userService.requestMusic("user2", "music4")
		userService.requestMusic("user3", "music5")
		userService.requestMusic("user4", "music6")
		userService.requestMusic("user4", "music7")

		println(channelService.getPlaylist("channel0"))


	}
}