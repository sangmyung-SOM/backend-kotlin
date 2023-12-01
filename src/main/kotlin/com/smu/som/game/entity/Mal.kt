package com.smu.som.game.entity

class Mal (val id : Int) {

	private var position : Int
	private var updaMalList : MutableList<Mal> // 업은 말
	private var isEnd : Boolean
	private var isUped : Boolean // 다른 말에게 업혔는지 여부

	init {
		this.position = 0
		this.updaMalList = ArrayList()
		this.isEnd = false
		this.isUped = false
	}

	// 말 움직이기
	public fun move(yutResult: YutResult) : Int{
		checkValid()

		val nextPosition = findNextPosition(yutResult)

		// 말이 도착했을 경우
		if(nextPosition == 0 && this.position != 0){
			finish()

			// 업은 말에 대해서도 도착 처리 해줌
			for(updaMal in updaMalList){
				updaMal.finish()
			}
		}

		this.position = nextPosition

		return nextPosition
	}

	// 말 움직이는 위치 찾기
	public fun findNextPosition(yutResult: YutResult) : Int{

		checkValid()

		// 빽도일 경우
		if(yutResult == YutResult.BACK_DO){
			when(position){
				0 -> {return 0}
				1 -> { return 20 }
				21 -> {return 5}
				23->{return 27}
				29->{return 23}
				// 끝
				20->{return 0}
				// 나머지
				else -> {return position-1}
			}
		}

		// 도~모 일경우
		var nextPosition : Int = position
		when(nextPosition){ // 교차점에서 시작할 경우를 위해서
			// 교차점일 경우
			5->{nextPosition = 21}
			10->{nextPosition = 26}
			23->{nextPosition = 29}
			// 교차점이 아닌 경우중 위치의 index가 확 바뀌는 부분
			25->{nextPosition = 15}
			27->{nextPosition = 23}
			30->{nextPosition = 20}
			// 끝
			20->{ return 0 }
			// 나머지
			else->{nextPosition++}
		}

		for(i in 0 until yutResult.move-1){
			when(nextPosition){
				// 교차점이 아닌 경우중 위치의 index가 확 바뀌는 부분
				25->{nextPosition =15}
				27->{nextPosition =23}
				30->{nextPosition =20}
				// 끝
				20->{ return 0 }
				// 나머지
				else->{nextPosition++}
			}
		}

		return nextPosition
	}

	// 말 잡기
	public fun catch(oppMalList : Array<Mal>): Int{
		checkValid()

		if(this.position == 0){ // 아직 윷판 위에 못올라간 말임
			return -1
		}

		for(oppMal in oppMalList){
			if(oppMal.position == this.position && oppMal.isValid()) { // 잡음
				oppMal.position = 0
				oppMal.isUped = false

				for(oppUpdaMal in oppMal.updaMalList){ // 잡은 말의 업은 말들에 대해 처리
					oppUpdaMal.position = 0
					oppUpdaMal.isUped = false
					oppUpdaMal.updaMalList.clear()
				}
				oppMal.updaMalList.clear()

				return oppMal.id
			}
		}

		return -1
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
