package com.smu.som.game

import com.smu.som.game.dto.*
import com.smu.som.game.entity.PlayerTemp
import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.web.bind.annotation.RestController
import java.lang.RuntimeException
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@RestController
@RequiredArgsConstructor
@NoArgsConstructor
class GameMessageController(val sendingOperations: SimpMessageSendingOperations, val gameMalService: GameMalService)
{

	var roomList = ConcurrentHashMap<GameRoom, String>() // 게임방, 유저아이디


	// 게임 연결 끊김
	@MessageMapping("/game/disconnect")
	fun gameDisconnect(gameMessage: GameMessage.GetGameDisconnect) {

		sendingOperations.convertAndSend("/topic/game/disconnect/${gameMessage.roomId}", gameMessage)

		var isDisconnect = false
		// 1P, 2P 둘 중 연결이 한명이라도 끊기면 게임방 삭제
		roomList.forEach {
			if (it.key.roomId == UUID.fromString(gameMessage.roomId) && it.key.turn == gameMessage.playerId) {
				isDisconnect = true
			}
		}

		if (isDisconnect) {
			roomList.forEach {
				if (it.key.roomId == UUID.fromString(gameMessage.roomId)) {
					roomList.remove(it.key)
				}
			}

		}

	}

	@MessageMapping("/game/message")
	fun gameWait(gameMessage: GameMessage.GetGameInfo){

		// WAIT 처음 입장
		if (GameStateType.WAIT == gameMessage.type) {

			if (gameMessage.playerId == "1P") {
				gameMessage.sender?.let {
					roomList.put(GameRoom(UUID.fromString(gameMessage.roomId), gameMessage.sender, gameMessage.playerId, gameMessage.profileURL_1P) , it)
				}
			} else if (gameMessage.playerId == "2P") {
				gameMessage.sender?.let {
					roomList.put(GameRoom(UUID.fromString(gameMessage.roomId), gameMessage.sender, gameMessage.playerId, gameMessage.profileURL_2P) , it)
				}
			}

			if (gameMessage.playerId == "1P") {
				var name2p : String? = null
				var profileURL_2P : String? = null

				// 2P가 먼저 들어온 경우
				roomList.forEach {
					if (it.key.roomId == UUID.fromString(gameMessage.roomId) && it.key.turn == "2P") {
						name2p = it.value
						profileURL_2P = it.key.profileURL
					}
				}

				if (name2p != null) {
					println("2P가 들어와있습니다.")
					gameMessage.type = GameStateType.START
					gameMessage.userNameList = "${gameMessage.sender},$name2p"
					gameMessage.profileURL_2P = profileURL_2P
				}
			}

			println("WAIT STATE : " + gameMessage.playerId + " " + gameMessage.sender + " " + gameMessage.roomId)

			//1P가 있는 방에 2P가 들어오면 게임 시작 메시지 전송
			if (gameMessage.playerId == "2P") {
				gameMessage.type = GameStateType.START

				var name1p: String? = null
				val name2p = gameMessage.sender

				var profileURL_1P: String? = null

				// 2P가 들어오면 1P의 이름을 가져옴
				roomList.forEach {
					if (it.key.roomId == UUID.fromString(gameMessage.roomId) && it.key.turn == "1P") {
						name1p = it.value
						profileURL_1P = it.key.profileURL
					}
				}

				// 예외사항 : 2P가 1P 보다 먼저 들어오게 되는 경우 대기
				if (name1p == null) {
					println("1P가 들어오지 않았습니다.")
					gameMessage.type = GameStateType.WAIT
					gameMessage.message = "1P가 들어오지 않았습니다."

					sendingOperations.convertAndSend("/topic/game/room/" + gameMessage.roomId, gameMessage)
				}
				else {
					gameMessage.userNameList = "$name1p,$name2p"
					gameMessage.profileURL_1P = profileURL_1P
				}
			}
			sendingOperations.convertAndSend("/topic/game/room/" + gameMessage.roomId, gameMessage)
		}

		// 1P 2P 모두 입장 후 게임 시작
		if (GameStateType.START == gameMessage.type) {
			println("START STATE : " + gameMessage.playerId + " " + gameMessage.sender + " " + gameMessage.roomId)

		}
	}

