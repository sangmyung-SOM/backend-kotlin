package com.smu.som.chatwithroom

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.NoArgsConstructor


data class ChatMessage(
    var type: MessageType?,
    var roomId :String?,
    var sender:String?,
    var message:String?
)
