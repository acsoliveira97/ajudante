package pt.cinzarosa.ajudante.validation

import org.springframework.stereotype.Component
import pt.cinzarosa.ajudante.dto.CreateShiftRequest

@Component
class CreateShiftValidator(
    private val rules: List<CreateShiftRule>
) {
    fun validate(request: CreateShiftRequest) {
        rules.forEach { it.validate(request) }
    }
}