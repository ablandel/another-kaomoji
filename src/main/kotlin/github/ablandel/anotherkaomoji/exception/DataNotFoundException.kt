package github.ablandel.anotherkaomoji.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class DataNotFoundException(parameter: String, value: String) : ResponseStatusException(
    HttpStatus.NOT_FOUND,
    String.format("Resource with `%s` `%s` not found", parameter, value)
)