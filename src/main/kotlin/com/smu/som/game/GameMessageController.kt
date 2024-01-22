package com.smu.som.game

import com.smu.som.game.dto.*
import com.smu.som.game.entity.GameRoom
import com.smu.som.game.entity.PlayerTemp
import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.concurrent.ConcurrentHashMap

// 게임 소켓 컨트롤러
@RestController
@RequiredArgsConstructor
@NoArgsConstructor
class GameMessageController(val sendingOperations: SimpMessageSendingOperations, val gameMalService: GameMalService)
{

	// 게임방 리스트
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

	// 게임 처응 입장 및 게임 시작 메시지
	@MessageMapping("/game/message")
	fun gameWait(gameMessage: GameMessage.GetGameInfo){

		// WAIT 처음 입장
		if (GameStateType.WAIT == gameMessage.type) {

			if (gameMessage.playerId == "1P") {
				gameMessage.sender?.let {
					roomList.put(GameRoom(UUID.fromString(gameMessage.roomId), gameMessage.sender, gameMessage.playerId, gameMessage.profileURL_1P, gameMessage.malNumLimit) , it)
				}
			} else if (gameMessage.playerId == "2P") {
				gameMessage.sender?.let {
					roomList.put(GameRoom(UUID.fromString(gameMessage.roomId), gameMessage.sender, gameMessage.playerId, gameMessage.profileURL_2P, gameMessage.malNumLimit) , it)
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
					gameMessage.type = GameStateType.START
					gameMessage.userNameList = "${gameMessage.sender},$name2p"
					gameMessage.profileURL_2P = profileURL_2P
				}
			}

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
	}

	// 게잉 윷 던진 결과
	@MessageMapping("/game/throw")
	fun yutThrow(gameMessage : GameMessage.GetThrowResult){
		val gameService = GameService()
		val gameRoom = findGameRoom(gameMessage.roomId!!)
		val player = gameRoom.findPlayer(gameMessage.playerId!!)
		val num = gameService.playGame(player.yuts.sum())

		// 처음 던진 윷이 빽도인 경우 한번 더 던짐
		if (GameStateType.FIRST_THROW == gameMessage.type && num == 0) {

			gameMessage.type = GameStateType.FIRST_THROW
			gameMessage.yut = num.toString() // 윷 던진 결과

			sendingOperations.convertAndSend("/topic/game/throw/"+gameMessage.roomId,gameMessage)
			return
		}

		player.yuts[num] += 1
		println("윷 던진 결과 : $num, ${gameMessage.type}, ${gameMessage.roomId}, ${gameMessage.playerId}")
		if (GameStateType.THROW == gameMessage.type || GameStateType.FIRST_THROW == gameMessage.type) {
			if(num == 4 || num == 5) //윷이나 모
				gameMessage.type = GameStateType.ONE_MORE_THROW

			gameMessage.yut = num.toString() // 윷 던진 결과

			sendingOperations.convertAndSend("/topic/game/throw/"+gameMessage.roomId,gameMessage)
		}
	}

	// 질문 결과 전송 (질문을 요청하는 api가 따로 있고, 실시간 질문 공유를 위해 만듬)
	@MessageMapping("/game/question")
	fun question(request: QnARequest.GetQuestionDTO){

		val gameRoom : GameRoom = findGameRoom(request.roomId)
		val player : PlayerTemp? = request.playerId?.let { gameRoom.findPlayer(it) }
		request.penalty = player?.getPenalty()!!

		sendingOperations.convertAndSend("/topic/game/question/"+request.roomId, request)
	}

	// 질문 변경 1회를 클릭한 경우
	@MessageMapping("/game/question/pass")
	fun questionPass(request: QnARequest.GetQuestionDTO){
		val gameRoom : GameRoom = findGameRoom(request.roomId)
		val player : PlayerTemp? = request.playerId?.let { gameRoom.findPlayer(it) }

		player?.addPenalty()

		request.penalty = player?.getPenalty()!!

		sendingOperations.convertAndSend("/topic/game/question/"+request.roomId, request)

		// 턴 변경
		turnChange(request.roomId, request.playerId, GameStateType.TURN_CHANGE)
		player.yuts = IntArray(6) {0}
	}

	// 추가 질문권 사용
	@MessageMapping("/game/question/wish")
	fun questionWish(request: QnARequest.GetAnswerDTO){

		if (request.answer == "추가 질문권 사용") {
			sendingOperations.convertAndSend("/topic/game/question/wish/notice"+request.roomId, request)
			return
		}

		val response = QnARequest.GetQuestionWishDTO(
			playerId = request.playerId,
			question = request.answer
		)
		sendingOperations.convertAndSend("/topic/game/question/wish"+request.roomId, response)
	}

	// 질문에 대한 답변
	@MessageMapping("/game/answer")
	fun answer(request: QnARequest.GetAnswerDTO){
		val playerTemp = findGameRoom(request.roomId).findPlayer(request.playerId!!)
		playerTemp?.initPenalty()
		sendingOperations.convertAndSend("/topic/game/answer/" + request.roomId, request)
	}

	// 말 이동할 수 있는 위치 조회 요청
	@MessageMapping("/game/mal")
	fun getNextPositionOfAllMal(request : GameMalRequest.GetMalMovePositionDTO){
		println("말 이동할 수 있는 위치 조회 소켓 통신 테스트")
		println("request.playerId = ${request.playerId}")
		println("request.gameId = ${request.gameId}")
		println("request.yutResult = ${request.yutResult}")

		val gameRoom : GameRoom = findGameRoom(request.gameId)

		// 나머진 다 service 클래스에서
		val response: GameMalResponse.GetMalMovePosition = gameMalService.getNextPositionOfAllMal(gameRoom, request)

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
		val player = gameRoom.findPlayer(request.playerId!!)

		if(request.malId > gameRoom.malNumLimit-1){
			throw RuntimeException("제한된 말 개수를 넘는 요청")
		}

		val response : GameMalResponse.MoveMalDTO = gameMalService.moveMal(gameRoom, request)

		val num = request.yutResult.ordinal

		player.yuts[num] -= 1

		// 말 잡은 경우 한 번 더 던지기
		if(response.isCatchMal) {
			val gameMessage = GameMessage.GetThrowResult(
				roomId = request.gameId,
				playerId = request.playerId,
				yut = request.yutResult.toString(),
				type = GameStateType.CATCH_MAL
			)
			sendingOperations.convertAndSend("/topic/game/throw/"+gameMessage.roomId,gameMessage)
			turnChange(request.gameId, request.playerId, GameStateType.MY_TURN)
		}
		// 말 이동 하고도 결과가 남아있는 경우 -> 턴 변경하지 않음
		else if (player.yuts.sum() >= 1) { 
			if (player.yuts.sum() == 1 && (num != 4 && num != 5)) { // 윷이나 모가 아닌 경우
				turnChange(request.gameId, request.playerId, GameStateType.TURN_CHANGE)
			}
			else turnChange(request.gameId, request.playerId, GameStateType.MY_TURN)
		}
		// 윷이나 모가 안나오고 도 개 걸 빽도 중 한가지가 나온 경우 -> 턴 변경
		else if (player.yuts.sum() == 0)
		{
			if (num == 0 || num == 1 || num == 2 || num == 3) {
				turnChange(request.gameId, request.playerId, GameStateType.TURN_CHANGE)
			}
			// 윷이나 모가 나왔는데 윷을 한 번 더 던지기 전에 윷이나 모만큼 말을 이동시키는 경우 -> 턴 변경하지 않음(한 번 더 던져야함)
			else turnChange(request.gameId, request.playerId, GameStateType.MY_TURN)
			player.yuts = IntArray(6) { 0 }
		}

		val url = StringBuilder("/topic/game/")
			.append(request.gameId)
			.append("/mal/move")
			.toString()
		sendingOperations.convertAndSend(url, response)
	}

	// 게잉 점수 조회
	@MessageMapping("/game/score")
	fun getPlayerScore(request: GameScoreRequest.getGameScoreDTO) {

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
//		request.player2Score = gameRoom.player2.getScore()

		// 테스트를 위한 임시 코드
//		request.player2Score = gameRoom.malNumLimit

		// 스코어가 4점이면 게임 종료
		if (request.player1Score == gameRoom.malNumLimit) {
			sendWinner("1P", "2P", request.gameId)
		} else if (request.player2Score == gameRoom.malNumLimit) {
			sendWinner("2P", "1P", request.gameId)
		}

		sendingOperations.convertAndSend("/topic/game/score/" + request.gameId, request)
	}

	// 질문 패스권 조회 및 사용
	@MessageMapping("/game/room/wish/pass")
	fun passWish(request: GameMessage.GetPassWish) {

		val gameId = request.roomId
		val gameRoom : GameRoom = findGameRoom(gameId!!)
		val player : PlayerTemp? = request.playerId?.let { gameRoom.findPlayer(it) }

		if (request.passCard == 0) {
			player?.usePassCard()

		} else if (request.passCard == 1) {
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
	private fun findGameRoom(gameId:String): GameRoom {
		for(item in roomList.keys){
			if(item.roomId == UUID.fromString(gameId)){
				return item
			}
		}

		throw RuntimeException("게임을 찾을 수 없음.")
	}

	// 턴 변경
	private fun turnChange(gameId: String, playerId: String, type: GameStateType) {
		val turnChange = GameMessage.GetTurnChange(
			roomId = gameId,
			playerId = playerId,
			type = type
		)
		// Log 출력
		if (type == GameStateType.TURN_CHANGE) {
			println("턴 변경")
		}
		else println("턴 유지 player : $playerId")
		sendingOperations.convertAndSend("/topic/game/turn/" + turnChange.roomId, turnChange)
	}
}
