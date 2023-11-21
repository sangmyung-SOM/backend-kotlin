package com.smu.som.game.entity

class PlayerTemp(id : String) {

	private var id : String
	private var malCollection : MalCollection

	init {
		this.id = id
	    malCollection = MalCollection()
	}
}
