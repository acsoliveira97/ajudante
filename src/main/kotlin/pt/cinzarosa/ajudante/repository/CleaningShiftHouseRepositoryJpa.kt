package pt.cinzarosa.ajudante.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.cinzarosa.ajudante.entity.CleaningShiftHouse
import pt.cinzarosa.ajudante.entity.CleaningShiftHouseId
import java.time.LocalDate

interface CleaningShiftHouseRepositoryJpa : JpaRepository<CleaningShiftHouse, CleaningShiftHouseId>{
    fun findAllByHouseIdInAndCleaningDate(houseIds: Collection<Int>, cleaningDate: LocalDate): List<CleaningShiftHouse>
}