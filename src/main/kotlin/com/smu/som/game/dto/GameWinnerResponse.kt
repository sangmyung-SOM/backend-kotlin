package com.smu.som.game.dto

import lombok.Getter
import lombok.NoArgsConstructor

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
