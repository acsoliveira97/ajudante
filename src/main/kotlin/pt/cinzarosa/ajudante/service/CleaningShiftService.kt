package pt.cinzarosa.ajudante.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import pt.cinzarosa.ajudante.dto.CreateShiftRequest
import pt.cinzarosa.ajudante.dto.CreateShiftResponse
import pt.cinzarosa.ajudante.mapper.ShiftMapper
import pt.cinzarosa.ajudante.repository.CleaningShiftRepository
import pt.cinzarosa.ajudante.repository.EmployeeRepository
import pt.cinzarosa.ajudante.repository.HouseRepository

@Service
class CleaningShiftService(
    private val cleaningShiftRepository : CleaningShiftRepository,
    private val shiftMapper: ShiftMapper,
    private val employeeRepository: EmployeeRepository,
    private val houseRepository: HouseRepository
) {

    @Transactional
    fun createShift(request: CreateShiftRequest): CreateShiftResponse {
        val shift = with(shiftMapper) { request.toDomain() }

        val employees = employeeRepository.findAllById(shift.employeeIds).toList()
        val houses = houseRepository.findAllById(shift.houseIds).toList()

        val entity = with(shiftMapper) { shift.toEntity(employees, houses) }
        val saved = cleaningShiftRepository.save(entity)
        val savedShift = with(shiftMapper) { saved.toDomain() }

        return with(shiftMapper) { savedShift.toCreateShiftResponse() }
    }
}