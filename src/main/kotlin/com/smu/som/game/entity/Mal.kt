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
}
