package pt.cinzarosa.ajudante.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import pt.cinzarosa.ajudante.dto.*
import pt.cinzarosa.ajudante.mapper.ShiftMapper
import pt.cinzarosa.ajudante.repository.CleaningShiftRepository
import pt.cinzarosa.ajudante.repository.EmployeeRepository
import pt.cinzarosa.ajudante.repository.HouseRepository
import pt.cinzarosa.ajudante.validation.CreateShiftValidator
import java.time.LocalDate

@Service
class CleaningShiftService(
    private val createShiftValidator: CreateShiftValidator,
    private val shiftMapper: ShiftMapper,
    private val employeeRepository: EmployeeRepository,
    private val houseRepository: HouseRepository,
    private val cleaningShiftRepository: CleaningShiftRepository
) {

    @Transactional
    fun createShift(request: CreateShiftRequest): ShiftIdResponse {
        createShiftValidator.validate(request)

        val employees = employeeRepository
            .findAllById(request.employeeIds)
            .toSet()

        val houses = houseRepository
            .findAllById(request.houseIds)
            .toSet()

        val shift = with(shiftMapper) { request.toDomain(employees, houses) }

        val entity = with(shiftMapper) { shift.toEntity(employees.toList(), houses.toList()) }
        cleaningShiftRepository.assertCanCreate(entity)
        val saved = cleaningShiftRepository.save(entity)

        return ShiftIdResponse(shiftId = requireNotNull(saved.id))
    }

    fun findBy(date: LocalDate, houseId: Int?, employeeIds: Set<Int>): List<ShiftViewResponse> {
        val findByDateHouseAndExactTeam =
            cleaningShiftRepository.findByDateHouseAndExactTeam(date, houseId, employeeIds, employeeIds.size)

        val shift = with(shiftMapper) { findByDateHouseAndExactTeam.toDomain() }

        return with(shiftMapper) { shift.toShiftViewResponseList() }
    }

    fun findById(id: Int): ShiftDetailResponse {
        val shift = cleaningShiftRepository.findById(id)
            .orElseThrow { NoSuchElementException("Shift with id $id not found") }

        return with(shiftMapper) { shift.toShiftDetailResponse() }
    }

    @Transactional
    fun updateShift(id: Int, request: UpdateShiftRequest): ShiftIdResponse {
        val shift = cleaningShiftRepository.findById(id)
            .orElseThrow { NoSuchElementException("Shift with id $id not found") }

        val employees = employeeRepository.findAllById(request.employeeIds).toList()
        val houses = houseRepository.findAllById(request.houseIds).toList()

        cleaningShiftRepository.updateShift(shift, employees, houses)

        return ShiftIdResponse(shiftId = requireNotNull(shift.id))
    }
}