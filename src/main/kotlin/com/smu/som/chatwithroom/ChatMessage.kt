package com.smu.som.chatwithroom

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import lombok.NoArgsConstructor

data class ChatMessage(
	@JsonProperty("messageType")
    var type: MessageType?,
	@JsonProperty("chatRoomId")
    var roomId :String?,
	@JsonProperty("sender")
    var sender:String?,
    var message:String?=""
)
