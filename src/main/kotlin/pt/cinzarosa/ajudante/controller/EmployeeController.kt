package pt.cinzarosa.ajudante.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pt.cinzarosa.ajudante.dto.CreateEmployeeRequest
import pt.cinzarosa.ajudante.dto.EmployeeResponse
import pt.cinzarosa.ajudante.service.EmployeeService

@RestController
@RequestMapping("/employees")
@Tag(name = "Employees", description = "Operations related to employees")
class EmployeeController(
    private val employeeService: EmployeeService
) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all employees")
    fun fetchAllEmployees(): List<EmployeeResponse> {
        return employeeService.fetchAll()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new employee")
    fun createEmployee(
        @Valid @RequestBody request: CreateEmployeeRequest
    ): EmployeeResponse {
        return employeeService.create(request)
    }
}