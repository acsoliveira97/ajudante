package pt.cinzarosa.ajudante.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty

@Schema(description = "Request to update an existing cleaning shift")
data class UpdateShiftRequest(

    @field:NotEmpty
    @Schema(description = "IDs of employees that worked on the shift", example = "[1,2]")
    val employeeIds: Set<Int>,

    @field:NotEmpty
    @Schema(description = "IDs of houses cleaned during the shift", example = "[5,6,8]")
    val houseIds: Set<Int>
)
