package com.smu.som.game

import com.smu.som.game.dto.GameMalRequest
import com.smu.som.game.dto.GameMalResponse
import com.smu.som.game.entity.Mal
import com.smu.som.game.entity.PlayerTemp
import org.springframework.stereotype.Service

@Service
class GameMalService {

	// 말 이동할 수 있는 위치 조회
	public fun getNextPositionOfAllMal(gameRoom: GameRoom, request: GameMalRequest.GetMalMovePositionDTO) : GameMalResponse.GetMalMovePosition{
		val player : PlayerTemp = gameRoom.findPlayer(request.playerId)
		var newMalId : Int = -1 // 새로 추가할 수 있는 말

		// 말 움직일 수 있는 위치 찾기
		val nextPositionMap = player.findNextPositionOfAllMal(request.yutResult)

		var malMoveInfoList: ArrayList<GameMalResponse.MalMoveInfo>  = ArrayList()
		for(mal in player.getMalList()){
			if(!mal.isValid()){ // 업혀있거나 도착해서 사용할 수 없는 말
				continue
			}

			if(mal.getPosition() == 0){ // 새로 추가할 수 있는 말
				newMalId = mal.id
				continue
			}

			// 기존에 윷판에 있던 말
			malMoveInfoList.add(
				GameMalResponse.MalMoveInfo(
					malId = mal.id,
					isEnd = mal.isEnd(),
					point = mal.getPoint(),
					position = mal.getPosition(),
					nextPosition = nextPositionMap[mal.id]!!
				)
			)
		}

		return GameMalResponse.GetMalMovePosition(
			userId = request.userId,
			playerId = request.playerId,
			yutResult = request.yutResult,
			newMalId = newMalId,
			malList = malMoveInfoList
		);
	}

	// 말 움직이기
	public fun moveMal(gameRoom: GameRoom, request: GameMalRequest.MoveMalDTO) : GameMalResponse.MoveMalDTO{
		val player = gameRoom.findPlayer(request.playerId)
		val oppPlayer = gameRoom.findOppPlayer(request.playerId)

		val mal : Mal = player.findMal(request.malId)
		var caughtMalList : List<Int> = ArrayList()
		var updaMalId : Int = -1

		if(!mal.isValid()){
			val tempRequest : GameMalRequest.GetMalMovePositionDTO = GameMalRequest.GetMalMovePositionDTO(
				userId = request.userId,
				playerId = request.playerId,
				gameId = request.gameId,
				yutResult = request.yutResult
			);
			val temp:GameMalResponse.GetMalMovePosition = getNextPositionOfAllMal(gameRoom, tempRequest)
			for(malInfo in temp.malList){
				println("말id: ${malInfo.malId}, 현위치: ${malInfo.position}, 다음위치: ${malInfo.nextPosition}, 끝?: ${malInfo.isEnd}")
			}
			println("temp = ${temp}, malInfoSize: ${temp.malList.size}, newMalId=${temp.newMalId}")
			for(tempMal in player.getMalList()){
				println("말id: ${tempMal.id}, 업힌말?: ${tempMal.isUped()}, 끝난말?: ${tempMal.isEnd()}, 현위치: ${tempMal.getPosition()}")
			}
			throw RuntimeException("유효하지 않은 말! 게임방: ${gameRoom.roomId}에서 말id: ${mal.id}, 현위치 ${mal.getPosition()}를 이동시키려 함")
		}

		val movement = player.moveMal(mal, request.yutResult)
		if(!mal.isEnd()){
			caughtMalList = player.catchMal(mal, oppPlayer)
			updaMalId = player.updaMal(mal)
		}

		return GameMalResponse.MoveMalDTO(
			userId = request.userId,
			playerId = request.playerId,
			malId = request.malId,
			point = mal.getPoint(),
			movement = movement,
			nextPosition = mal.getPosition(),
			isEnd = mal.isEnd(),
			isCatchMal = caughtMalList.isNotEmpty(),
			catchMalList = caughtMalList,
			isUpdaMal = (updaMalId != -1),
			updaMalId = updaMalId
		)
	}
}
