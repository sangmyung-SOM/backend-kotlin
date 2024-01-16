package com.smu.som.reportQnA

import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service

@Service
@Slf4j
@RequiredArgsConstructor
@NoArgsConstructor
class ReportService(
	var qnaMap: HashMap<String, ArrayList<ReadReportDTO>> = LinkedHashMap<String, ArrayList<ReadReportDTO>>()
) {

	fun sendQnA(gameroomId: String, qna: ReadReportDTO): Boolean {
		if (qnaMap.get(gameroomId) == null) {
			qnaMap.put(gameroomId, ArrayList<ReadReportDTO>())
		}
		qnaMap.get(gameroomId)?.add(qna)
		return true
	}

	fun getQnA(gameroomId: String): List<ReadReportDTO> {
		if (findById(gameroomId) != null) {
			// 역 정렬
			return qnaMap.get(gameroomId)?.reversed()!!
		}
		return emptyList()
	}

	private fun findById(gameroomId: String): ArrayList<ReadReportDTO>? {
		return qnaMap.get(gameroomId)
	}

}
