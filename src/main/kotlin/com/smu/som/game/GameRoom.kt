package com.smu.som.game

import com.smu.som.game.entity.PlayerTemp
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import java.util.UUID

@NoArgsConstructor
@Getter
@Setter
class GameRoom(roomId: UUID, sender: String?, turn: String?) {

	var userCount: Int = 0
	var roomId: UUID = roomId
	var userName: String? = sender
	var turn: String? = turn

	private val player1 : PlayerTemp = PlayerTemp("1P")
	private val player2 : PlayerTemp = PlayerTemp("2P")

	companion object {
		fun of(roomId: UUID, sender: String?, turn: String?): GameRoom {
			return GameRoom(
				roomId = roomId,
				sender = sender,
				turn = turn
			)
		}

	}



}
