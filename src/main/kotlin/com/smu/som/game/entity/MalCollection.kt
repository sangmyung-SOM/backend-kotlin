package com.smu.som.game.entity

class MalCollection {

	private var malList : HashMap<Int, Mal>
	private var disableMalList : HashMap<Int, Mal> // 업힌 말 리스트

	init {
	    malList = HashMap()
		disableMalList = HashMap()

		// 말 4개 초기화
		for(i in 0 until 4){
			malList.put(i, Mal(i))
		}
	}
}
