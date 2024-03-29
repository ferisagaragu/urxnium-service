package org.urx.service

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageWriter

import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.UUID
import java.util.stream.Collectors
import javax.imageio.ImageIO

import jakarta.servlet.http.HttpServletResponse

import org.apache.commons.io.output.ByteArrayOutputStream
import org.springframework.core.io.FileSystemResource

import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.urx.service.helper.Injects
import org.urx.service.helper.ResponseList
import org.urx.service.helper.ResponseMap

import org.urx.service.refactor.ResponseRecycle

@Component
class Response {

	fun ok(): ResponseEntity<Any> {
		return ResponseRecycle.response(null,null, null, null, HttpStatus.OK)
	}

	fun ok(message: String?): ResponseEntity<Any> {
		return ResponseRecycle.response(message, null, null, null, HttpStatus.OK)
	}

	fun ok(data: Any?): ResponseEntity<Any> {
		return ResponseRecycle.response(null, data, null, null, HttpStatus.OK)
	}

	fun ok(message: String?, data: Any?): ResponseEntity<Any> {
		return ResponseRecycle.response(message, data, null, null, HttpStatus.OK)
	}

	fun ok(data: Any?, payload: Any?): ResponseEntity<Any> {
		return ResponseRecycle.response(null, data, null, payload, HttpStatus.OK)
	}

	fun ok(message: String?, data: Any?, payload: Any?): ResponseEntity<Any> {
		return ResponseRecycle.response(message, data, null, payload, HttpStatus.OK)
	}

	fun created(): ResponseEntity<Any> {
		return ResponseRecycle.response(null,null, null, null, HttpStatus.CREATED)
	}

	fun created(message: String?): ResponseEntity<Any> {
		return ResponseRecycle.response(message, null, null, null, HttpStatus.CREATED)
	}

	fun created(data: Any?): ResponseEntity<Any> {
		return ResponseRecycle.response(null, data, null, null, HttpStatus.CREATED)
	}

	fun created(message: String?, data: Any?): ResponseEntity<Any> {
		return ResponseRecycle.response(message, data, null, null, HttpStatus.CREATED)
	}

	fun created(data: Any?, payload: Any?): ResponseEntity<Any> {
		return ResponseRecycle.response(null, data, null, payload, HttpStatus.CREATED)
	}

	fun created(message: String?, data: Any?, payload: Any?): ResponseEntity<Any> {
		return ResponseRecycle.response(message, data, null, payload, HttpStatus.CREATED)
	}

	fun file(mediaType: MediaType, fileName: String, fileData: InputStreamResource): ResponseEntity<Any> {
		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(mediaType.type))
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${fileName}")
			.body(fileData)
	}

	fun file(mediaType: String, fileName: String, fileData: InputStreamResource): ResponseEntity<Any> {
		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(mediaType))
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${fileName}")
			.body(fileData)
	}

	fun file(mediaType: MediaType, fileName: String, fileData: InputStream): ResponseEntity<Any> {
		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(mediaType.type))
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${fileName}")
			.body(InputStreamResource(fileData))
	}

	fun file(mediaType: String, fileName: String, fileData: InputStream): ResponseEntity<Any> {
		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(mediaType))
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${fileName}")
			.body(InputStreamResource(fileData))
	}

	fun file(mediaType: String, fileName: String, fileData: ByteArrayInputStream): ResponseEntity<Any> {
		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(mediaType))
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${fileName}")
			.body(InputStreamResource(fileData))
	}

	fun partialContent(mediaType: String, filePath: String, fileName: String): ResponseEntity<Any> {
		val filePathString = "$filePath${fileName}"
		val  responseHeaders = HttpHeaders()
		responseHeaders.add("content-type", mediaType)
		return ResponseEntity(FileSystemResource(filePathString), responseHeaders, HttpStatus.OK)
	}

	fun qr(data: String): ResponseEntity<Any> {
		val matrix = MultiFormatWriter().encode(
			data,
			BarcodeFormat.QR_CODE,
			512,
			512
		)
		val os = ByteArrayOutputStream()
		ImageIO.write(MatrixToImageWriter.toBufferedImage(matrix), "png", os)
		val iss: InputStream = ByteArrayInputStream(os.toByteArray())

		return file("image/png", "${UUID.randomUUID()}.png", iss)
	}

	fun html(response: HttpServletResponse, content: String): String {
		response.contentType = "text/plain"
		response.characterEncoding = "UTF-8"

		return content
	}

	fun html(response: HttpServletResponse, inputStream: InputStream): String {
		val text: String = BufferedReader(
			InputStreamReader(
				inputStream,
				StandardCharsets.UTF_8
			)
		).lines().collect(Collectors.joining(""))

		response.contentType = "text/plain"
		response.characterEncoding = "UTF-8"

		return text
	}

	fun toMap(any: Any, vararg callMethods: String): ResponseMap {
		return toMap(any, true, Injects(), *callMethods)
	}

	fun toMap(any: Any, autoCallFunctions: Boolean, vararg callMethods: String): ResponseMap {
		return toMap(any, autoCallFunctions, Injects(), *callMethods)
	}

	fun toMap(any: Any, injects: Injects, vararg callMethods: String): ResponseMap {
		return toMap(any, true, injects, *callMethods)
	}

	fun toListMap(anyList: List<Any>, vararg callMethods: String): ResponseList {
		return toListMap(anyList, true, Injects(), *callMethods)
	}

	fun toListMap(anyList: List<Any>, autoCallFunctions: Boolean, vararg callMethods: String): ResponseList {
		return toListMap(anyList, autoCallFunctions, Injects(), *callMethods)
	}

	fun toListMap(anyList: List<Any>, injects: Injects, vararg callMethods: String): ResponseList {
		return toListMap(anyList, true, injects, *callMethods)
	}

	fun toMap(any: Any, autoCallFunctions: Boolean, injects: Injects, vararg callMethods: String): ResponseMap {
		val out = ResponseMap()

		for (field in any::class.java.declaredFields) {
			field.isAccessible = true
			val result: Any? = field.get(any)

			if (result != null) {
				if (ResponseRecycle.isValidType(result)) {
					out[field.name] = ResponseRecycle.convertValue(result)
				}
			} else {
				out[field.name] = null
			}
		}

		if (autoCallFunctions) {
			//Aquí se invocan que tiene auto call
			ResponseRecycle.autoCallMethods(any, out)
		}

		//Aquí se invocan los métodos llamados por el desarrollador
		ResponseRecycle.callMethods(any, out, *callMethods)

		//Aquí se remplaza el texto que se quiere injectar
		ResponseRecycle.injectText(out, injects)

		return out
	}

	fun toListMap(anyList: List<Any>, autoCallFunctions: Boolean, injects: Injects,  vararg callMethods: String): ResponseList {
		val out = ResponseList()

		for (any in anyList) {
			out.add(toMap(any, autoCallFunctions, injects, *callMethods))
		}

		return out
	}

}