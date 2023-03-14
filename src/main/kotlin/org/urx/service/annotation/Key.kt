package org.urx.service.annotation

import org.urx.service.enums.DefaultValue

@Target
@Retention(AnnotationRetention.RUNTIME)
annotation class Key(
	val name: String,
	val autoCall: Boolean = false,
	val defaultNullValue: DefaultValue = DefaultValue.NULL
)