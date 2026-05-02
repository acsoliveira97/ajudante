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

class ShiftMapperTest {

    private val employeeMapper = EmployeeMapper()
    private val clientMapper = ClientMapper()
    private val houseMapper = HouseMapper(clientMapper)
    private val mapper = ShiftMapper(houseMapper, employeeMapper)

    @Test
    fun `Map from cleaning shift entity to domain`() {
        val cleaningShift = Instancio.create(CleaningShift::class.java)

        val domain = with(mapper) { cleaningShift.toDomain() }

        assertThat(domain.id)
            .isEqualTo(cleaningShift.id)

        assertThat(domain.date)
            .isEqualTo(cleaningShift.cleaningDate)

        val expectedEmployeeIds: Set<Int?> = cleaningShift.teamMembers
            .mapNotNull { it.employee }
            .map { it.id }
            .toSet()

        assertThat(domain.employeeSet.map { it.id }.toSet())
            .usingRecursiveComparison()
            .isEqualTo(expectedEmployeeIds)

        val expectedHouseIds: Set<Int?> = cleaningShift.houses
            .mapNotNull { it.house }
            .map { it.id }
            .toSet()

        assertThat(domain.houseSet.map { it.id }.toSet())
            .usingRecursiveComparison()
            .isEqualTo(expectedHouseIds)
    }

    @Test
    fun `Map from domain shift to cleaning shift entity`() {
        val shift = Instancio.create(Shift::class.java)

        val employees = shift.employeeSet
            .map { Employee(id = it.id, name = it.name) }

        val houses = shift.houseSet
            .map {
                House(
                    id = it.id,
                    name = it.name,
                    shortName = it.shortName,
                    client = Instancio.create(Client::class.java),
                    pricePerClean = it.pricePerClean
                )
            }

        val entity = with(mapper) { shift.toEntity(employees, houses) }

        assertThat(entity.id).isEqualTo(shift.id)
        assertThat(entity.cleaningDate).isEqualTo(shift.date)

        val mappedEmployeeIds = entity.teamMembers
            .mapNotNull { it.employee?.id }
            .toSet()

        assertThat(mappedEmployeeIds)
            .usingRecursiveComparison()
            .isEqualTo(shift.employeeSet.map { it.id }.toSet())

        val mappedHouseIds = entity.houses
            .mapNotNull { it.house?.id }
            .toSet()

        assertThat(mappedHouseIds)
            .usingRecursiveComparison()
            .isEqualTo(shift.houseSet.map { it.id }.toSet())
    }

    @Test
    fun `Map from create cleaning shift request to domain shift entity`() {
        val createShiftRequest = Instancio.create(CreateShiftRequest::class.java)

        val employees = createShiftRequest.employeeIds
            .map { Instancio.of(Employee::class.java).set(Select.field("id"), it).create() }
            .toSet()

        val houses = createShiftRequest.houseIds
            .map { Instancio.of(House::class.java).set(Select.field("id"), it).create() }
            .toSet()

        val domain = with(mapper) { createShiftRequest.toDomain(employees, houses) }

        assertThat(domain.id).isNull()
        assertThat(domain.date).isEqualTo(createShiftRequest.date)

        assertThat(domain.employeeSet.map { it.id }.toSet())
            .usingRecursiveComparison()
            .isEqualTo(createShiftRequest.employeeIds)

        assertThat(domain.houseSet.map { it.id }.toSet())
            .usingRecursiveComparison()
            .isEqualTo(createShiftRequest.houseIds)
    }

    @Test
    fun `Map from shift domain entity to create cleaning shift response`() {
        val shift = Instancio.create(Shift::class.java)

        val response = with(mapper) { shift.toShiftIdResponse() }

        assertThat(response.shiftId).isEqualTo(shift.id!!)
    }

    @Test
    fun `Map from shift domain to create shift response throws exception when id is null`() {
        val shift = Instancio.of(Shift::class.java)
            .set(Select.field(Shift::class.java, "id"), null)
            .create()

        assertThatThrownBy { with(mapper) { shift.toShiftIdResponse() } }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

}