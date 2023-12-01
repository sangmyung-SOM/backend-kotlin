package com.smu.som.game

import com.smu.som.game.dto.GameMalRequest
import com.smu.som.game.dto.GameMalResponse
import com.smu.som.game.entity.Mal
import com.smu.som.game.entity.PlayerTemp
import org.springframework.stereotype.Service

@Service
class GameMalService {

	// 말 이동할 수 있는 위치 조회
	public fun getAllMalMovement(gameRoom: GameRoom, request: GameMalRequest.GetMalMovePositionDTO) : GameMalResponse.GetMalMovePosition{
		val player : PlayerTemp = gameRoom.findPlayer(request.playerId)

		// 말 움직일 수 있는 위치 찾기
		val nextPositionMap = player.findAllMalMovePosition(request.yutResult)

		var malMoveInfoList = ArrayList<GameMalResponse.MalMoveInfo>()
		for(mal in player.getMalList()){
			malMoveInfoList.add(
				GameMalResponse.MalMoveInfo(
					malId = mal.id,
					isEnd = mal.isEnd(),
					point = mal.getPoint(),
					position = mal.getPosition(),
					nextPosition = nextPositionMap.get(mal.id)!!
				)
			)
		}

		return GameMalResponse.GetMalMovePosition(
			userId = request.userId,
			playerId = request.playerId,
			yutResult = request.yutResult,
			malList = malMoveInfoList
		);
	}

	// 말 움직이기
	public fun moveMal(gameRoom: GameRoom, request: GameMalRequest.MoveMalDTO) : GameMalResponse.MoveMalDTO{
		val player = gameRoom.findPlayer(request.playerId)
		val oppPlayer = gameRoom.findOppPlayer(request.playerId)

		player.moveMal(request.malId, request.yutResult)
		val caughtMalId = player.catchMal(request.malId, oppPlayer)
		val updaMalId = player.updaMal(request.malId)

		val mal : Mal = player.findMal(request.malId)

		return GameMalResponse.MoveMalDTO(
			userId = request.userId,
			playerId = request.playerId,
			malId = request.malId,
			point = mal.getPoint(),
			nextPosition = mal.getPosition(),
			isEnd = mal.isEnd(),
			isCatchMal = if(caughtMalId == -1) false else true,
			catchMalId = caughtMalId,
			isUpdaMal = if(updaMalId == -1) false else true,
			updaMalId = updaMalId
		)
	}
}
