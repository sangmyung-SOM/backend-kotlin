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
class GameRoom(var roomId: UUID, var sender: String?, var turn: String?, var profileURL: String?) {

	// 이렇게 저장해놓는것보다 배열이나 hashmap으로 저장해놓는게 좋을텐데..
	val player1 : PlayerTemp = PlayerTemp("1P")
	val player2 : PlayerTemp = PlayerTemp("2P")

	var yuts = IntArray(6){ 0 }

	companion object {
		fun of(roomId: UUID, sender: String?, turn: String?, profileURL: String?): GameRoom {
			return GameRoom(
				roomId = roomId,
				sender = sender,
				turn = turn,
				profileURL = profileURL
			)
		}

	}

	public fun findPlayer(playerId: String) : PlayerTemp{
		return if(playerId.equals("1P")) player1 else player2
	}

	public fun findOppPlayer(playerId: String) : PlayerTemp{
		return if(playerId.equals("1P")) player2 else player1
	}
}
