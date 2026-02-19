package pt.cinzarosa.ajudante.dto

import java.time.LocalDate
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

@Schema(description = "Request to create a new cleaning shift")
data class CreateShiftRequest(

    @field:NotNull
    @Schema(example = "2026-02-18")
    val date: LocalDate,

    @field:NotEmpty
    @Schema(description = "IDs of employees that worked on the shift", example = "[1,2]")
    val employeeIds: Set<Int>,

    @field:NotEmpty
    @Schema(description = "IDs of houses cleaned during the shift", example = "[5,6,8]")
    val houseIds: Set<Int>
)