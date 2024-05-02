package github.ablandel.anotherkaomoji.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class InvalidParameterException(message: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, message)