package com.smu.som.game

import com.smu.som.game.dto.GameMalRequest
import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

@RestController
@RequiredArgsConstructor
@NoArgsConstructor
class GameMessageController(val sendingOperations: SimpMessageSendingOperations)
{

	var roomList = ArrayList<GameRoom>()
	var nameList = ArrayList<String>()
	var gameSetting = ArrayList<String>()


	@MessageMapping("/game/message")
	fun gameStart(gameMessage: GameMessage){

		// 게임방에 접속한 클라이언트 수
		var userCount = AtomicInteger(0)

		roomList.add(GameRoom(UUID.fromString(gameMessage.roomId), gameMessage.sender))

		// 게임방에 접속한 클라이언트 수를 증가시키는 함수
		fun addUserCount(roomId: UUID, userName: String){
			println("addUserCount")
			roomList.forEach {
				if(it.roomId == roomId){
					it.userCount++
					userCount.set(it.userCount)
					nameList.add(userName)

					if(gameMessage.turn == "1P") {

						gameMessage.gameCategory?.split(",")?.get(0)?.let { it1 -> gameSetting.add(it1) }
						gameMessage.gameCategory?.split(",")?.get(1)?.let { it1 -> gameSetting.add(it1) }
						gameMessage.gameCategory?.split(",")?.get(2)?.let { it1 -> gameSetting.add(it1) }

					}


					sendingOperations.convertAndSend("/topic/game/room/"+gameMessage.roomId,gameMessage)
				}

			}
			println("addUserCount END")
		}

		if (userCount.get() < 3 && gameMessage.type == GameStateType.WAIT) {
			gameMessage.sender?.let { addUserCount(UUID.fromString(gameMessage.roomId), it) }
		}

		// WAIT 처음 입장
		if (GameStateType.WAIT == gameMessage.type){

			gameMessage.turn = gameMessage.turn
			if (gameMessage.turn == "1P") {
				var category = gameMessage.gameCategory?.split(",")?.get(0)
				var kcategory = gameMessage.gameCategory?.split(",")?.get(1)
				var adult = gameMessage.gameCategory?.split(",")?.get(2)

				if (category != null) {
					gameSetting.add(category)
				}
				if (kcategory != null) {
					gameSetting.add(kcategory)
				}
				if (adult != null) {
					gameSetting.add(adult)
				}
				println("$category $kcategory $adult")
			}

			println("WAIT STATE : " + gameMessage.turn + " " + gameMessage.sender)


			// 2P 들어오면 게임 시작
			// 예외사항 : 2P가 1P 보다 먼저 들어오게 되는 경우
			if(gameMessage.turn == "2P"){
				gameMessage.type = GameStateType.START
				gameMessage.userNameList = nameList[0] + "," + nameList[1]
				gameMessage.gameCategory = gameSetting[0] + "," + gameSetting[1] + "," + gameSetting[2]


				gameMessage.turn = gameMessage.turn
			}

			sendingOperations.convertAndSend("/topic/game/room/"+gameMessage.roomId,gameMessage)

		}

		// START 게임 시작
		if(GameStateType.START == gameMessage.type){
			gameMessage.gameCategory = gameSetting[0] + "," + gameSetting[1] + "," + gameSetting[2]

			println("START : $gameMessage")

			sendingOperations.convertAndSend("/topic/game/room/"+gameMessage.roomId,gameMessage)

		}
	}

	@MessageMapping("/game/throw")
	fun yutThrow(gameMessage : GameMessage){

		// yutInfoMessage
		/*
		 {
			"roomId": "1",
			"sender": "name",
			"yut": "1",
			"turnChange": "TURN_CHANGE",

		 }
		 */

		// 첫번째로 던졌을때 말 추가 버튼 클릭 없이 말 이동
		if(GameStateType.FIRST_THROW == gameMessage.type){
			if(gameMessage.yut == "4" || gameMessage.yut == "5") //윷이나 모
			{
				gameMessage.type = GameStateType.THROW
			}
			else {
				gameMessage.turnChange = GameStateType.TURN_CHANGE.toString()
			}
			println("FIRST_THROW : " + gameMessage.turn + " "
				+ gameMessage.sender + " " + gameMessage.yut + " " + gameMessage.turnChange)

			sendingOperations.convertAndSend("/topic/game/throw/"+gameMessage.roomId,gameMessage)

		}

		if (GameStateType.THROW == gameMessage.type) {
			if(gameMessage.yut == "4" || gameMessage.yut == "5") //윷이나 모
			{
				gameMessage.type = GameStateType.THROW
			}
			else {
				gameMessage.turnChange = GameStateType.TURN_CHANGE.toString()
			}
			gameMessage.yut = gameMessage.yut // 윷 던진 결과

			println("THROW : " + gameMessage.turn + " " + gameMessage.sender + " " + gameMessage.yut + " " + gameMessage.turnChange)
			sendingOperations.convertAndSend("/topic/game/throw/"+gameMessage.roomId,gameMessage)
		}
	}

	@MessageMapping("/game/question")
	fun question(gameMessage : GameMessage){
		if (GameStateType.QUESTION == gameMessage.type) {

			gameMessage.questionMessage = gameMessage.questionMessage

			println("QUESTION : " + gameMessage.turn + " " + gameMessage.sender + " " + gameMessage.questionMessage)
			sendingOperations.convertAndSend("/topic/game/question/"+gameMessage.roomId,gameMessage)

		}
	}

	@MessageMapping("/game/answer")
	fun answer(gameMessage : GameMessage){
		if (GameStateType.ANSWER == gameMessage.type) {
			gameMessage.type = GameStateType.ANSWER_RESULT

			println("ANSWER : " + gameMessage.turn + " " + gameMessage.sender + " " + gameMessage.answerMessage)
			sendingOperations.convertAndSend("/topic/game/answer/"+gameMessage.roomId,gameMessage)

		}
	}

	// 말 이동할 수 있는 위치 조회 요청
	@MessageMapping("/game/mal")
	fun getMalMovePosition(request : GameMalRequest.GetMalMovePositionDTO){
		println("말 이동 소켓 통신 테스트")
		println("request.yutResult = ${request.yutResult}")
		println("request.playerId = ${request.playerId}")

		// 게임 찾기 <- 원래 이거 Service 클래스에서 해야함.
		// 나머진 다 service 클래스에서
		//
	}
}
