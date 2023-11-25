package com.smu.som.game.entity

class Mal (val id : Int) {

	private var position : Int
	private var point : Int // 업은 말의 개수
	private var updaMalList : List<Mal>
	private var isEnd : Boolean

	init {
		this.position = 0
		this.point = 1
		this.updaMalList = ArrayList()
		this.isEnd = false
	}

	// 말 움직이기
	public fun move(yutResult: YutResult) : Int{
		val nextPosition = findNextPosition(yutResult)

		// 말이 도착했을 경우
		if(nextPosition == 0 && this.position != 0){
			finish()
		}

		this.position = nextPosition

		return nextPosition
	}

	// 말 움직이는 위치 찾기
	public fun findNextPosition(yutResult: YutResult) : Int{

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

	public fun getPosition():Int{
		return position
	}

	private fun finish(){
		this.isEnd = true
	}

	public fun getPoint() : Int{
		return point
	}

	public fun isEnd() : Boolean{
		return isEnd
	}
}
