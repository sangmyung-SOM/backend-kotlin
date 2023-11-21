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

	fun userLimit(roomId: UUID): Boolean {
		val gameRoom = findRoom(roomId)
		if (gameRoom != null) {
			if (gameRoom.userCount == 2) {
				return true
			}
		}
		return false
	}

	fun joinRoom(roomId: UUID, userName: String): GameRoom? {
		val gameRoom = findRoom(roomId)
		if (gameRoom != null) {
			if (gameRoom.userCount < 2) {
				gameRoom.userCount++
				return gameRoom
			}
		}
		return null
	}

	fun joinRoom(roomId: UUID): GameRoom? {
		val gameRoom = findRoom(roomId)
		if (gameRoom != null) {
			if (gameRoom.userCount < 2) {
				gameRoom.userCount++
				return gameRoom
			}
		}
		return null
	}

	fun getUserCount(roomId: UUID): Int {
		val gameRoom = findRoom(roomId)
		if (gameRoom != null) {
			return gameRoom.userCount
		}
		return 0
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
