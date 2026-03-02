package pt.cinzarosa.ajudante.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(HouseAlreadyAssignedException::class)
    fun handleHouseAlreadyAssigned(ex: HouseAlreadyAssignedException): ResponseEntity<ApiError> {
        val houses = ex.houses.joinToString(", ")
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ApiError("Conflito: $houses já têm limpeza marcada para ${ex.date}."))
    }
}