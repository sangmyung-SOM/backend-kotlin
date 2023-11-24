package com.smu.som.gameroom.service

import com.smu.som.gameroom.GameRoomSetting
import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service

@Service
@Slf4j
@RequiredArgsConstructor
@NoArgsConstructor
class GameRoomService (var gameRooms: HashMap<String, GameRoomSetting> = LinkedHashMap<String, GameRoomSetting>())
{
	fun createGameRoom(name: String, category: String, adult: String): GameRoomSetting {
		var gameRoomSetting: GameRoomSetting = GameRoomSetting.of(name, category, adult)
		gameRooms.put(gameRoomSetting.roomId!!, gameRoomSetting)
		return gameRoomSetting
	}

	fun findAllRoom(): List<GameRoomSetting> {
		var result = ArrayList(gameRooms.values)
		result.reverse()

		return result
	}

	fun findById(roomId: String): GameRoomSetting? {
		return gameRooms.get(roomId)
	}

	fun deleteById(roomId: String) {
		gameRooms.remove(roomId)
	}

}
