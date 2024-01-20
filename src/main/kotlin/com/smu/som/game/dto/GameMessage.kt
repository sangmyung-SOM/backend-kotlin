package com.smu.som.game.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.smu.som.game.GameStateType
import lombok.Getter
import lombok.NoArgsConstructor

class GameMessage {

	@Getter
	@NoArgsConstructor
	class GetGameInfo {
		@JsonProperty("messageType")
		var type: GameStateType?

		@JsonProperty("room_id")
		var roomId: String?

		@JsonProperty("sender")
		var sender: String?
		@JsonProperty("player_id")
		var playerId: String? = ""
		@JsonProperty("mal_num_limit")
		var malNumLimit: Int

		var userNameList: String? = ""
		var profileURL_1P: String? = ""
		var profileURL_2P: String? = ""
		var message: String? = ""

		constructor(type: GameStateType?, roomId: String?, sender: String?, playerId: String?, malNumLimit: Int) {
			this.type = type
			this.roomId = roomId
			this.sender = sender
			this.playerId = playerId
			this.malNumLimit = malNumLimit
		}
	}

	data class GetThrowResult(
		@JsonProperty("messageType")
		var type: GameStateType?,
		@JsonProperty("room_id")
		var roomId: String?,
		@JsonProperty("player_id")
		var playerId: String?,
		@JsonProperty("yut")
		var yut: String?
	)

	data class GetGameDisconnect(
		@JsonProperty("messageType")
		var type: GameStateType?,
		@JsonProperty("room_id")
		var roomId: String?,
		@JsonProperty("player_id")
		var playerId: String?
	)

	data class GetTurnChange(
		@JsonProperty("messageType")
		var type: GameStateType?,
		@JsonProperty("room_id")
		var roomId: String?,
		@JsonProperty("player_id")
		var playerId: String?
	)

	data class GetPassWish (
		@JsonProperty("room_id")
		var roomId: String?,
		@JsonProperty("player_id")
		var playerId: String?,
		@JsonProperty("pass_card")
		var passCard: Int?
	)

}
