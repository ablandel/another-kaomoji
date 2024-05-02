package github.ablandel.anotherkaomoji.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class DataAlreadyExistException : ResponseStatusException {
    companion object {
        private val HTTP_STATUS = HttpStatus.CONFLICT
    }

    constructor(parameter: String, value: String) : super(
        HTTP_STATUS,
        String.format("Resource with `%s` `%s` already exist", parameter, value)
    )

    constructor(message: String) : super(HTTP_STATUS, message)
}