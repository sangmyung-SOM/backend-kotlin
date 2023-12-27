package com.smu.som.game.entity

class PlayerTemp(id : String) {

	private var id : String // 1P 또는 2P
	private var malList : Array<Mal>
	private var score : IntArray
	private var passCard : Int
	private var penalty : Boolean
	var yuts : IntArray

	init {
		this.id = id
		malList = Array<Mal>(4, {i -> Mal(i)})
		this.score = IntArray(4){ 0 }
		this.passCard = 0
		this.penalty = false
		this.yuts = IntArray(6){ 0 }

	}

	// 말 이동하기
	public fun moveMal(mal : Mal, yutResult : YutResult): List<Int>{
		return mal.move(yutResult)
	}


	/**
	 * 모든 말에 대해 윷 결과만큼 움직였을 때 위치
	 * @return key: 말id, value: Mal
	 */
	public fun findNextPositionOfAllMal(yutResult : YutResult) : Map<Int, Int> {

		var map = HashMap<Int, Int>() // 말id, nextPosition

		for(mal in malList){
			if(!mal.isValid()){
				continue
			}
			map.put(mal.id, mal.findNextPosition(yutResult).last())
		}

		return map
	}

	// 말 잡기
	public fun catchMal(mal : Mal, oppPlayer: PlayerTemp) : List<Int>{
		return mal.catch(oppPlayer.malList)
	}

	// 말 업기
	public fun updaMal(mal : Mal) : Int{
		return mal.upda(malList)
	}

	public fun findMal(malId: Int): Mal{
		return malList[malId]
	}

	// 해당 윷을 사용자가 던졌었는지 확인
	public fun checkYutValid(yutResult: YutResult) : Boolean{
		return yuts[yutResult.id] > 0
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
		this.penalty = true

		// 패널티로 던진 윷 사용 못함
		for(i in 0 until 6){
			yuts[i] = 0
		}
	}

	fun getPenalty(): Int {
		if(penalty){
			return 1
		}
		else{
			return 0
		}
	}

	fun usePassCard() {
		this.passCard--
	}
}
