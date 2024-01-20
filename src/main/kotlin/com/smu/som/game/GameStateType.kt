package com.smu.som.game

// 게임 상태를 나타내는 enum class
enum class GameStateType {
	START, THROW, FIRST_THROW,
	WAIT, TURN_CHANGE,
	ONE_MORE_THROW, MY_TURN,
	CATCH_MAL, END
}
