package pt.cinzarosa.ajudante.repository

import pt.cinzarosa.ajudante.entity.CleaningShift
import pt.cinzarosa.ajudante.entity.House

fun interface CleaningShiftHouseRepository {
    fun assertNoConflicts(shift: CleaningShift, houses: List<House>, excludeShiftId: Int?)
}