package com.smu.som.gameroom

import java.util.*

// 게임 방 설정 정보를 저장하는 DTO
class GameRoomSetting(
	var roomId:String?,
	var roomName:String?,
	var category:String?,
	var adult:String?,
	var playing:Boolean = false,
	var malNumLimit: Int
) {
	companion object {
		fun of(roomName: String, category: String, adult: String, malNumLimit: Int):GameRoomSetting{
			return GameRoomSetting(
				roomId= UUID.randomUUID().toString(),
				roomName=roomName,
				category=category,
				adult=adult,
				malNumLimit = malNumLimit
			)
		}
	}

}
