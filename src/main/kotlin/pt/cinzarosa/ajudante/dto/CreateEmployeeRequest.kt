package pt.cinzarosa.ajudante.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "Request to create a new employee")
data class CreateEmployeeRequest(

    @field:NotBlank
    @Schema(description = "Name of the employee", example = "Maria Silva")
    val name: String
)

