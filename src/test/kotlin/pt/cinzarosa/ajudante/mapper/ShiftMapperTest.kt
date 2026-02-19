package pt.cinzarosa.ajudante.mapper

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.instancio.Instancio
import org.instancio.Select
import org.junit.jupiter.api.Test
import pt.cinzarosa.ajudante.dto.CreateShiftRequest
import pt.cinzarosa.ajudante.entity.CleaningShift
import pt.cinzarosa.ajudante.entity.Client
import pt.cinzarosa.ajudante.entity.Employee
import pt.cinzarosa.ajudante.entity.House
import pt.cinzarosa.ajudante.model.Shift
import java.math.BigDecimal

class ShiftMapperTest {

    private val mapper = ShiftMapper()

    @Test
    fun `Map from cleaning shift entity to domain`() {
        val cleaningShift = Instancio.create(CleaningShift::class.java)

        val domain = with(mapper) { cleaningShift.toDomain() }

        assertThat(domain.id)
            .isEqualTo(cleaningShift.id)

        assertThat(domain.date)
            .isEqualTo(cleaningShift.cleaningDate)

        val expectedEmployeeIds: Set<Int> = cleaningShift.teamMembers
            .mapNotNull { it.employee?.id }     // employee can be null
            .toSet()

        assertThat(domain.employeeIds)
            .usingRecursiveComparison()
            .isEqualTo(expectedEmployeeIds)


        val expectedHouseIds: Set<Int> = cleaningShift.houses
            .mapNotNull { it.house?.id }
            .toSet()

        assertThat(domain.houseIds)
            .usingRecursiveComparison()
            .isEqualTo(expectedHouseIds)
    }

    @Test
    fun `Map from domain shift to cleaning shift entity`() {
        val shift = Instancio.create(Shift::class.java)

        // Build "loaded entities" from whatever IDs Instancio generated
        val employees = shift.employeeIds
            .map { Employee(id = it, name = "E-${it}") }

        val houses = shift.houseIds
            .map { House(id = it, name = "H-${it}", shortName = "H${it}", Instancio.create(Client::class.java),
                BigDecimal("31.00")
            ) }

        val entity = with(mapper) { shift.toEntity(employees, houses) }

        assertThat(entity.id).isEqualTo(shift.id)
        assertThat(entity.cleaningDate).isEqualTo(shift.date)

        val mappedEmployeeIds = entity.teamMembers
            .mapNotNull { it.employee?.id }
            .toSet()

        assertThat(mappedEmployeeIds)
            .usingRecursiveComparison()
            .isEqualTo(shift.employeeIds.map { it }.toSet())

        val mappedHouseIds = entity.houses
            .mapNotNull { it.house?.id }
            .toSet()

        assertThat(mappedHouseIds)
            .usingRecursiveComparison()
            .isEqualTo(shift.houseIds.map { it }.toSet())
    }

    @Test
    fun `Map from create cleaning shift request to domain shift entity`() {
        val createShiftRequest = Instancio.create(CreateShiftRequest::class.java)

        val domain = with(mapper) { createShiftRequest.toDomain() }

        assertThat(domain.id).isNull()
        assertThat(domain.date).isEqualTo(createShiftRequest.date)

        assertThat(domain.employeeIds)
            .usingRecursiveComparison()
            .isEqualTo(createShiftRequest.employeeIds)

        assertThat(domain.houseIds)
            .usingRecursiveComparison()
            .isEqualTo(createShiftRequest.houseIds)
    }

    @Test
    fun `Map from shift domain entity to create cleaning shift response`() {
        val shift = Instancio.create(Shift::class.java)

        val response = with(mapper) { shift.toCreateShiftResponse() }

        assertThat(response.shiftId).isEqualTo(shift.id!!)
    }

    @Test
    fun `Map from shift domain to create shift response throws exception when id is null`() {
        val shift = Instancio.of(Shift::class.java)
            .set(Select.field(Shift::class.java, "id"), null)
            .create()

        assertThatThrownBy { with(mapper) { shift.toCreateShiftResponse() } }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

}