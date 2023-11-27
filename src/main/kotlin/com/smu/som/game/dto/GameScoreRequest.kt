package com.smu.som.game.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GameScoreRequest (
	@JsonProperty("game_id")
	var gameId : String,

	@JsonProperty("player_id")
	var playerId : String,

	@JsonProperty("1P_score")
	var player1Score : Int,

	@JsonProperty("2P_score")
	var player2Score : Int

)
