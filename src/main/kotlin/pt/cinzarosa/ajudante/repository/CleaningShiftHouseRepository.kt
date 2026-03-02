package pt.cinzarosa.ajudante.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.cinzarosa.ajudante.entity.CleaningShiftHouse
import pt.cinzarosa.ajudante.entity.CleaningShiftHouseId
import java.time.LocalDate

interface CleaningShiftHouseRepository : JpaRepository<CleaningShiftHouse, CleaningShiftHouseId> {
    fun existsByHouseIdAndCleaningDate(houseId: Int, cleaningDate: LocalDate): Boolean
    fun findAllByHouseIdInAndCleaningDate(houseIds: Collection<Int>, cleaningDate: LocalDate): List<CleaningShiftHouse>
}