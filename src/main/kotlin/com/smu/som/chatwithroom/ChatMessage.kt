package com.smu.som.chatwithroom

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.NoArgsConstructor

@NoArgsConstructor
class ChatMessage(
	@JsonProperty("type")
    var type: MessageType?,
	@JsonProperty("roomId")
    var roomId :String?,
	@JsonProperty("sender")
    var sender:String?,
	@JsonProperty("message")
    var message:String?
)
