package com.smu.som.game.entity

import com.smu.som.game.dto.GameMalRequest
import com.smu.som.game.dto.GameMalResponse

class PlayerTemp(id : String) {

	private var id : String // 1P 또는 2P
	private var malList : Array<Mal>

	init {
		this.id = id
		malList = Array<Mal>(4, {i -> Mal(i)})

	}

	// 말 이동하기
	public fun moveMal(malId : Int, yutResult : YutResult): Int{

		val mal = malList[malId]

		if(!mal.isValid()){
			throw RuntimeException("사용자가 유효하지 않은 말을 움직이려 함")
		}

		return mal.move(yutResult)
	}

	// 말 잡기
	public fun catchMal(malId : Int, oppPlayer: PlayerTemp) : Int{

		val mal = malList[malId]

		if(!mal.isValid()){
			throw RuntimeException("사용자가 유효하지 않은 말을 움직이려 함")
		}

		return mal.catch(oppPlayer.malList)
	}

	// 말 업기
	public fun updaMal(malId : Int,) : Int{

		val mal = malList[malId]

		if(!mal.isValid()){
			throw RuntimeException("사용자가 유효하지 않은 말을 움직이려 함")
		}

		return mal.upda(malList)
	}

	/**
	 * 모든 말에 대해 윷 결과만큼 움직였을 때 위치
	 * @return key: 말id, value: Mal
	 */
	public fun findAllMalMovePosition(yutResult : YutResult) : Map<Int, Int> {

		var map = HashMap<Int, Int>() // 말id, nextPosition

		for(mal in malList){
			map.put(mal.id, mal.findNextPosition(yutResult))
		}

		return map
	}

	public fun findMal(malId: Int): Mal{
		return malList[malId]
	}

	public fun getMalList(): Array<Mal>{
		return this.malList
	}
}
