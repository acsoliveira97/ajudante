package pt.cinzarosa.ajudante.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pt.cinzarosa.ajudante.dto.CreateShiftRequest
import pt.cinzarosa.ajudante.dto.CreateShiftResponse
import pt.cinzarosa.ajudante.service.CleaningShiftService

@RestController
@RequestMapping("/shifts")
@Tag(name = "Cleaning Shifts", description = "Operations related to cleaning shifts")
class CleaningShiftController(
    private val cleaningShiftService: CleaningShiftService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new cleaning shift")
    fun createShift(
        @Valid @RequestBody request: CreateShiftRequest
    ): CreateShiftResponse {
        return cleaningShiftService.createShift(request)
    }
}