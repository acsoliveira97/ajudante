package pt.cinzarosa.ajudante.repository

import org.springframework.stereotype.Component
import pt.cinzarosa.ajudante.entity.CleaningShift
import pt.cinzarosa.ajudante.entity.House
import pt.cinzarosa.ajudante.exception.HouseAlreadyAssignedException

@Component
class CleaningShiftHouseRepositoryImpl(
    private val cleaningShiftHouseRepositoryJpa: CleaningShiftHouseRepositoryJpa,
    private val houseRepository: HouseRepository
) : CleaningShiftHouseRepository {

    override fun assertNoConflicts(shift: CleaningShift, houses: List<House>, excludeShiftId: Int?) {
        val houseIds = houses.map { it.id!! }.toSet()

        val conflicts = cleaningShiftHouseRepositoryJpa
            .findAllByHouseIdInAndCleaningDate(houseIds, shift.cleaningDate)
            .filter { excludeShiftId == null || it.shift?.id != excludeShiftId }

        if (conflicts.isEmpty()) return

        val conflictingHouseIds = conflicts.mapNotNull { it.house?.id }.toSet()
        val conflictingNames = houseRepository.findAllById(conflictingHouseIds)
            .map { it.shortName }
            .sorted()

        throw HouseAlreadyAssignedException(shift.cleaningDate, conflictingNames)
    }
}

