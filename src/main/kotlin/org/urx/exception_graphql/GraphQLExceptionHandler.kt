package org.urx.exception_graphql

import graphql.ErrorType
import graphql.GraphQLError
import graphql.GraphQLException
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.stereotype.Component

@Component
class GraphQLExceptionHandler : DataFetcherExceptionResolverAdapter() {

	override fun resolveToSingleError(ex: Throwable, env: DataFetchingEnvironment): GraphQLError? {
		return if (ex is GraphQLException) {
			GraphqlErrorBuilder.newError()
				.message(ex.message)
				.errorType(ErrorType.ValidationError)
				.path(env.executionStepInfo.path)
				.location(env.field.sourceLocation)
				.build()
		} else {
			null
		}
	}

}