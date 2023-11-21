package com.smu.som.game

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.smu.som.chatwithroom.MessageType

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GameMessage(
	@JsonProperty("messageType")
	var type: GameStateType?,
	@JsonProperty("gameRoomId")
	var roomId: String?,
	@JsonProperty("sender")
	var sender: String?,
	@JsonProperty("turn")
	var turn: String? = "",

	var turnChange: String? = "",
	var gameCategory: String? = "",
	var questionMessage: String? = "",
	var answerMessage: String? = "",
	var userNameList: String? = "",
	var yut: String? = "",
	var mal: String? = "",


)
