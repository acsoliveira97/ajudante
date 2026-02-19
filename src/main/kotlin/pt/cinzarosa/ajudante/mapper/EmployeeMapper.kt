package pt.cinzarosa.ajudante.mapper

import org.springframework.stereotype.Component
import pt.cinzarosa.ajudante.dto.EmployeeResponse
import pt.cinzarosa.ajudante.model.Employee

@Component
class EmployeeMapper{

    fun List<Employee>.toEmployeeResponseList(): List<EmployeeResponse> =
        map { it.toEmployeeResponse() }

    fun List<pt.cinzarosa.ajudante.entity.Employee>.toDomainList(): List<Employee> =
        map { it.toDomain() }

    fun Employee.toEmployeeResponse(): EmployeeResponse =
        EmployeeResponse(
            id = id,
            name = name
        )

    fun pt.cinzarosa.ajudante.entity.Employee.toDomain(): Employee =
        Employee(
            id = requireNotNull(id) { "Employee id is null" },
            name = name
        )
}