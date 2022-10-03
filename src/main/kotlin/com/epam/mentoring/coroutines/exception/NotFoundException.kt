package com.epam.mentoring.coroutines.exception

import org.springframework.http.HttpStatus

class NotFoundException(message: String = "entity not found"): ApiException(HttpStatus.NOT_FOUND, Error(message))