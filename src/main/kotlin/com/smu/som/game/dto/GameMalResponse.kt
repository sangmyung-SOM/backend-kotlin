package com.smu.som.game.dto

import com.smu.som.game.entity.YutResult
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import javax.persistence.Id

class GameMalResponse {

	@NoArgsConstructor
	@Getter
	public class GetMalMovePosition{
		var userId : Long
		var playerId : String
		var yutResult : YutResult
		var malList : List<MalMoveInfo>

		constructor(userId:Long, playerId:String, yutResult: YutResult, malList: List<MalMoveInfo>){
			this.userId = userId
			this.playerId = playerId
			this.yutResult = yutResult
			this.malList = malList
		}
	}

	public class MalMoveInfo{
		var malId : Int
		var isEnd: Boolean
		var point : Int
		var position : Int
		var nextPosition : Int

		constructor(malId: Int, isEnd : Boolean, point : Int, position : Int, nextPosition: Int){
			this.malId = malId
			this.isEnd = isEnd
			this.point = point
			this.position = position
			this.nextPosition = nextPosition
		}
	}

	public class MoveMalDTO{
		var userId : Long
		var playerId : String
		var malId : Int
		var point : Int
		var nextPosition : Int
		var isCatchMal : Boolean
		var catchMalId : Int
		var isUpdaMal : Boolean
		var updaMalId : Int

		constructor(userId: Long, playerId: String, malId: Int, point: Int, nextPosition: Int, isCatchMal: Boolean, catchMalId: Int, isUpdaMal: Boolean, updaMalId: Int){
			this.userId = userId
			this.playerId = playerId
			this.malId = malId
			this.point = point
			this.nextPosition = nextPosition
			this.isCatchMal = isCatchMal
			this.catchMalId = catchMalId
			this.isUpdaMal = isUpdaMal
			this.updaMalId = updaMalId
		}
	}
}
