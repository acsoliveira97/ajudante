package pt.cinzarosa.ajudante.mapper

import org.springframework.stereotype.Component
import pt.cinzarosa.ajudante.dto.CreateShiftRequest
import pt.cinzarosa.ajudante.dto.CreateShiftResponse
import pt.cinzarosa.ajudante.dto.ShiftViewResponse
import pt.cinzarosa.ajudante.entity.*
import pt.cinzarosa.ajudante.model.Shift

@Component
class ShiftMapper(
    private val houseMapper: HouseMapper,
    private val employeeMapper: EmployeeMapper
) {

    fun List<CleaningShift>.toDomain(): List<Shift> = this.map { it.toDomain() }

    fun List<Shift>.toShiftViewResponseList(): List<ShiftViewResponse> = this.map { it.toShiftViewResponse() }

    fun CleaningShift.toDomain(): Shift =
        Shift(
            id = this.id,
            date = this.cleaningDate,
            employeeSet = this.teamMembers
                .mapNotNull {  with(employeeMapper) { it.employee?.toDomain() } }     // handle nullable employee
                .toSet(),
            houseSet = this.houses
                .mapNotNull {  with(houseMapper) { it.house?.toDomain() } }        // handle nullable house
                .toSet()
        )

    fun Shift.toEntity(
        employees: List<Employee>,
        houses: List<House>
    ): CleaningShift {

        val shift = CleaningShift(
            id = this.id,
            cleaningDate = this.date
        )

        shift.teamMembers = employees
            .map { emp ->
                CleaningShiftEmployee(
                    shift = shift,
                    employee = emp
                )
            }
            .toMutableList()

        shift.houses = houses
            .map { house ->
                CleaningShiftHouse(
                    shift = shift,
                    house = house,
                    cleaningDate = shift.cleaningDate // if your table has this column
                )
            }
            .toMutableList()

        return shift
    }

    /**
     * Request DTO -> Domain
     */
    fun CreateShiftRequest.toDomain(
        employees: Set<Employee>,
        houses: Set<House>): Shift =
        Shift(
            id = null,
            date = this.date,
            employeeSet = employees
                .map {  with(employeeMapper) { it.toDomain() } }
                .toSet(),
            houseSet = houses
                .map {  with(houseMapper) { it.toDomain() } }
                .toSet()
        )

    /**
     * Domain -> Response DTO
     */
    fun Shift.toCreateShiftResponse(): CreateShiftResponse =
        CreateShiftResponse(
            shiftId = requireNotNull(this.id)
        )

    fun Shift.toShiftViewResponse(): ShiftViewResponse =
        ShiftViewResponse(
            shiftId = requireNotNull(this.id),
            date = this.date,
            employees = this.employeeSet.map { it.name },
            houses = this.houseSet.map { it.shortName }
        )
}