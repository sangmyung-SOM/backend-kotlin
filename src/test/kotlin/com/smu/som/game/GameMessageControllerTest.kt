package com.smu.som.game

import com.smu.som.game.entity.PlayerTemp
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class GameMessageControllerTest {

    @Test
    fun moveMal() {
		val player = PlayerTemp("1P")
		val player2 = PlayerTemp("2P")

		/*
		- 윷도 -> 윷으로 상대말 잡음 -> 윷 던져서 걸 나옴
		-> 도걸 남음 -> 도 움직임 -> 상대로 턴 넘어감
		-> 걸 움직임 -> 나에게 다시 턴이 넘어옴(원래 상대턴이어야함)
		 */
		player.yuts = IntArray(6) { 0 }
		player2.yuts = IntArray(6) { 0 }
		player.yuts[3] = 1
		player.yuts[2] = 1

		var num = 3
		player.yuts[num] -= 1
		player.addThrowChance()

		if (player.getThrowChance() >= 1) {
			println("0 MY_TURN")
			player.resetThrowChance()
		}
		else if (player.yuts.sum() == 0 && (num == 4 || num == 5)) {
			println("1 MY_TURN")
			player.yuts = IntArray(6) { 0 }
		}
		// 말 이동 하고도 결과가 남아있는 경우 -> 턴 변경하지 않음
		else if (player.yuts.sum() >= 1) { // 이동할 말들이 아직 남아있는 경우
			println("2 MY_TURN")
		}
		// 윷이나 모가 아니면서 결과가 남아있는 경우 말 이동만 하고 끝이기 때문에 턴 변경
		else if (player.yuts.sum() == 1 && (num != 4 && num != 5)) {
			println("3 TURN_CHANGE")
			// 윷이나 모 결과가 아직 남아있기 때문에 초기화 하지 않음
		}
		// 윷이나 모가 안나오고 도 개 걸 빽도 중 한가지가 나온 경우 -> 턴 변경
		else if (player.yuts.sum() == 0)
		{
			println("4 TURN_CHANGE")
			player.yuts = IntArray(6) { 0 }
		}
    }
}
