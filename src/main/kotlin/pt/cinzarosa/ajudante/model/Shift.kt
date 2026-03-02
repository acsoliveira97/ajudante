package pt.cinzarosa.ajudante.model

import java.time.LocalDate

data class Shift(
    val id: Int? = null,
    val date: LocalDate,
    val employeeSet: Set<Employee>,
    val houseSet: Set<House>
)