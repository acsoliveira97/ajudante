package pt.cinzarosa.ajudante.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Response containing a shift ID")
data class ShiftIdResponse(

    @Schema(description = "The shift ID", example = "42")
    val shiftId: Int
)