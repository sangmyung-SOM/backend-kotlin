package com.smu.som.game

import com.smu.som.game.dto.GameMessage
import com.smu.som.gameroom.GameRoomSetting
import com.smu.som.gameroom.controller.GameRoomController
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class GameRoomTests {

	@Autowired
	lateinit var gameRoomController : GameRoomController
	@Autowired
	lateinit var gameMessageController : GameMessageController

	@Test
	public fun 게임만들기1000_테스트(){
		for(i in 0 until 1000){
			val response: GameRoomSetting = gameRoomController.createRoom("가나", "COUPLE", "OFF")
			println("[${i}] response.roomId = ${response.roomId}")
		}

		println("게임방 1000개 생성후 조회")
		val gameRoomList = gameRoomController.room()

		var count = 0
		gameRoomList.forEach{
			println("[${count}] it.roomId = ${it.roomId}")
			count++

			val request = GameMessage.GetGameInfo(
				type = GameStateType.WAIT,
				roomId = it.roomId,
				sender = "가나",
				playerId = "1P"
			)
			gameMessageController.gameWait(request)
		}
	}
}
