package pt.cinzarosa.ajudante.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pt.cinzarosa.ajudante.dto.CreateShiftRequest
import pt.cinzarosa.ajudante.dto.ShiftIdResponse
import pt.cinzarosa.ajudante.dto.ShiftDetailResponse
import pt.cinzarosa.ajudante.dto.ShiftViewResponse
import pt.cinzarosa.ajudante.dto.UpdateShiftRequest
import pt.cinzarosa.ajudante.service.CleaningShiftService
import java.time.LocalDate

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
    ): ShiftIdResponse {
        return cleaningShiftService.createShift(request)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a cleaning shift by ID")
    fun getShift(@PathVariable id: Int): ShiftDetailResponse {
        return cleaningShiftService.findById(id)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing cleaning shift")
    fun updateShift(
        @PathVariable id: Int,
        @Valid @RequestBody request: UpdateShiftRequest
    ): ShiftIdResponse {
        return cleaningShiftService.updateShift(id, request)
    }

    @GetMapping
    fun findShifts(
        @RequestParam date: LocalDate,
        @RequestParam(required = false) houseId: Int?,
        @RequestParam(required = false) employeeIds: String?
    ): List<ShiftViewResponse> {
        val employees = employeeIds
            ?.split(",")
            ?.mapNotNull { it.trim().toIntOrNull() }
            ?.toSet()
            ?: emptySet()

        return cleaningShiftService.findBy(date, houseId, employees)
    }
}