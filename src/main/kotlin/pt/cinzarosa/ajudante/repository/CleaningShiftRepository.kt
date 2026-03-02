package pt.cinzarosa.ajudante.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import pt.cinzarosa.ajudante.entity.CleaningShift
import java.time.LocalDate

interface CleaningShiftRepository : JpaRepository<CleaningShift, Int> {

    @Query(
        value = """
        SELECT s.*
        FROM cleaning_shift s
        WHERE s.cleaning_date = :date
          AND (:houseId IS NULL OR EXISTS (
              SELECT 1
              FROM cleaning_shift_house sh
              WHERE sh.cleaning_shift_id = s.id
                AND sh.house_id = :houseId
                AND sh.cleaning_date = :date
          ))
          AND (
              :teamSize = 0
              OR s.id IN (
                  SELECT se.cleaning_shift_id
                  FROM cleaning_shift_employee se
                  GROUP BY se.cleaning_shift_id
                  HAVING COUNT(DISTINCT se.employee_id) = :teamSize
                     AND COUNT(DISTINCT CASE WHEN se.employee_id IN (:employeeIds)
                                             THEN se.employee_id END) = :teamSize
              )
          )
        ORDER BY s.id DESC
        """,
        nativeQuery = true
    )
    fun findByDateHouseAndExactTeam(
        @Param("date") date: LocalDate,
        @Param("houseId") houseId: Int?,
        @Param("employeeIds") employeeIds: Set<Int>,
        @Param("teamSize") teamSize: Int
    ): List<CleaningShift>
}