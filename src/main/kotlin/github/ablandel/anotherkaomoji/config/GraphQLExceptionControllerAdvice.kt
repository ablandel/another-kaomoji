package github.ablandel.anotherkaomoji.config

import graphql.GraphQLError
import org.dataloader.annotations.VisibleForTesting
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler
import org.springframework.graphql.execution.ErrorType
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.server.ResponseStatusException

@ControllerAdvice
class GraphQLExceptionControllerAdvice {
    companion object {
        const val ERROR_NOT_HANDLED = "An unhandled server exception has been thrown."
    }

    @VisibleForTesting
    fun toErrorType(statusCode: HttpStatusCode): ErrorType {
        return when (statusCode) {
            HttpStatus.BAD_REQUEST -> ErrorType.BAD_REQUEST
            // There is no CONFLICT in `ErrorType`, to avoid an ugly `INTERNAL_ERROR` in
            // response the `BAD_REQUEST` is preferred. To be seen in the future if a custom
            // implementation should be added.
            HttpStatus.CONFLICT -> ErrorType.BAD_REQUEST
            HttpStatus.UNAUTHORIZED -> ErrorType.UNAUTHORIZED
            HttpStatus.FORBIDDEN -> ErrorType.FORBIDDEN
            HttpStatus.NOT_FOUND -> ErrorType.NOT_FOUND
            else -> ErrorType.INTERNAL_ERROR
        }
    }

    @GraphQlExceptionHandler
    fun handle(ex: Exception): GraphQLError {
        if (ex is ResponseStatusException) {
            return GraphQLError.newError().errorType(toErrorType(ex.statusCode)).message(ex.reason)
                .build()
        }
        return GraphQLError.newError().errorType(ErrorType.INTERNAL_ERROR).message(ERROR_NOT_HANDLED).build()
    }
}