package org.urx.exception

import java.text.SimpleDateFormat
import java.util.Date
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Component
class HttpExceptionResponse {

	private val logger = LoggerFactory.getLogger(HttpExceptionResponse::class.java)


	fun error(e: ResponseStatusException): ResponseEntity<Any> {
		val response: MutableMap<String, Any?> = LinkedHashMap()
		response["timestamp"] = SimpleDateFormat(
			"MM-dd-yyyy  HH:mm:ss a"
		).format(Date())

		response["status"] = e.statusCode.value()
		response["error"] = e.statusCode
		response["message"] = e.reason

		try {
			val developMessage = e.javaClass.getMethod(
				"getDevelopMessage"
			).invoke(e)

			if (developMessage != null) {
				response["developMessage"] = developMessage
			}
		} catch (ex: Exception) {
			logger.info("No develop message send")
		}

		try {
			val developMessage = e.javaClass.getMethod(
				"getFieldNameError"
			).invoke(e)

			if (developMessage != null) {
				response["fieldNameError"] = developMessage
			}
		} catch (ex: Exception) {
			logger.info("No develop message send")
		}

		logger.error(e.message)
		return ResponseEntity(
			response,
			e.statusCode
		)
	}

}