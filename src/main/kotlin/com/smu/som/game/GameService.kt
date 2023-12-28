package com.smu.som.game

import java.util.*
import kotlin.collections.HashSet

class GameService {

	private var gameRoomList: HashSet<GameRoom> = HashSet()

	private var btnState: Boolean = false

	fun playGame(sum: Int): Int {
		return percentage(sum)
	}

	private fun percentage(sum: Int) : Int {
		val range = (1..16)
		var per = arrayOf(1, 3, 6, 4, 1, 1)     // 윷 확률
		if (sum > 0)
			per = arrayOf(1, 4, 6, 5)           // 확률 재설정
		var num = range.random()

		for ((index,item) in per.withIndex()) {
			if (num <= item)
				return index
			num -= item
		}
		return -1
	}
}
