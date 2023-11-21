package com.smu.som.game

import com.smu.som.game.entity.PlayerTemp
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import java.util.UUID

@NoArgsConstructor
@Getter
@Setter
class GameRoom(roomId: UUID, sender: String?) {

	var roomId: UUID = roomId
	var userName: String? = sender

	var userCount: Int = 0

	var turn: String = ""

	private val player1 : PlayerTemp = PlayerTemp("1P")
	private val player2 : PlayerTemp = PlayerTemp("2P")

	companion object {
		fun of(roomId: UUID, sender: String?): GameRoom {
			return GameRoom(
				roomId = roomId,
				sender = sender
			)
		}

	}


	fun userLimit(): Boolean {
		if(userCount == 2) {
			return true
		}
		return false
	}


}
