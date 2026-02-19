package pt.cinzarosa.ajudante.model

import java.time.LocalDate

data class Shift(
    val id: Int? = null,
    val date: LocalDate,
    val employeeIds: Set<Int>,
    val houseIds: Set<Int>
)