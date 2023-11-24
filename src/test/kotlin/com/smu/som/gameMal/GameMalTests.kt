package com.smu.som.gameMal

import com.smu.som.game.entity.Mal
import com.smu.som.game.entity.YutResult
import org.junit.jupiter.api.Test

class GameMalTests {
	
	@Test
	public fun 말움직이기_테스트(){
		// 도 -> 윷 -> 걸 -> 개 ->  빽도 -> 도 -> 모(도착)
		val mal : Mal = Mal(0)

		mal.move(YutResult.DO)
		println("도: mal.getPosition() = ${mal.getPosition()}") // 1

		mal.move(YutResult.YUT)
		println("윷: mal.getPosition() = ${mal.getPosition()}") // 5

		mal.move(YutResult.GIRL)
		println("걸: mal.getPosition() = ${mal.getPosition()}") // 23

		mal.move(YutResult.GAE)
		println("개: mal.getPosition() = ${mal.getPosition()}") // 30

		mal.move(YutResult.BACK_DO)
		println("빽도: mal.getPosition() = ${mal.getPosition()}") // 29

		mal.move(YutResult.DO)
		println("도: mal.getPosition() = ${mal.getPosition()}") // 20

		mal.move(YutResult.MO)
		println("모: mal.getPosition() = ${mal.getPosition()}, 도착 = ${mal.isEnd()}") // 0
	}
}
