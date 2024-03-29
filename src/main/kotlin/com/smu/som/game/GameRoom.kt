package com.smu.som.game

import com.smu.som.game.dto.GameMalResponse
import com.smu.som.game.entity.PlayerTemp
import com.smu.som.game.entity.YutResult
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import java.util.ArrayList
import java.util.UUID

@NoArgsConstructor
@Getter
@Setter
class GameRoom(roomId: UUID, sender: String?, turn: String?) {

	var userCount: Int = 0
	var roomId: UUID = roomId
	var userName: String? = sender
	var turn: String? = turn

	// 이렇게 저장해놓는것보다 배열이나 hashmap으로 저장해놓는게 좋을텐데..
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

	public fun findPlayer(playerId: String) : PlayerTemp{
		return if(playerId.equals("1P")) player1 else player2
	}
}
