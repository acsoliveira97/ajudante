package pt.cinzarosa.ajudante.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import pt.cinzarosa.ajudante.dto.CreateShiftRequest
import pt.cinzarosa.ajudante.dto.CreateShiftResponse
import pt.cinzarosa.ajudante.dto.ShiftViewResponse
import pt.cinzarosa.ajudante.exception.HouseAlreadyAssignedException
import pt.cinzarosa.ajudante.mapper.ShiftMapper
import pt.cinzarosa.ajudante.repository.CleaningShiftHouseRepository
import pt.cinzarosa.ajudante.repository.CleaningShiftRepository
import pt.cinzarosa.ajudante.repository.EmployeeRepository
import pt.cinzarosa.ajudante.repository.HouseRepository
import java.time.LocalDate

@Service
class CleaningShiftService(
    private val cleaningShiftRepository : CleaningShiftRepository,
    private val cleaningShiftHouseRepository: CleaningShiftHouseRepository,
    private val shiftMapper: ShiftMapper,
    private val employeeRepository: EmployeeRepository,
    private val houseRepository: HouseRepository
) {

    @Transactional
    fun createShift(request: CreateShiftRequest): CreateShiftResponse {

        val conflicts = cleaningShiftHouseRepository
            .findAllByHouseIdInAndCleaningDate(request.houseIds, request.date)

        if (conflicts.isNotEmpty()) {
            val conflictingHouseIds = conflicts.map { it.house!!.id!! }.toSet()
            val conflictingNames = houseRepository.findAllById(conflictingHouseIds)
                .map { it.shortName }
                .sorted()

            throw HouseAlreadyAssignedException(request.date, conflictingNames)
        }


        val employees = employeeRepository
            .findAllById(request.employeeIds)
            .toSet()

        val houses = houseRepository
            .findAllById(request.houseIds)
            .toSet()

        val shift = with(shiftMapper) { request.toDomain(employees, houses) }

        val entity = with(shiftMapper) { shift.toEntity(employees.toList(), houses.toList()) }
        val saved = cleaningShiftRepository.save(entity)

        return CreateShiftResponse(shiftId = requireNotNull(saved.id))
    }

    fun findBy(date: LocalDate, houseId: Int?, employeeIds: Set<Int>): List<ShiftViewResponse> {
        val findByDateHouseAndExactTeam =
            cleaningShiftRepository.findByDateHouseAndExactTeam(date, houseId, employeeIds, employeeIds.size)

        val shift = with(shiftMapper) { findByDateHouseAndExactTeam.toDomain() }

        return with(shiftMapper) { shift.toShiftViewResponseList() }
    }
}