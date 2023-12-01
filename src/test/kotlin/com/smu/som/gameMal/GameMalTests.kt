package com.smu.som.gameMal

import com.smu.som.game.entity.Mal
import com.smu.som.game.entity.PlayerTemp
import com.smu.som.game.entity.YutResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.RuntimeException

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

	@Test
	public fun 말움직이기_빽도_테스트(){
		// 빽도
		val mal : Mal = Mal(0)

		mal.move(YutResult.BACK_DO)
		println("빽도: mal.getPosition() = ${mal.getPosition()}, 도착 = ${mal.isEnd()}") // 0
	}

	@Test
	public fun 말잡기_테스트(){
		val playerA = PlayerTemp("1P")
		val playerB = PlayerTemp("2P")

		val mal0A = playerA.findMal(0)
		val mal0B = playerB.findMal(0)

		println("말 잡기 전")
		// 플레이어 A 말 [걸] 이동
		playerA.moveMal(0, YutResult.GIRL)
		println("A: mal0 position=${mal0A.getPosition()}")

		println("말 잡은 후")
		// 플레이어 B 말 [걸] 이동해 A의 말 잡기
		playerB.moveMal(0, YutResult.GIRL)
		playerB.catchMal(0, playerA)
		println("B: mal0 position=${mal0B.getPosition()}")
		println("A: mal0 position=${mal0A.getPosition()}")
	}

	@Test
	public fun 말업기_테스트(){
		val playerA = PlayerTemp("1P")

		val mal0A = playerA.findMal(0)
		val mal1A = playerA.findMal(1)

		println("말 업기 전")
		// 플레이어 A 말0 [윷] 이동
		playerA.moveMal(0, YutResult.YUT)
		println("mal 0: postion=${mal0A.getPosition()}, point=${mal0A.getPoint()}, valid=${mal0A.isValid()}")

		println("말 업은 후")
		// 플레이어 A 말1 [윷] 이동해 말0 업기
		playerA.moveMal(1, YutResult.YUT)
		playerA.updaMal(1)
		println("mal 0: postion=${mal0A.getPosition()}, point=${mal0A.getPoint()}, valid=${mal0A.isValid()}")
		println("mal 1: postion=${mal1A.getPosition()}, point=${mal1A.getPoint()}, valid=${mal1A.isValid()}")
	}

	@Test
	public fun 업은말_잡기_테스트(){
		val playerA = PlayerTemp("1P")
		val playerB = PlayerTemp("2P")

		val mal0A = playerA.findMal(0)
		val mal1A = playerA.findMal(1)
		val mal0B = playerB.findMal(0)

		println("말 잡기 전")
		// 플레이어 A말0 [모] 이동후 A말1 [모] 이동해 업기
		playerA.moveMal(0, YutResult.MO)
		playerA.moveMal(1, YutResult.MO)
		playerA.updaMal(1)
		println("A mal 0: postion=${mal0A.getPosition()}, point=${mal0A.getPoint()}, valid=${mal0A.isValid()}")
		println("A mal 1: postion=${mal1A.getPosition()}, point=${mal1A.getPoint()}, valid=${mal1A.isValid()}")

		println("말 잡은 후")
		// 플레이어 B말0 [모] 이동해 A말0,1 잡기
		playerB.moveMal(0, YutResult.MO)
		playerB.catchMal(0, playerA)
		println("A mal 0: postion=${mal0A.getPosition()}, point=${mal0A.getPoint()}, valid=${mal0A.isValid()}")
		println("A mal 1: postion=${mal1A.getPosition()}, point=${mal1A.getPoint()}, valid=${mal1A.isValid()}")
		println("B mal 0: postion=${mal0B.getPosition()}, point=${mal0B.getPoint()}, valid=${mal0B.isValid()}")

	}

	@Test
	public fun 업은말_또업기_테스트(){
		val playerA = PlayerTemp("1P")

		val mal0A = playerA.findMal(0)
		val mal1A = playerA.findMal(1)
		val mal2A = playerA.findMal(2)

		println("말 업기 전")
		// 플레이어 A 말0 [개] 이동
		playerA.moveMal(0, YutResult.GAE)
		println("mal 0: postion=${mal0A.getPosition()}, point=${mal0A.getPoint()}, valid=${mal0A.isValid()}")

		println("말 한번 업은 후")
		// 플레이어 A 말1 [개] 이동해 말0 업기
		playerA.moveMal(1, YutResult.GAE)
		playerA.updaMal(1)
		println("mal 0: postion=${mal0A.getPosition()}, point=${mal0A.getPoint()}, valid=${mal0A.isValid()}")
		println("mal 1: postion=${mal1A.getPosition()}, point=${mal1A.getPoint()}, valid=${mal1A.isValid()}")

		println("말 한번 더 업은 후")
		// 플레이어 A 말2 [개] 이동해 말0,1 업기
		playerA.moveMal(2, YutResult.GAE)
		playerA.updaMal(2)
		println("mal 0: postion=${mal0A.getPosition()}, point=${mal0A.getPoint()}, valid=${mal0A.isValid()}")
		println("mal 1: postion=${mal1A.getPosition()}, point=${mal1A.getPoint()}, valid=${mal1A.isValid()}")
		println("mal 2: postion=${mal2A.getPosition()}, point=${mal2A.getPoint()}, valid=${mal2A.isValid()}")
	}

	@Test
	public fun 유효하지_않은말_실행_테스트(){
		val playerA = PlayerTemp("1P")

		val mal0A = playerA.findMal(0)
		val mal1A = playerA.findMal(1)

		playerA.moveMal(0, YutResult.YUT)
		playerA.moveMal(1, YutResult.YUT)
		playerA.updaMal(1)

		// 업힌 말에 대해 실행 테스트
		Assertions.assertThrows(RuntimeException::class.java, {
			playerA.moveMal(0, YutResult.BACK_DO)
		})
	}

	@Test
	public fun 유효하지_않은말_실행_테스트2(){
		val playerA = PlayerTemp("1P")

		val mal0A = playerA.findMal(0)

		playerA.moveMal(0, YutResult.MO)
		println("mal 0: postion=${mal0A.getPosition()}, point=${mal0A.getPoint()}, valid=${mal0A.isValid()}")
		playerA.moveMal(0, YutResult.GIRL)
		println("mal 0: postion=${mal0A.getPosition()}, point=${mal0A.getPoint()}, valid=${mal0A.isValid()}")
		playerA.moveMal(0, YutResult.YUT) // 도착
		println("mal 0: postion=${mal0A.getPosition()}, point=${mal0A.getPoint()}, valid=${mal0A.isValid()}, isEnd=${mal0A.isEnd()}")

		// 도착한 말에 대해 실행 테스트
		Assertions.assertThrows(RuntimeException::class.java, {
			playerA.moveMal(0, YutResult.BACK_DO)
		})
	}

	@Test
	public fun 에러테스트(){
		val playerA = PlayerTemp("1P")

		val mal0A = playerA.findMal(0)

		playerA.moveMal(0, YutResult.BACK_DO)
		playerA.updaMal(0)
		println("mal 0: postion=${mal0A.getPosition()}, point=${mal0A.getPoint()}, valid=${mal0A.isValid()}")

		playerA.moveMal(0, YutResult.DO)
	}
}
