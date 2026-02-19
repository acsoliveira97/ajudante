package pt.cinzarosa.it

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.containers.PostgreSQLContainer
import pt.cinzarosa.ajudante.AjudanteApplication
import pt.cinzarosa.ajudante.dto.CreateShiftRequest
import pt.cinzarosa.ajudante.repository.CleaningShiftRepository
import pt.cinzarosa.ajudante.service.CleaningShiftService
import java.time.LocalDate
import kotlin.jvm.optionals.getOrNull

@SpringBootTest(classes = [AjudanteApplication::class])
@ActiveProfiles("it")
@Testcontainers
class CleaningShiftServiceIT {

    companion object {
        @Container
        @ServiceConnection
        val postgres = PostgreSQLContainer("postgres:18-alpine")
            .withDatabaseName("ajudante")
            .withUsername("test")
            .withPassword("test")
    }

    @Autowired lateinit var cleaningShiftService: CleaningShiftService
    @Autowired lateinit var cleaningShiftRepository: CleaningShiftRepository

    @Test
    fun `creates a shift with employees and houses`() {
        val request = CreateShiftRequest(
            date = LocalDate.of(2026, 2, 18),
            employeeIds = setOf(1, 2),
            houseIds = setOf(1, 2, 3, 4)
        )

        val result = cleaningShiftService.createShift(request)

        val shiftOpt = cleaningShiftRepository.findById(result.shiftId).getOrNull()

        assertThat(shiftOpt).isNotNull
    }
}