package pt.cinzarosa.ajudante.mapper

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.instancio.Instancio
import org.instancio.Select
import org.junit.jupiter.api.Test
import pt.cinzarosa.ajudante.model.Employee
import pt.cinzarosa.ajudante.entity.Employee as EmployeeEntity

class EmployeeMapperTest {

    private val mapper = EmployeeMapper()

    @Test
    fun `Map from employee entity to domain`() {
        val employeeEntity = Instancio.create(EmployeeEntity::class.java)

        val domain = with(mapper) { employeeEntity.toDomain() }

        assertThat(domain.id).isEqualTo(employeeEntity.id)

        assertThat(domain.name).isEqualTo(employeeEntity.name)
    }

    @Test
    fun `Map from employee entity to domain throws exception when id is null`() {
        val employeeEntity = Instancio.of(EmployeeEntity::class.java)
            .set(Select.field(EmployeeEntity::class.java, "id"), null)
            .create()

        assertThatThrownBy { with(mapper) { employeeEntity.toDomain() } }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Employee id is null")
    }

    @Test
    fun `Map from employee entity list to domain list`() {
        val employeeEntities = listOf(
            Instancio.create(EmployeeEntity::class.java),
            Instancio.create(EmployeeEntity::class.java),
            Instancio.create(EmployeeEntity::class.java)
        )

        val domainList = with(mapper) { employeeEntities.toDomainList() }

        assertThat(domainList)
            .hasSize(employeeEntities.size)

        domainList.forEachIndexed { index, domain ->
            assertThat(domain.id).isEqualTo(employeeEntities[index].id)
            assertThat(domain.name).isEqualTo(employeeEntities[index].name)
        }
    }

    @Test
    fun `Map from employee domain to response`() {
        val employee = Instancio.create(Employee::class.java)

        val response = with(mapper) { employee.toEmployeeResponse() }

        assertThat(response.id).isEqualTo(employee.id)
        assertThat(response.name).isEqualTo(employee.name)
    }

    @Test
    fun `Map from employee domain list to response list`() {
        val employees = listOf(
            Instancio.create(Employee::class.java),
            Instancio.create(Employee::class.java),
            Instancio.create(Employee::class.java)
        )

        val responseList = with(mapper) { employees.toEmployeeResponseList() }

        assertThat(responseList).hasSize(employees.size)
        responseList.forEachIndexed { index, response ->
            assertThat(response.id).isEqualTo(employees[index].id)
            assertThat(response.name).isEqualTo(employees[index].name)
        }
    }
}

