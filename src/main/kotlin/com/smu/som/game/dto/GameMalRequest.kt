package com.smu.som.game.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.smu.som.game.entity.YutResult
import lombok.Getter
import lombok.NoArgsConstructor

class GameMalRequest {

	@Getter
	@NoArgsConstructor
	public class GetMalMovePositionDTO{

		@JsonProperty("user_id")
		var userId : Long

		@JsonProperty("player_id")
		var playerId : String

		@JsonProperty("game_id")
		var gameId : String

		@JsonProperty("yut_result")
		var yutResult : YutResult

		constructor(userId : Long, playerId : String, gameId: String, yutResult: YutResult){
			this.userId = userId
			this.playerId = playerId
			this.gameId = gameId
			this.yutResult = yutResult
		}
	}

	@Getter
	@NoArgsConstructor
	public class MoveMalDTO{

		@JsonProperty("user_id")
		var userId : Long

		@JsonProperty("player_id")
		var playerId : String

		@JsonProperty("game_id")
		var gameId : String

		@JsonProperty("mal_id")
		var malId : Int

		@JsonProperty("yut_result")
		var yutResult : YutResult

		constructor(userId : Long, playerId : String, gameId: String, malId: Int, yutResult: YutResult){
			this.userId = userId
			this.playerId = playerId
			this.gameId = gameId
			this.malId = malId
			this.yutResult = yutResult
		}
	}
}
