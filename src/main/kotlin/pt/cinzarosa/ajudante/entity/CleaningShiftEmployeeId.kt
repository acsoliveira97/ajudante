package pt.cinzarosa.ajudante.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class CleaningShiftEmployeeId(
    @Column(name = "cleaning_shift_id")
    val cleaningShiftId: Int = 0,

    @Column(name = "employee_id")
    val employeeId: Int = 0
) : Serializable