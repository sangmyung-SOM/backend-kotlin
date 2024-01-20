package com.smu.som.game

import com.smu.som.game.dto.GameMalRequest
import com.smu.som.game.dto.GameMalResponse
import com.smu.som.game.entity.GameRoom
import com.smu.som.game.entity.Mal
import com.smu.som.game.entity.PlayerTemp
import com.smu.som.game.entity.YutResult
import org.springframework.stereotype.Service
import kotlin.collections.ArrayList

@Service
class GameMalService {

	// 말 이동할 수 있는 위치 조회
	public fun getNextPositionOfAllMal(gameRoom: GameRoom, request: GameMalRequest.GetMalMovePositionDTO) : GameMalResponse.GetMalMovePosition{
		val player : PlayerTemp = gameRoom.findPlayer(request.playerId)
		var newMalId : Int = -1 // 새로 추가할 수 있는 말
		var isSuccess: Boolean = true

		if(!player.isYutValid(request.yutResult)){
			isSuccess = false
		}

		// 말 움직일 수 있는 위치 찾기
		val nextPositionMap = player.findNextPositionOfAllMal(request.yutResult)

		val malMoveInfoList: ArrayList<GameMalResponse.MalMoveInfo>  = ArrayList()
		for(entry in nextPositionMap.entries){
			val mal = player.findMal(entry.key)

			if(mal.getPosition() == 0){ // 새로 추가할 수 있는 말
				if(mal.id < newMalId || newMalId == -1){ // 가장 작은 id를 가진 말부터 추가하게 하기 위함
					newMalId = mal.id
				}
			}
			else{ // 기존에 윷판에 있던 말
				malMoveInfoList.add(
					GameMalResponse.MalMoveInfo(
						malId = mal.id,
						isEnd = mal.isEnd(),
						point = mal.getPoint(),
						position = mal.getPosition(),
						nextPosition = entry.value
					)
				)
			}
		}

		// 윷판에 말이 있는데 빽도가 나왔을 경우 새로운 말을 추가할 수 없음
		val disableAddNewMal = (request.yutResult == YutResult.BACK_DO && malMoveInfoList.isNotEmpty())
		if(disableAddNewMal){
			newMalId = -1
		}

		return GameMalResponse.GetMalMovePosition(
			isSuccess = isSuccess,
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

		if(!player.isYutValid(request.yutResult)){
			throw RuntimeException("사용자가 던지지 않은 윷을 이용하려고 함.")
		}
		if(!mal.isValid()){
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
