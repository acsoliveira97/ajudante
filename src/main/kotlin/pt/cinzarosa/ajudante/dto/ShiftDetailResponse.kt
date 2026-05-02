package pt.cinzarosa.ajudante.dto

import java.time.LocalDate

data class ShiftDetailResponse(
    val shiftId: Int,
    val date: LocalDate,
    val employeeIds: Set<Int>,
    val houseIds: Set<Int>
)

