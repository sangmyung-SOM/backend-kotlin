package com.smu.som.gameroom

import java.util.*

class GameRoomSetting(
	var roomId:String?,
	var roomName:String?,
	var category:String?,
	var adult:String?,
	var playing:Boolean = false,
	var malNum: Int
) {
	companion object {
		fun of(roomName: String, category: String, adult: String, malNum: Int):GameRoomSetting{
			return GameRoomSetting(
				roomId= UUID.randomUUID().toString(),
				roomName=roomName,
				category=category,
				adult=adult,
				malNum = malNum
			)
		}
	}

}
