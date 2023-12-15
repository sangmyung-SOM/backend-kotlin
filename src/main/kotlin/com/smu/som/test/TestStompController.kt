package com.smu.som.test

import com.smu.som.game.dto.*
import com.smu.som.game.entity.PlayerTemp
import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.web.bind.annotation.RestController
import java.lang.RuntimeException
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@RestController
@RequiredArgsConstructor
class TestStompController(val sendingOperations: SimpMessageSendingOperations) {

	// 말 이동하기
	@MessageMapping("/test")
	fun moveMal(request: TestRequest){

		println("클라이언트로부터 요청 받음! ${request.msg}")

		val url = StringBuilder("/topic/test/")
			.toString()
		sendingOperations.convertAndSend(url, TestResponse("요청 성공!"))
	}

	private class TestResponse{
		var msg : String

		constructor(msg:String){
			this.msg = msg
		}
	}

	public class TestRequest{
		var msg : String

		constructor(msg:String){
			this.msg = msg
		}
	}
}
