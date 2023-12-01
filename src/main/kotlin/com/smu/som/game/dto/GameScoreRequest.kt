package com.smu.som.game.dto

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Getter
import lombok.NoArgsConstructor

class GameScoreRequest {

	@Getter
	@NoArgsConstructor
	class getGameScoreDTO {
		@JsonProperty("game_id")
		var gameId: String

		@JsonProperty("player_id")
		var playerId: String

		var player1Score: Int

		var player2Score: Int

		constructor(gameId: String, playerId: String, player1Score: Int, player2Score: Int) {
			this.gameId = gameId
			this.playerId = playerId
			this.player1Score = player1Score
			this.player2Score = player2Score
		}
	}

}
