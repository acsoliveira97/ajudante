package pt.cinzarosa.ajudante.validation

import org.springframework.stereotype.Component
import pt.cinzarosa.ajudante.dto.CreateShiftRequest
import pt.cinzarosa.ajudante.exception.HouseAlreadyAssignedException
import pt.cinzarosa.ajudante.repository.CleaningShiftHouseRepository
import pt.cinzarosa.ajudante.repository.HouseRepository

@Component
class HouseNotAlreadyAssignedRule(
    private val cleaningShiftHouseRepository: CleaningShiftHouseRepository,
    private val houseRepository: HouseRepository
) : CreateShiftRule {

    override fun validate(request: CreateShiftRequest) {
        val conflicts = cleaningShiftHouseRepository
            .findAllByHouseIdInAndCleaningDate(request.houseIds, request.date)

        if (conflicts.isEmpty()) return

        val conflictingHouseIds = conflicts.mapNotNull { it.house?.id }.toSet()

        val conflictingNames = houseRepository.findAllById(conflictingHouseIds)
            .map { it.shortName }
            .sorted()

        throw HouseAlreadyAssignedException(request.date, conflictingNames)
    }
}