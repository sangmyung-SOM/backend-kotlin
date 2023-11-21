package com.smu.som.game

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
