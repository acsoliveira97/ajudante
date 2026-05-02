package pt.cinzarosa.ajudante.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

@Schema(description = "Request to create a new house")
data class CreateHouseRequest(

    @field:NotBlank
    @Schema(description = "Full name / address of the house", example = "Rua das Flores 12, 2º Dto")
    val name: String,

    @field:NotBlank
    @Schema(description = "Short identifier for the house", example = "AD11")
    val shortName: String,

    @field:NotNull
    @field:Positive
    @Schema(description = "ID of the client who owns this house", example = "1")
    val clientId: Int,

    @field:NotNull
    @field:Positive
    @Schema(description = "Price per cleaning session", example = "45.00")
    val pricePerClean: BigDecimal
)

