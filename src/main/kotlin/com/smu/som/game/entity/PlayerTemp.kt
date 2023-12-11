package com.smu.som.game.entity

class PlayerTemp(id : String) {

	private var id : String // 1P 또는 2P
	private var malList : Array<Mal>
	private var score : IntArray
	private var passCard : Int
	private var penalty : Int

	init {
		this.id = id
		malList = Array<Mal>(4, {i -> Mal(i)})
		this.score = IntArray(4){ 0 }
		this.passCard = 0
		this.penalty = 0

	}

	// 말 이동하기
	public fun moveMal(malId : Int, yutResult : YutResult): List<Int>{

		val mal = malList[malId]

		if(!mal.isValid()){
			throw RuntimeException("사용자가 유효하지 않은 말을 움직이려 함")
		}

		return mal.move(yutResult)
	}

	// 말 잡기
	public fun catchMal(malId : Int, oppPlayer: PlayerTemp) : List<Int>{

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
			map.put(mal.id, mal.findNextPosition(yutResult).last())
		}

		return map
	}

	public fun findMal(malId: Int): Mal{
		return malList[malId]
	}

	public fun getMalList(): Array<Mal>{
		return this.malList
	}

	fun addScore(score: Int, id: Int) {
		// 말 id당 1번씩만 점수를 추가할 수 있음 (중복 방지)
		this.score[id] += score
	}

	fun getScore(): Int {
		return score.sum()
	}

	fun addPassCard() {
		this.passCard++
	}

	fun getPassCard(): Int {
		return this.passCard
	}

	fun addPenalty() {
		this.penalty++
	}

	fun getPenalty(): Int {
		return this.penalty
	}

	fun usePassCard() {
		this.passCard--
	}
}
