package com.smu.som.game.entity

import com.smu.som.game.dto.GameMalRequest
import com.smu.som.game.dto.GameMalResponse

class PlayerTemp(id : String) {

	private var id : String
	private var malList : MutableList<Mal>

	init {
		this.id = id
		malList = ArrayList<Mal>()

		for(i in 0 until 4){
			malList.add(Mal(i))
		}
	}

	// 말 이동하기
	public fun moveMal(mal : Mal, yutResult : YutResult): Int{
		if(!mal.isValid()){
			throw RuntimeException("사용자가 유효하지 않은 말을 움직이려 함")
		}

		return mal.move(yutResult)
	}

	// 말 잡기
	public fun catchMal(mal : Mal, oppPlayer: PlayerTemp) : Int{
		if(!mal.isValid()){
			throw RuntimeException("사용자가 유효하지 않은 말을 움직이려 함")
		}

		return mal.catch(oppPlayer.malList)
	}

	// 말 업기
	public fun updaMal(mal : Mal) : Int{
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


	// 말 찾기
	public fun findMal(malId : Int) : Mal{
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