	@MessageMapping("/game/start")
	fun gameStart(gameMessage: GameMessage.GetGameInfo){
		println("START : $gameMessage")

		sendingOperations.convertAndSend("/topic/game/start/"+gameMessage.roomId,gameMessage)

	}

	@MessageMapping("/game/throw")
	fun yutThrow(gameMessage : GameMessage.GetThrowResult){
		val gameService = GameService()
		val gameRoom = findGameRoom(gameMessage.roomId!!)
		val num = gameService.playGame(gameRoom.yuts.sum())
		gameRoom.yuts[num] += 1
		
		if (GameStateType.THROW == gameMessage.type) {
			if(num == 4 || num == 5) //윷이나 모
			{
				gameMessage.type = GameStateType.ONE_MORE_THROW
			}
			gameMessage.yut = num.toString() // 윷 던진 결과

			sendingOperations.convertAndSend("/topic/game/throw/"+gameMessage.roomId,gameMessage)
		}
		println("THROW : " + gameMessage.yut + " " + gameMessage.playerId + " " + gameMessage.roomId)
	}

	@MessageMapping("/game/question")
	fun question(request: QnARequest.GetQuestionDTO){

		println("QUESTION : " + request.question + " " + request.playerId)
		val gameRoom : GameRoom = findGameRoom(request.roomId)
		val player : PlayerTemp? = request.playerId?.let { gameRoom.findPlayer(it) }
		request.penalty = player?.getPenalty()!!

		sendingOperations.convertAndSend("/topic/game/question/"+request.roomId, request)
	}

	// 질문 변경을 선택한 경우 패널티 적립 (말 놓기 X)
	@MessageMapping("/game/question/pass")
	fun questionPass(request: QnARequest.GetQuestionDTO){
		val gameRoom : GameRoom = findGameRoom(request.roomId)
		val player : PlayerTemp? = request.playerId?.let { gameRoom.findPlayer(it) }
		player?.addPenalty()

		request.penalty = player?.getPenalty()!!
		sendingOperations.convertAndSend("/topic/game/question/"+request.roomId, request)

		// 패널티로 말 놓기 불가능 -- 말 놓기 버튼 비활성화 : 구현하기

	}

	// 상대방에게 내가 원하는 질문
	@MessageMapping("/game/question/wish")
	fun questionWish(request: QnARequest.GetAnswerDTO){
		val response = QnARequest.GetQuestionWishDTO(
			playerId = request.playerId,
			question = request.answer
		)
		sendingOperations.convertAndSend("/topic/game/question/wish"+request.roomId, response)
	}

