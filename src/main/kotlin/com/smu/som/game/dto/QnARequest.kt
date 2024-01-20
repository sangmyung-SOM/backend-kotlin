package com.smu.som.game.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.Expose
import lombok.Getter
import lombok.NoArgsConstructor

// 게임 내에서 받는 질문과 답변에 대한 DTO
class QnARequest {

	@Getter
	@NoArgsConstructor
	class GetQuestionDTO {

		@JsonProperty("question_id")
		var questionId : Long

		@JsonProperty("question")
		var question : String

		@JsonProperty("player_id")
		var playerId : String

		@JsonProperty("room_id")
		var roomId : String

		@JsonProperty("penalty")
		var penalty : Int

		constructor(questionId : Long, question : String, playerId : String, roomId : String, penalty : Int){
			this.questionId = questionId
			this.question = question
			this.playerId = playerId
			this.roomId = roomId
			this.penalty = penalty
		}
	}


	@Getter
	@NoArgsConstructor
	class GetAnswerDTO {

		@JsonProperty("answer")
		var answer : String

		@JsonProperty("player_id")
		var playerId : String

		@JsonProperty("room_id")
		var roomId : String

		constructor(answer : String, playerId : String, roomId : String){
			this.answer = answer
			this.playerId = playerId
			this.roomId = roomId
		}
	}

	data class GetQuestionWishDTO(
		@JsonProperty("answer")
		var question : String,

		@JsonProperty("player_id")
		var playerId : String,
	)
}
