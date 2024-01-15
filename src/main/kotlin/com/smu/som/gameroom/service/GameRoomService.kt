package com.smu.som.gameroom.service

import com.smu.som.gameroom.GameRoomSetting
import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import kotlin.math.min
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

@Service
@Slf4j
@RequiredArgsConstructor
@NoArgsConstructor
class GameRoomService (var gameRooms: HashMap<String, GameRoomSetting> = LinkedHashMap<String, GameRoomSetting>())
{
	fun createGameRoom(name: String, category: String, adult: String, malNum: Int): GameRoomSetting {
		var gameRoomSetting: GameRoomSetting = GameRoomSetting.of(name, category, adult, malNum)
		gameRooms.put(gameRoomSetting.roomId!!, gameRoomSetting)
		return gameRoomSetting
	}

	fun findAllRoom(): List<GameRoomSetting> {
		var result = ArrayList(gameRooms.values)
		result = result.filter { !it.playing } as ArrayList<GameRoomSetting>
		result.filterNotNull() as ArrayList<GameRoomSetting>
		result.reverse()

		return result
	}

	fun findRoom(pageNumber: Int, pageSize: Int): List<GameRoomSetting> {
		val result = findAllRoom()

		val startIndex = (pageNumber - 1) * pageSize
		val endIndex = min(startIndex + pageSize, result.size)

		return if (startIndex <= endIndex) {
			result.subList(startIndex, endIndex)
		} else {
			emptyList()
		}
	}

	fun findById(roomId: String): GameRoomSetting? {
		return gameRooms.get(roomId)
	}

	fun deleteById(roomId: String) {
		if (findById(roomId) != null) {
			gameRooms.remove(roomId)
		}
	}
	fun updateState(roomId: String, b: Boolean) : Boolean {
		if (findById(roomId) != null) {
			gameRooms.get(roomId)?.playing = b
			return true
		}
		return false
	}

}
