package com.smu.som.game

import com.smu.som.game.entity.PlayerTemp
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import java.util.UUID

@NoArgsConstructor
@Getter
@Setter
class GameRoom(var roomId: UUID, var sender: String?, var turn: String?, var profileURL: String?, val malNumLimit: Int) {

	// 이렇게 저장해놓는것보다 배열이나 hashmap으로 저장해놓는게 좋을텐데..
	val player1 : PlayerTemp = PlayerTemp("1P", malNumLimit)
	val player2 : PlayerTemp = PlayerTemp("2P", malNumLimit)


	companion object {
		fun of(roomId: UUID, sender: String?, turn: String?, profileURL: String?, malNumLimit: Int): GameRoom {
			return GameRoom(
				roomId = roomId,
				sender = sender,
				turn = turn,
				profileURL = profileURL,
				malNumLimit = malNumLimit
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
