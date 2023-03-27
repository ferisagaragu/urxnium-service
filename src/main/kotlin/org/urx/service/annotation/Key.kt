package org.urx.service.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Target
import org.urx.service.enums.DefaultValue

@Target(ElementType.METHOD, ElementType.TYPE)
@Retention(AnnotationRetention.RUNTIME)
annotation class Key(
	val name: String,
	val autoCall: Boolean = false,
	val defaultNullValue: DefaultValue = DefaultValue.NULL
)