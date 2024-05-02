package github.ablandel.anotherkaomoji.config

import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ExceptionControllerAdvice : ResponseEntityExceptionHandler()
