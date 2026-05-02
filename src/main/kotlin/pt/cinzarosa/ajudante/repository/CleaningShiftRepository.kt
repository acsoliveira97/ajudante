package pt.cinzarosa.ajudante.repository

import pt.cinzarosa.ajudante.entity.CleaningShift
import pt.cinzarosa.ajudante.entity.Employee
import pt.cinzarosa.ajudante.entity.House
import java.time.LocalDate
import java.util.*

interface CleaningShiftRepository {

    fun findByDateHouseAndExactTeam(date: LocalDate, houseId: Int?, employeeIds: Set<Int>, teamSize: Int): List<CleaningShift>

    fun findById(cleaningShiftId: Int): Optional<CleaningShift>

    fun save(cleaningShift: CleaningShift): CleaningShift

    /**
     * Asserts no house conflicts exist for the given shift before creation.
     * Must be called before save().
     */
    fun assertCanCreate(shift: CleaningShift)

    /**
     * Updates a managed CleaningShift by diffing team members and houses.
     * Enforces house-per-day uniqueness (excluding the shift being updated).
     * The managed entity is mutated in place — dirty checking persists changes.
     */
    fun updateShift(shift: CleaningShift, employees: List<Employee>, houses: List<House>)
}


