package com.smu.som.game.entity

class Mal (val id : Int) {

	private var position : Int
	private var updaMalList : MutableList<Mal> // 업은 말
	private var isEnd : Boolean
	private var isUped : Boolean // 다른 말에게 업혔는지 여부
	var isScored: Boolean

	init {
		this.position = 0
		this.updaMalList = ArrayList()
		this.isEnd = false
		this.isUped = false
		this.isScored = false
	}

	// 말 움직이기
	public fun move(yutResult: YutResult) : List<Int>{
		checkValid()

		val nextPosition = findNextPosition(yutResult)
		val nextPositionValue = nextPosition.last()

		// 말이 도착했을 경우
		if(nextPositionValue == 0 && this.position != 0){
			println("말이 도착했습니다. $id")
			finish()

			// 업은 말에 대해서도 도착 처리 해줌
			for(updaMal in updaMalList){
				updaMal.finish()
			}
		}

		this.position = nextPosition.last()

		return nextPosition
	}

	// 말 움직이는 위치 찾기
	public fun findNextPosition(yutResult: YutResult) : List<Int>{

//		checkValid()

		val movements = ArrayList<Int>()

		// 빽도일 경우
		if(yutResult == YutResult.BACK_DO){
			when(position){
				0 -> {movements.add(0)}
				1 -> { movements.add(20)}
				21 -> {movements.add(5)}
				23->{movements.add(27)}
				29->{movements.add(23)}
				// 끝
				20->{movements.add(0)}
				// 나머지
				else -> {movements.add(position-1)}
			}
			return movements
		}

		// 도~모 일경우
		var nextPosition : Int = position
		when(nextPosition){ // 교차점에서 시작할 경우를 위해서
			// 교차점일 경우
			5->{movements.add(21); nextPosition = 21}
			10->{movements.add(26); nextPosition = 26}
			23->{movements.add(29); nextPosition = 29}
			// 교차점이 아닌 경우중 위치의 index가 확 바뀌는 부분
			25->{movements.add(15); nextPosition = 15}
			27->{movements.add(23); nextPosition = 23}
			30->{movements.add(20); nextPosition = 20}
			// 끝
			20->{ movements.add(0); return movements }
			// 나머지
			else->{ movements.add(nextPosition+1); nextPosition++}
		}

		for(i in 0 until yutResult.move-1){
			when(nextPosition){
				// 교차점이 아닌 경우중 위치의 index가 확 바뀌는 부분
				25->{movements.add(15); nextPosition =15}
				27->{movements.add(23); nextPosition =23}
				30->{movements.add(20); nextPosition =20}
				// 끝
				20->{ movements.add(0); return movements }
				// 나머지
				else->{movements.add(nextPosition+1); nextPosition++}
			}
		}

		return movements
	}

	// 말 잡기
	public fun catch(oppMalList : Array<Mal>): List<Int>{
		checkValid()

		val tempUpdaMalList = ArrayList<Int>()

		if(this.position == 0){ // 아직 윷판 위에 못올라간 말임
			return tempUpdaMalList
		}

		for(oppMal in oppMalList){
			if(oppMal.position == this.position && oppMal.isValid()) { // 잡음
				oppMal.position = 0
				oppMal.isUped = false
				tempUpdaMalList.add(oppMal.id)

				for(oppUpdaMal in oppMal.updaMalList){ // 잡은 말의 업은 말들에 대해 처리
					oppUpdaMal.position = 0
					oppUpdaMal.isUped = false
					oppUpdaMal.updaMalList.clear()
					tempUpdaMalList.add(oppUpdaMal.id)
				}

				oppMal.updaMalList.clear()

				return tempUpdaMalList
			}
		}

		return tempUpdaMalList
	}

	// 말 업기
	public fun upda(malList: Array<Mal>): Int {
		checkValid()

		if(this.position == 0){ // 아직 윷판 위에 못올라간 말임
			return -1
		}

		for(mal in malList){
			if(mal.position == this.position && mal.id != this.id && mal.isValid()){ // 업음
				mal.isUped = true
				for(updaMal in mal.updaMalList) { // 업으려는 말의 업힌 말에 대해 처리
					this.updaMalList.add(updaMal)
				}
				this.updaMalList.add(mal)

				return mal.id
			}
		}

		return -1;
	}

	// 말이 유효한지 확인
	// 다른 말에 업혀있거나, 이미 끝난 말이면 false 리턴
	public fun isValid() : Boolean{
		return !(isEnd || isUped)
	}

	// 말이 유효한지 확인
	// 다른 말에 업혀있거나, 이미 끝난 말이면 예외 발생
	private fun checkValid(){
		if (isEnd || isUped){
			throw RuntimeException("말이 유효하지 않음")
		}
	}

	public fun getPosition():Int{
		return position
	}

	// 업은 말의 개수
	public fun getPoint() : Int{
		return updaMalList.size + 1
	}

	private fun finish(){
		this.isEnd = true
	}

	public fun isEnd() : Boolean{
		return isEnd
	}

}
