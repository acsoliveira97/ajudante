package pt.cinzarosa.ajudante.mapper

import org.springframework.stereotype.Component
import pt.cinzarosa.ajudante.dto.CreateShiftRequest
import pt.cinzarosa.ajudante.dto.CreateShiftResponse
import pt.cinzarosa.ajudante.entity.*
import pt.cinzarosa.ajudante.model.Shift

@Component
class ShiftMapper {

    fun CleaningShift.toDomain(): Shift =
        Shift(
            id = this.id,
            date = this.cleaningDate,
            employeeIds = this.teamMembers
                .mapNotNull { it.employee?.id }     // handle nullable employee
                .toSet(),
            houseIds = this.houses
                .mapNotNull { it.house?.id }        // handle nullable house
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
            .toMutableSet()

        shift.houses = houses
            .map { house ->
                CleaningShiftHouse(
                    shift = shift,
                    house = house,
                    cleaningDate = shift.cleaningDate // if your table has this column
                )
            }
            .toMutableSet()

        return shift
    }

    /**
     * Request DTO -> Domain
     */
    fun CreateShiftRequest.toDomain(): Shift =
        Shift(
            id = null,
            date = this.date,
            employeeIds = this.employeeIds,
            houseIds = this.houseIds
        )

    /**
     * Domain -> Response DTO
     */
    fun Shift.toCreateShiftResponse(): CreateShiftResponse =
        CreateShiftResponse(
            shiftId = requireNotNull(this.id)
        )
}