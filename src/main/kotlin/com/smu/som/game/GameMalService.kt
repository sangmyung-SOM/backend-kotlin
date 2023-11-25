package com.smu.som.game

import com.smu.som.game.dto.GameMalRequest
import com.smu.som.game.dto.GameMalResponse
import com.smu.som.game.entity.PlayerTemp
import org.springframework.stereotype.Service

@Service
class GameMalService {

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
}
