package pt.cinzarosa.ajudante.service

import org.springframework.stereotype.Service
import pt.cinzarosa.ajudante.dto.EmployeeResponse
import pt.cinzarosa.ajudante.mapper.EmployeeMapper
import pt.cinzarosa.ajudante.repository.EmployeeRepository

@Service
class EmployeeService(
    private val employeeMapper: EmployeeMapper,
    private val employeeRepository: EmployeeRepository
) {

    fun fetchAll(): List<EmployeeResponse> {
        val domainHouses = with(employeeMapper) { employeeRepository.findAll().toDomainList() }

        return with(employeeMapper) { domainHouses.toEmployeeResponseList() }
    }
}