package pt.cinzarosa.ajudante.validation

import pt.cinzarosa.ajudante.dto.CreateShiftRequest

fun interface CreateShiftRule {
    fun validate(request: CreateShiftRequest)
}