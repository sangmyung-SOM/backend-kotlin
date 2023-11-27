package com.smu.som.game

import com.smu.som.game.dto.GameMalRequest
import com.smu.som.game.dto.GameMalResponse
import com.smu.som.game.dto.GameScoreRequest
import com.smu.som.game.entity.PlayerTemp
import com.smu.som.game.entity.YutResult
import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.web.bind.annotation.RestController
import java.lang.RuntimeException
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@RestController
@RequiredArgsConstructor
@NoArgsConstructor
class GameMessageController(val sendingOperations: SimpMessageSendingOperations, val gameMalService: GameMalService)
{

	var roomList = HashMap<GameRoom, String>() // 게임방, 유저아이디
	var nameList = ArrayList<String>()
	var gameSetting = HashMap<GameRoom, List<String>>() // 게임방, 게임설정


	@MessageMapping("/game/message")
	fun gameWait(gameMessage: GameMessage){

		// WAIT 처음 입장
		if (GameStateType.WAIT == gameMessage.type) {

			gameMessage.sender?.let {
				roomList.put(GameRoom(UUID.fromString(gameMessage.roomId), gameMessage.sender, gameMessage.turn),
					it // 유저닉네임
				)
			}

			if (gameMessage.turn == "1P") {
				var category = gameMessage.gameCategory?.split(",")?.get(0)
				var kcategory = gameMessage.gameCategory?.split(",")?.get(1)
				var adult = gameMessage.gameCategory?.split(",")?.get(2)

				if (category != null && kcategory != null && adult != null) {
					gameSetting[GameRoom(UUID.fromString(gameMessage.roomId), gameMessage.sender, gameMessage.turn)] =
						listOf(category, kcategory, adult)
				}
				var name2p : String? = null
				println("$category $kcategory $adult")

				roomList.forEach {
					if (it.key.roomId == UUID.fromString(gameMessage.roomId) && it.key.turn == "2P") {
						name2p = it.value
					}
				}

				if (name2p != null) {
					println("2P가 들어와있습니다.")
					gameMessage.type = GameStateType.START
					gameMessage.userNameList = "${gameMessage.sender},$name2p"
				}



			}

			println("WAIT STATE : " + gameMessage.turn + " " + gameMessage.sender + " " + gameMessage.roomId)


			//1P가 있는 방에 2P가 들어오면 게임 시작 메시지 전송
			if (gameMessage.turn == "2P") {
				gameMessage.type = GameStateType.START

				var name1p: String? = null
				val name2p = gameMessage.sender

				// 2P가 들어오면 1P의 이름을 가져옴
				roomList.forEach {
					if (it.key.roomId == UUID.fromString(gameMessage.roomId) && it.key.turn == "1P") {
						name1p = it.value
					}
				}

				// 예외사항 : 2P가 1P 보다 먼저 들어오게 되는 경우 대기
				if (name1p == null) {
					println("1P가 들어오지 않았습니다.")
					gameMessage.type = GameStateType.WAIT
					gameMessage.answerMessage = "1P가 들어오지 않았습니다."
					sendingOperations.convertAndSend("/topic/game/room/" + gameMessage.roomId, gameMessage)
				}
				else {
					gameMessage.userNameList = "$name1p,$name2p"
					gameSetting.forEach {
						if (it.key.roomId == UUID.fromString(gameMessage.roomId) && it.key.sender == name1p) {
							gameMessage.gameCategory = it.value[0] + "," + it.value[1] + "," + it.value[2]
						}
					}

				}
			}
			sendingOperations.convertAndSend("/topic/game/room/" + gameMessage.roomId, gameMessage)
		}

		// 1P 2P 모두 입장 후 게임 시작
		if (GameStateType.START == gameMessage.type) {

			println("START STATE : " + gameMessage.turn + " " + gameMessage.sender + " " + gameMessage.roomId)

		}
	}

	@MessageMapping("/game/start")
	fun gameStart(gameMessage: GameMessage){

		println("START : $gameMessage")

		sendingOperations.convertAndSend("/topic/game/start/"+gameMessage.roomId,gameMessage)

	}

	@MessageMapping("/game/throw")
	fun yutThrow(gameMessage : GameMessage){

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
		gameMessage.questionMessage = gameMessage.questionMessage

		println("QUESTION : " + gameMessage.turn + " " + gameMessage.sender + " " + gameMessage.questionMessage)
		sendingOperations.convertAndSend("/topic/game/question/"+gameMessage.roomId,gameMessage)
	}

	@MessageMapping("/game/answer")
	fun answer(gameMessage : GameMessage){
		gameMessage.type = GameStateType.ANSWER_RESULT
		println("ANSWER : " + gameMessage.turn + " " + gameMessage.sender + " " + gameMessage.answerMessage)
		sendingOperations.convertAndSend("/topic/game/answer/"+gameMessage.roomId,gameMessage)
	}

	// 말 이동할 수 있는 위치 조회 요청
	@MessageMapping("/game/mal")
	fun getMalMovePosition(request : GameMalRequest.GetMalMovePositionDTO){
		println("말 이동할 수 있는 위치 조회 소켓 통신 테스트")
		println("request.playerId = ${request.playerId}")
		println("request.gameId = ${request.gameId}")
		println("request.yutResult = ${request.yutResult}")
		

		var gameRoom : GameRoom = findGameRoom(request.gameId)


		// 나머진 다 service 클래스에서
		val response = gameMalService.getAllMalMovement(gameRoom, request)

		val url = StringBuilder("/topic/game/")
			.append(request.gameId)
			.append("/mal")
			.toString()
		sendingOperations.convertAndSend(url, response)
	}

	// 말 이동하기
	@MessageMapping("/game/mal/move")
	fun moveMal(request: GameMalRequest.MoveMalDTO){
		println("말 이동 소켓 통신 테스트")
		println("request.playerId = ${request.playerId}")
		println("request.gameId = ${request.gameId}")
		println("request.yutResult = ${request.yutResult}")
		println("request.malId = ${request.malId}")

		val gameRoom: GameRoom = findGameRoom(request.gameId)

		val response : GameMalResponse.MoveMalDTO = gameMalService.moveMal(gameRoom, request)

		val url = StringBuilder("/topic/game/")
			.append(request.gameId)
			.append("/mal/move")
			.toString()
		sendingOperations.convertAndSend(url, response)
	}

	// 플레이어 점수 조회
	@MessageMapping("/game/score")
	fun getPlayerScore(request: GameMessage) {
		println("플레이어 점수 조회 소켓 통신 테스트")

		var gameRoom : GameRoom = findGameRoom(request.roomId.toString())
		val player : PlayerTemp? = request.turn?.let { gameRoom.findPlayer(it) }

		player?.getMalList()?.forEach {
			if(it.isEnd()) {
				player.addScore(1)
			}
		}

		request.player1Score = gameRoom.player1.getScore()
		request.player2Score = gameRoom.player2.getScore()

		println("request.playerId = ${request.turn}, " +
			"request.1pScore = ${request.player1Score}, " +
			"request.2pScore = ${request.player2Score}")

		if (request.player1Score == 4) {
			request.winner = "1P"
		} else if (request.player2Score == 4) {
			request.winner = "2P"
		}

		sendingOperations.convertAndSend("/topic/game/score" + request.roomId, request)
	}

	// 게임 찾기 <- 이것도 원래는 service 클래스에서 해야함
	private fun findGameRoom(gameId:String):GameRoom{
		for(item in roomList.keys){
			if(item.roomId == UUID.fromString(gameId)){
				return item
			}
		}

		throw RuntimeException("게임을 찾을 수 없음.")
	}
}
