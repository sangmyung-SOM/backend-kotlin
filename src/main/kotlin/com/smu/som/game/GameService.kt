package com.smu.som.game

import java.util.*
import kotlin.collections.HashSet

class GameService {

	private var gameRoomList: HashSet<GameRoom> = HashSet()

	private var btnState: Boolean = false

	fun findRoom(roomId: UUID): GameRoom? {
		for (gameRoom in gameRoomList) {
			if (gameRoom.roomId == roomId) {
				return gameRoom
			}
		}
		return null
	}


	fun setBtnState(gameState: String){
		if (gameState == "TRUE") {
			btnState =  true
		}
		btnState =  false
	}

	fun getBtnState() : Boolean {
		return btnState
	}

}
