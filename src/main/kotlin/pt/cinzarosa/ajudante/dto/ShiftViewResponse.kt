package pt.cinzarosa.ajudante.dto

import java.time.LocalDate

data class ShiftViewResponse(
    val shiftId: Int,
    val date: LocalDate,
    val employees: List<String>,
    val houses: List<String>
)