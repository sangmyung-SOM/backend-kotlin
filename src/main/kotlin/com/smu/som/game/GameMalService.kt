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
		var newMalId : Int = -1 // 새로 추가할 수 있는 말

		// 말 움직일 수 있는 위치 찾기
		val nextPositionMap = player.findAllMalMovePosition(request.yutResult)

		var malMoveInfoList: ArrayList<GameMalResponse.MalMoveInfo>  = ArrayList()
		for(mal in player.getMalList()){
			if(!mal.isValid()){
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
					nextPosition = nextPositionMap.get(mal.id)!!
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
		var caughtMalList : List<Int> = ArrayList<Int>()
		var updaMalId : Int = -1

		val movement = player.moveMal(request.malId, request.yutResult)
		if(!mal.isEnd()){
			caughtMalList = player.catchMal(request.malId, oppPlayer)
			updaMalId = player.updaMal(request.malId)
		}

		return GameMalResponse.MoveMalDTO(
			userId = request.userId,
			playerId = request.playerId,
			malId = request.malId,
			point = mal.getPoint(),
			movement = movement,
			nextPosition = mal.getPosition(),
			isEnd = mal.isEnd(),
			isCatchMal = if(caughtMalList.size == 0) false else true,
			catchMalList = caughtMalList,
			isUpdaMal = if(updaMalId == -1) false else true,
			updaMalId = updaMalId
		)
	}
}