	@MessageMapping("/game/answer")
	fun answer(request: QnARequest.GetAnswerDTO){

		sendingOperations.convertAndSend("/topic/game/answer/" + request.roomId, request)
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
		val response: GameMalResponse.GetMalMovePosition = gameMalService.getAllMalMovement(gameRoom, request)

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

		val num = request.yutResult.ordinal
		gameRoom.yuts[num] -= 1


		// 말 잡은 경우
		if(response.isCatchMal) {

			var gameMessage = GameMessage.GetThrowResult(
				roomId = request.gameId,
				playerId = request.playerId,
				yut = request.yutResult.toString(),
				type = GameStateType.CATCH_MAL
			)

			println("말 잡았다고 알려주기")
			println("gameMessage = ${gameMessage}")

			sendingOperations.convertAndSend("/topic/game/throw/"+gameMessage.roomId,gameMessage)
		}
		// 턴 변경 아직 수정중 - 소영
		else if (gameRoom.yuts.sum() > 0 && (num == 4 || num == 5)) {
				val notTurnChange = GameMessage.GetTurnChange(
					roomId = request.gameId,
					playerId = request.playerId,
					type = GameStateType.MY_TURN
				)
				println("턴 변경하지 않고 한 번 더 던질 수 있음")
				sendingOperations.convertAndSend("/topic/game/turn/"+notTurnChange.roomId,notTurnChange)
			}
		// 윷 합이 0인 경우 : 윷이나 모가 안나옴 -> 턴 변경
		else if (gameRoom.yuts.sum() == 0)
		{
			val turnChange = GameMessage.GetTurnChange(
				roomId = request.gameId,
				playerId = request.playerId,
				type = GameStateType.TURN_CHANGE
			)
			// 윷 리스트 초기화
			gameRoom.yuts = IntArray(6) { 0 }

			println("턴 변경!")
			sendingOperations.convertAndSend("/topic/game/turn/"+turnChange.roomId,turnChange)
		}

		val url = StringBuilder("/topic/game/")
			.append(request.gameId)
			.append("/mal/move")
			.toString()
		sendingOperations.convertAndSend(url, response)
	}

	// 플레이어 점수 조회
	@MessageMapping("/game/score")
	fun getPlayerScore(request: GameScoreRequest.getGameScoreDTO) {
		println("플레이어 점수 조회 소켓 통신 테스트")

		var gameRoom : GameRoom = findGameRoom(request.gameId)
		val player : PlayerTemp? = request.playerId?.let { gameRoom.findPlayer(it) }

		player?.getMalList()?.forEach {
			// 이미 점수를 얻은 말이면 넘어감
			if(it.isScored) {
				return@forEach
			}
			if(it.isEnd()) {
				player.addScore(1, it.id)
				it.isScored = true
			}
		}

		request.player1Score = gameRoom.player1.getScore()
		request.player2Score = gameRoom.player2.getScore()

		// 스코어가 4점이면 게임 종료
		if (request.player1Score == 4) {
			sendWinner("1P", "2P", request.gameId)
		} else if (request.player2Score == 4) {
			sendWinner("2P", "1P", request.gameId)
		}

		sendingOperations.convertAndSend("/topic/game/score/" + request.gameId, request)
	}

	// 소원권 패스 적립
	@MessageMapping("/game/room/wish/pass")
	fun passWish(request: GameMessage.GetPassWish) {
		println("소원권 패스 적립 소켓 통신 테스트")
		println("request.playerId = ${request.playerId}")
		println("request.roomId = ${request.roomId}")

		val gameId = request.roomId
		val gameRoom : GameRoom = findGameRoom(gameId!!)
		val player : PlayerTemp? = request.playerId?.let { gameRoom.findPlayer(it) }

		if (request.passCard == 0) {
			println("소원권 사용")
			player?.usePassCard()

		} else if (request.passCard == 1) {
			println("소원권 적립")
			player?.addPassCard()

		}
		val response = GameMessage.GetPassWish(
			roomId = gameId,
			playerId = request.playerId,
			passCard = player?.getPassCard(),
		)

		val url = StringBuilder("/topic/game/room/")
			.append(gameId)
			.append("/wish/pass")
			.toString()

		println("소원권 패스 ${response.passCard}개 남음 ${response.playerId}")
		sendingOperations.convertAndSend(url, response)

	}

	// 게임 종료
	private fun sendWinner(winner: String, loser: String, gameId: String) {
		// 승리, 패배 플레이어 정보
		val gameWinnerResponse = GameWinnerResponse.GetGameWinnerDTO(winner, loser)
		val url = StringBuilder("/topic/game/")
			.append(gameId)
			.append("/end")
			.toString()

		sendingOperations.convertAndSend(url, gameWinnerResponse)

		// 게임 종료 후 방 삭제
		roomList.remove(findGameRoom(gameId))

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
