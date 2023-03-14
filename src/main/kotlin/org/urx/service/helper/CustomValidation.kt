package org.urx.service.helper

interface CustomValidation {
	fun validate(value: Any): Boolean
	fun getStatusCode(): Int = 400
}