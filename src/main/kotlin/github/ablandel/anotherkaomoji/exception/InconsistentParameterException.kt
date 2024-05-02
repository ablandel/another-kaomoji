package github.ablandel.anotherkaomoji.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class InconsistentParameterException(parameter: String) :
    ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        String.format("The `%s` parameters are inconsistent", parameter)
    )