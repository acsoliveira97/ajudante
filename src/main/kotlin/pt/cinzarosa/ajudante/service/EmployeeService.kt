package pt.cinzarosa.ajudante.service

import org.springframework.stereotype.Service
import pt.cinzarosa.ajudante.dto.CreateEmployeeRequest
import pt.cinzarosa.ajudante.dto.EmployeeResponse
import pt.cinzarosa.ajudante.mapper.EmployeeMapper
import pt.cinzarosa.ajudante.repository.EmployeeRepository

@Service
class EmployeeService(
    private val employeeMapper: EmployeeMapper,
    private val employeeRepository: EmployeeRepository
) {

    fun fetchAll(): List<EmployeeResponse> {
        val domainEmployees = with(employeeMapper) { employeeRepository.findAll().toDomainList() }

        return with(employeeMapper) { domainEmployees.toEmployeeResponseList() }
    }

    fun create(request: CreateEmployeeRequest): EmployeeResponse {
        val entity = with(employeeMapper) { request.toEntity() }
        val saved = employeeRepository.save(entity)
        val domain = with(employeeMapper) { saved.toDomain() }

        return with(employeeMapper) { domain.toEmployeeResponse() }
    }
}