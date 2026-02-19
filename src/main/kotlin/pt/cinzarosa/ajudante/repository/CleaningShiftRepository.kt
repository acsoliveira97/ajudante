package pt.cinzarosa.ajudante.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.cinzarosa.ajudante.entity.CleaningShift

interface CleaningShiftRepository : JpaRepository<CleaningShift, Int>