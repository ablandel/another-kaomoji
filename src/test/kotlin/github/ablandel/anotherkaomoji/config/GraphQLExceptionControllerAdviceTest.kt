package github.ablandel.anotherkaomoji.config

import github.ablandel.anotherkaomoji.exception.DataAlreadyExistException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.springframework.graphql.execution.ErrorType
import org.springframework.http.HttpStatus

class GraphQLExceptionControllerAdviceTest {

    @InjectMocks
    private lateinit var graphQLExceptionControllerAdvice: GraphQLExceptionControllerAdvice

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `toErrorType map the HttpStatusCode to the valid GraphQL classification error type`() {
        assertEquals(ErrorType.BAD_REQUEST, graphQLExceptionControllerAdvice.toErrorType(HttpStatus.BAD_REQUEST))
        assertEquals(ErrorType.BAD_REQUEST, graphQLExceptionControllerAdvice.toErrorType(HttpStatus.CONFLICT))
        assertEquals(ErrorType.UNAUTHORIZED, graphQLExceptionControllerAdvice.toErrorType(HttpStatus.UNAUTHORIZED))
        assertEquals(ErrorType.FORBIDDEN, graphQLExceptionControllerAdvice.toErrorType(HttpStatus.FORBIDDEN))
        assertEquals(ErrorType.NOT_FOUND, graphQLExceptionControllerAdvice.toErrorType(HttpStatus.NOT_FOUND))
        assertEquals(
            ErrorType.INTERNAL_ERROR,
            graphQLExceptionControllerAdvice.toErrorType(HttpStatus.INTERNAL_SERVER_ERROR)
        )
        assertEquals(ErrorType.INTERNAL_ERROR, graphQLExceptionControllerAdvice.toErrorType(HttpStatus.I_AM_A_TEAPOT))
    }

    @Test
    fun `handle extracts the data from the ResponseStatusException exception and creates a GraphQLError`() {
        val exception = DataAlreadyExistException(parameter = "parameter", value = "value")
        val error = graphQLExceptionControllerAdvice.handle(exception)
        assertEquals(ErrorType.BAD_REQUEST, error.errorType)
    }

    @Test
    fun `handle creates a default GraphQLError if exception is not handled`() {
        val exception = Exception()
        val error = graphQLExceptionControllerAdvice.handle(exception)
        assertEquals(ErrorType.INTERNAL_ERROR, error.errorType)
    }
}