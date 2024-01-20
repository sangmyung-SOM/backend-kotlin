package com.smu.som.game.dto

import lombok.Getter
import lombok.NoArgsConstructor

// 게임 승자 정보 DTO
class GameWinnerResponse {

	@Getter
	@NoArgsConstructor
	class GetGameWinnerDTO {
		var winner: String = ""
		var loser : String = ""

		constructor(winner: String, loser: String) {
			this.winner = winner
			this.loser = loser
		}
	}

}
