package com.smu.som.game.entity

import com.smu.som.game.dto.GameMalRequest
import com.smu.som.game.dto.GameMalResponse
import java.lang.RuntimeException

class PlayerTemp(id : String) {

	private var id : String
	private var malList : MutableList<Mal>
	private var disableMalList : MutableList<Mal>

	init {
		this.id = id
		malList = ArrayList<Mal>()
		disableMalList = ArrayList<Mal>()

		for(i in 0 until 4){
			malList.add(Mal(i))
		}
	}

	// 말 이동하기
	public fun moveMal(malId : Int, yutResult : YutResult): Int{
		val mal = findMal(malId)

		val nextPosition = mal.move(yutResult)

		// 도착했을 경우
		if(nextPosition == 0 && mal.isEnd()){
			malList.remove(mal)
			disableMalList.add(mal)
		}

		return nextPosition
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

	// 말 찾기
	private fun findMal(malId : Int) : Mal{
		for(mal in malList){
			if(mal.id == malId){
				return mal
			}
		}

		throw RuntimeException("말을 찾지 못했습니다")
	}

	public fun getMalList(): MutableList<Mal>{
		return malList
	}
}
