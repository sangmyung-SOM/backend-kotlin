package com.smu.som.reportQnA

import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service

// 질문 기록 저장하는 서비스
@Service
@Slf4j
@RequiredArgsConstructor
@NoArgsConstructor
class ReportService(
	var qnaMap: HashMap<String, ArrayList<ReadReportDTO>> = LinkedHashMap<String, ArrayList<ReadReportDTO>>()
) {

	// 질문 및 답변 저장
	fun sendQnA(gameroomId: String, qna: ReadReportDTO): Boolean {
		if (qnaMap.get(gameroomId) == null) {
			qnaMap.put(gameroomId, ArrayList<ReadReportDTO>())
		}
		qnaMap.get(gameroomId)?.add(qna)
		return true
	}

	// 질문 및 답변 기록들 조회
	fun getQnA(gameroomId: String): List<ReadReportDTO> {
		if (findById(gameroomId) != null) {
			// 역 정렬
			return qnaMap.get(gameroomId)?.reversed()!!
		}
		return emptyList()
	}

	// 게임 방 ID로 질문 기록 조회
	private fun findById(gameroomId: String): ArrayList<ReadReportDTO>? {
		return qnaMap.get(gameroomId)
	}

}
