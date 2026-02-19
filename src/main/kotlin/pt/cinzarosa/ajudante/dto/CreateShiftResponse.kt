package pt.cinzarosa.ajudante.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Response after creating a cleaning shift")
data class CreateShiftResponse(

    @Schema(description = "Generated shift ID", example = "42")
    val shiftId: Int
)