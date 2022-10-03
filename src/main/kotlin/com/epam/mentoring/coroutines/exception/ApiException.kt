package com.epam.mentoring.coroutines.exception

import org.springframework.http.HttpStatus

open class ApiException(val httpStatus: HttpStatus, val error: Error): RuntimeException(error.message)