package pt.cinzarosa.ajudante.repository

import org.springframework.stereotype.Component
import pt.cinzarosa.ajudante.entity.CleaningShift
import pt.cinzarosa.ajudante.entity.CleaningShiftEmployee
import pt.cinzarosa.ajudante.entity.CleaningShiftHouse
import pt.cinzarosa.ajudante.entity.Employee
import pt.cinzarosa.ajudante.entity.House
import java.time.LocalDate
import java.util.*

@Component
class CleaningShiftRepositoryImpl(
    private val cleaningShiftRepositoryJpa: CleaningShiftRepositoryJpa,
    private val cleaningShiftHouseRepository: CleaningShiftHouseRepository
) : CleaningShiftRepository {

    override fun findByDateHouseAndExactTeam(
        date: LocalDate,
        houseId: Int?,
        employeeIds: Set<Int>,
        teamSize: Int
    ): List<CleaningShift> {
        return cleaningShiftRepositoryJpa.findByDateHouseAndExactTeam(
            date = date,
            houseId = houseId,
            employeeIds = employeeIds,
            teamSize = teamSize
        )
    }

    override fun findById(cleaningShiftId: Int): Optional<CleaningShift> {
        return cleaningShiftRepositoryJpa.findById(cleaningShiftId)
    }

    override fun save(cleaningShift: CleaningShift) : CleaningShift {
        return cleaningShiftRepositoryJpa.save(cleaningShift)
    }

    override fun assertCanCreate(shift: CleaningShift) {
        cleaningShiftHouseRepository.assertNoConflicts(
            shift = shift,
            houses = shift.houses.mapNotNull { it.house },
            excludeShiftId = null
        )
    }

    override fun updateShift(shift: CleaningShift, employees: List<Employee>, houses: List<House>) {
        cleaningShiftHouseRepository.assertNoConflicts(
            shift = shift,
            houses = houses,
            excludeShiftId = shift.id
        )

        syncEmployees(shift, employees)
        syncHouses(shift, houses)
    }

    private fun syncEmployees(shift: CleaningShift, requestedEmployees: List<Employee>) {
        val requestedIds = requestedEmployees.map { it.id!! }.toSet()

        shift.teamMembers.removeIf { existing ->
            existing.employee?.id !in requestedIds
        }

        val existingIds = shift.teamMembers.mapNotNull { it.employee?.id }.toSet()

        requestedEmployees
            .filter { it.id!! !in existingIds }
            .forEach { emp ->
                shift.teamMembers.add(
                    CleaningShiftEmployee(shift = shift, employee = emp)
                )
            }
    }

    private fun syncHouses(shift: CleaningShift, requestedHouses: List<House>) {
        val requestedIds = requestedHouses.map { it.id!! }.toSet()

        shift.houses.removeIf { existing ->
            existing.house?.id !in requestedIds
        }

        val existingIds = shift.houses.mapNotNull { it.house?.id }.toSet()

        requestedHouses
            .filter { it.id!! !in existingIds }
            .forEach { house ->
                shift.houses.add(
                    CleaningShiftHouse(shift = shift, house = house, cleaningDate = shift.cleaningDate)
                )
            }
    }
}
