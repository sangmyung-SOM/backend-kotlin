package com.smu.som.gameroom

import java.util.*

class GameRoomSetting(
	var roomId:String?,
	var roomName:String?,
	var category:String?,
	var adult:String?
) {
	companion object {
		fun of(roomName: String, category: String, adult: String):GameRoomSetting{
			return GameRoomSetting(
				roomId= UUID.randomUUID().toString(),
				roomName=roomName,
				category=category,
				adult=adult
			)
		}
	}

}
